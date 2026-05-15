## <p align=center>Spring  Security 源码分析三:拦截器</p>
### 一、FilterSecurityInterceptor
#### 1.1、主要职能
    `FilterSecurityInterceptor`在 Spring Security 的过滤器链中担任着非常重要的角色，它是我们自身 REST 服务的守门员，
    `FilterSecurityInterceptor` 将用于受保护的 Web 请求。在这个类里面，Spring  Security 会判断当前的请求是否能够有权限访问我们的  
    REST服务，并且会通过代理的方式去调用我们的 REST 服务。
#### 1.2、主要步骤
    `FilterSecurityInterceptor`处理请求主要通过 `invoke()` 方法来完成，处理思路就是在调用目标 REST 服务的前后添加一些操作。
    具体的实现步骤如下：
       1). 首先，判断当前的这个过滤器是否已经处理过该请求，只有没有处理过得请求才会被处理。
       2). 其次，在该方法中会调用父类的 `beforeInvocation()` 方法执行一个认证和权限的校验。
       3). 第三，不管调用目标服务是否发生问题（异常），都需要执行恢复在 `beforeInvocation()`方法中修改的 `SecurityContext`.
       4). 最后，处理请求返回的结果，如果有注入 `AfterInvocationManager`，就会调用它的 `decide()` 方法决定返回的结果。
    核心代码如下：
    
```java
public void invoke(FilterInvocation fi) throws IOException, ServletException {
  if ((fi.getRequest() != null) && (fi.getRequest().getAttribute(FILTER_APPLIED) != null) && observeOncePerRequest) {
        // 1) 判断针对当前的请求该过滤器是否已经执行，
  	fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
  } else {
        // 第一次执行
	if (fi.getRequest() != null) {
		fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
	}
        // 2) 调用 REST 服务前的认证及权限校验操作
	InterceptorStatusToken token = super.beforeInvocation(fi);
	try {
		fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
	}
	finally {
                // 3) 恢复 SecurityContext
		super.finallyInvocation(token);
	}
        // 4) 处理返回结果
	super.afterInvocation(token, null);
  }
}
```
     在 `invoke()` 方法中，最重要也是最核心的步骤就是第2步，调用父类 `AbstractSecurityInterceptor` 的 `beforeInvocation()`方法
     进行用户验证。`beforeInvocation()` 方法具体的逻辑在 `AbstractSecurityInterceptor` 中分析。

### 二、 MethodSecurityInterceptor
#### 2.1、主要职能
    `MethodSecurityInterceptor` 主要是实现对业务方法的保护。
    
### 三、AbstractSecurityInterceptor
#### 3.1 介绍
    `AbstractSecurityInterceptor`是一个抽象的父类，主要是实现一些公用的方法供子类（`MethodSecurityInterceptor`、
    `FilterSecurityInterceptor`,`CustomSecurityFilter`,`AspectJMethodSecurityInterceptor`）调用。
#### 3.2 主要方法介绍
##### 3.2.1、 beforeInvocation() 方法
    `beforeInvocation()` 方法主要都是做了一些在真正请求受保护对象之前的一些必要操作，这个操作包含以下几个：
     1). 判断请求的受保护的对象是不是当前过滤器配置的受保护的对象；
     2). 获取配置的权限信息；
     3). 认证用户的身份；
     4). 进行目标资源的权限认证；
     5). 使用上下文原有的用户信息和权限信息构建一个新的 Authentication；
     6). 使用新的 Authentication 信息替换 SecurityContext 中的 AUthentication 信息。
     
     下面是 `beforeInvocation()` 的方法的具体代码：       
```java
protected InterceptorStatusToken beforeInvocation(Object object) {
  // 1) 当前被判断的对象是不是要判断的对象
  if (!getSecureObjectClass().isAssignableFrom(object.getClass())) {
    throw new IllegalArgumentException(
      "Security invocation attempted for object " + object.getClass().getName()
          + " but AbstractSecurityInterceptor only configured to support secure objects of type: "
          + getSecureObjectClass());
    }
  // 2) 取出配置的权限信息，权限信息的来源 ExpressionUrlAuthorizationConfigurer
  // 或者  UrlAuthorizationConfigurer 两个配置类的配置的权限信息
  Collection<ConfigAttribute> attributes = this.obtainSecurityMetadataSource().getAttributes(object);
  // 针对当前的过滤器 (FilterSecurityInterceptor) 没有配置权限信息，表示该烂机器不允许进行公共调用
  if (attributes == null || attributes.isEmpty()) {
    if (rejectPublicInvocations) {
      throw new IllegalArgumentException(
          "Secure object invocation " + object   + " was denied as public invocations are not allowed via this interceptor. "
              + "This indicates a configuration error because the "
              + "rejectPublicInvocations property is set to 'true'");
    }
    publishEvent(new PublicInvocationEvent(object));
    return null; // no further work post-invocation
  }
  if (SecurityContextHolder.getContext().getAuthentication() == null) {
    credentialsNotFound(messages.getMessage(
      "AbstractSecurityInterceptor.authenticationNotFound",  "An Authentication object was not found in the SecurityContext"),
       object, attributes);
  }
  // 3). 使用 AuthenticationManager 对当前的用户的身份进行认证，会使用具体的 AuthenticationProvider进行认证，具体的认证步骤在后面
  // 分析
  Authentication authenticated = authenticateIfRequired();
  // 4) 资源的权限认证
  try {
    this.accessDecisionManager.decide(authenticated, object, attributes);
  }
  catch (AccessDeniedException accessDeniedException) {
    publishEvent(new AuthorizationFailureEvent(object, attributes, authenticated, accessDeniedException));
    throw accessDeniedException;
  }
  if (publishAuthorizationSuccess) {
    publishEvent(new AuthorizedEvent(object, attributes, authenticated));
  }
  // 5) 使用原有的 Authentication 信息和权限信息构建一个新的 Authentication 信息
  Authentication runAs = this.runAsManager.buildRunAs(authenticated, object, attributes);
  if (runAs == null) {
    // 构建新的 Authentication 信息失败了
    return new InterceptorStatusToken(SecurityContextHolder.getContext(), false, attributes, object);
  }
  else {
    // 6) 替换原有 SecurityContext中的 Authentication信息
    SecurityContext origCtx = SecurityContextHolder.getContext();
    SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
    SecurityContextHolder.getContext().setAuthentication(runAs);
    // need to revert to token.Authenticated post-invocation
    return new InterceptorStatusToken(origCtx, true, attributes, object);
  }
}    
```
    在 `beforeInvocation()` 方法中，有几个比较重要的参数：
    1). ConfigAttribute
        从上面的分析可以看出，` beforeInvocation() `方法中进行鉴权的时候使用的 `AccessDecisionManager`的 `decide()`方法实现的。
        `decide()` 方法是需要接受当前的用户信息、受保护的对象以及受保护对象对应的的 `ConfigAttribute`集合作为参数进行鉴权。具体的
        `ConfigAttribute` 对象是什么，需要看 `AccessDecisionManager`的实现者是哪个类，可能是一个简单的角色名称。
        `AbstractSecurityInterceptor` 是使用一个 `SecurityMetadataSource` 对象来获取与受保护对象相关的 `ConfigAttribute` 
        集合。在实际使用中， `ConfigAttribute` 将通过注解的形式定义在受保护的方法上，或者通过 access属性定义在受保护的 URL上。
        例如 `@RolesAllowed({"USER", "ADMIN"})` 或者 `<intercept-url pattern="/user/list" access="ROLE_USER,ROLE_ADMIN"/>` 
        就表示将 `ConfigAttribute ROLE_USER`  和 `ConfigAttribute ROLE_ADMIN` 应用在 `/user/list` 这个请求的URL 上 在默认的
        `AccessDecisionManager` 实现中，上面的配置就表示用户所拥有的权限中只要拥有一个 `GrantedAuthority` 与这两个 
        `ConfigAttribute` 中的任意一个匹配就允许访问。但是， `ConfigAttribute` 仅仅只是一个简单的配置属性而已，这个属性具体的含义
        还是由 `AccessDecisionManager` 来决定。
    2). RunAsManager
        `RunAsManager` 意义就是为受保护的对象创建一个临时的 `Authenticaion`。在 `AbstractSecurityInterceptor`的 
        `beforeInvocation()` 方法中，`RunAsManager` 在 `Authentication` 认证成功后，会使用原来的 `Authenticaion`和 
        `ConfigAttribute` 构建一个新的 `Authenticaion`。如果新的 `Authenticaion`不为空，就产生一个新的 `SecurityContext`，
        并把新的 `Authenticaion` 设置到存放到其中。这样的话，在请求受保护的资源时，从 `SecurityContext` 中拿到的就是新的 
        `AUthentication`。当然，在请求结束后，`FilterSecurityInterceptor`回调用父类的 `finallyInvocation()` 方法，把原来的
         `SecurityContext` 重新设置到 `SecurityContextHolder`中。
##### 3.2.2. finallyInvocation() 方法
    在请求结束后，把在 `beforeInvocation()`方法中修改的 `SecurityContext` 恢复到原本的 `SecurityContext`。
##### 3.2.3. afterInvocation() 方法
    `afterInvocation` 的主要功能就是受保护的WEB请求调用完成后，对请求的返回值进行修改。其修改返回值的方式和 `AuthenticationManager`
    的方式类似，通过其维护的一系列的 `AfterInvocationProvider`，依次根据是否能够修改该返回值进而修改返回值。当然，修改返回值时也可能
    会抛出异常，例如后置的权限检查。
