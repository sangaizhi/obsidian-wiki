
## <p align='center'>Spring  Security 源码分析二:过滤器</p>
### 一、 UsernamePasswordAuthenticationFilter 过滤器

   `UsernamePasswordAuthenticationFilter` 主要是用来处理表单登录的请求，它的主要流程就是先调用父类的`AbstractAuthenticationProcessingFilter` 
   的 `diFilter` 方法，在父类的 `doFilter` 方法中在调用自身的 attemptAuthentication` 方法执行具体的认证,并根据认证结果决定是成功
   认证处理还是失败处理。
         
   下面是父类 `AbstractAuthenticationProcessingFilter` 的 doFilter 方法的处理过程源码：
```java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    // 1)、判断当前请filter是否可以处理当前请求，不可以就交由下一个过滤器处理
    if (!requiresAuthentication(request, response)) {
        chain.doFilter(request, response);
        return;
    }
    if (logger.isDebugEnabled()) {
        logger.debug("Request is to process authentication");
    }
    // 认证信息及认证结果
    Authentication authResult;
    try {
        // 2)、 调用具体的过滤器（这里是 UsernamePasswordAuthenticationFilter）进行认证
        authResult = attemptAuthentication(request, response);
        if (authResult == null) {
            // 认证信息为空，立即返回到子类，表名认证未完成
            return;
        }
        // 3)、 认证完成，并且根据认证信息来更新对应的session
        sessionStrategy.onAuthentication(authResult, request, response);
    }
    catch (InternalAuthenticationServiceException failed) {
        // 认证失败，执行认证失败的一些操作
        logger.error( "An internal error occurred while trying to authenticate the user.", failed);
        unsuccessfulAuthentication(request, response, failed);
        return;
    }
    catch (AuthenticationException failed) {
        unsuccessfulAuthentication(request, response, failed);
        return;
    }
    if (continueChainBeforeSuccessfulAuthentication) {
        chain.doFilter(request, response);
    }
    // 4)、认证成功，执行认证成功后的回调，主要是把认证信息放到 Spring 的上下文中
    successfulAuthentication(request, response, chain, authResult);
}
```
`AbstractAuthenticationProcessingFilter` 的 doFilter 的主要处理过程：

    1). 判断当前的过滤器是否可以处理当前的请求，如果可以就继续有当前过滤器处理；如果不行就把请求交给过滤器链中的下一个过滤器处理;
    2). 使用子类(这里是 `UsernamePasswordAuthenticationFilter` )的具体认证认证方法进行认证，并返回认证结果。
    3). 认证成功以后，就执行一些与 `session` 相关的操作(创建新的 `session` 或者合并 `session` 或者什么也不做)
    4). 认证成功后，执行成功认证的回调。在该回调中，Spring Security 执行的操作主要是把认证信息放到 `SecurityContextHolder` 中。
    在这个回调中，Spring Security 提供了一个接口供我们扩展该回调。比如记录日志等。

在父类的`doFilter`方法中的的第二步，调用了具体子类（`UsernamePasswordAuthenticationFilter`）的 `attemptAuthentication` 方法来执行具体的认证，该方法的源码如下：
```java
public Authentication attemptAuthentication(HttpServletRequest request,
    HttpServletResponse response) throws AuthenticationException {
    // 1)、判断该请求的请求方式是不是 POST， UsernamePasswordAuthenticationFilter 过滤器只处理 POST 请求
    if (postOnly && !request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException(
           "Authentication method not supported: " + request.getMethod());
    }
    // 2)、从请求中取出 username 和 password
    String username = obtainUsername(request);
    String password = obtainPassword(request);
    if (username == null) {
      username = "";
    }
    if (password == null) {
      password = "";
    }
    username = username.trim();
    // 3)、使用用户名和密码构建一个 Authentication，
    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
        username, password);
    // 4)、一个可以扩展的地方，可以供子类设置 details 信息
    setDetails(request, authRequest);
    // 5)、认证 Authentication，是否有效
    return this.getAuthenticationManager().authenticate(authRequest);
}
```
`attemptAuthentication` 方法执行具体认证的过程：

    1). 判断当前拦截到的请求是不是 `POST` 方式的请求，`UsernamePasswordAuthenticationFilter` 这个过滤器只处理 `POST` 方式的请求。
    2). 从当前请求的请求参数中取出用户名和密码，对应请求参数的名称是支持自定义的，默认是，自定义的方式如下：
    
```java
  protected void configure(HttpSecurity http) throws Exception {
     http.formLogin().loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL) // 配置需要认证时跳转的页面
             .usernameParameter("user")  // 自定义用户名参数的名称
             .passwordParameter("pass") // 自定义密码参数的名称
             .loginProcessingUrl(securityProperties.getBrowser().getSignInProcessUrl()) // 配置登录请求处理的url
             .successHandler(customAuthenticationSuccessHandler) // 登录成功处理器
             .failureHandler(customAuthenticationFailureHandler); // 登录失败处理器
   }
```

    3). 使用第二步拿到的用户名和密码构建一个未认证的 `Authentication`，其中包含密码信息，再认证成功后会进行密码擦除。
    4). 在这一步中，是Spring Security 留给我们扩展的地方，我们可以通过继承 `UsernamePasswordAuthenticationFilter` 这个过滤器并重
    写 `setDetails` 方法来实现扩展。
    5). 调用 `AuthenticationManager` 的具体实例(实际上是 `ProviderManager` )来认证当前的 `Authentication`，并返回认证结果。这一
    步的具体分下放到下一节。
    
至此， `UsernamePasswordAuthenticationFilter` 这个过滤器执行的主要操作已经分析完了。

### 二、 BasicAuthenticationFilter 过滤器

   `BasicAuthenticationFilter` 这个过滤器主要的用途就是处理基于Basic认证方式的认证请求。
#### 2.1.认证的基本原理
        在客户端端向 HTTP 服务器进行数据请求时，如果客户端么有被认证，Http服务器则会通过基本认证过程对客户端的用户名和密码进行验证，以
    决定客户端是否合法。客户端在接收到HTTP服务器的身份认证请求后，会提示输入用户名和密码，然后将用户名和密码进行 BASE64 加密，加密后的
    密文（BASE64(username+":"+password)）将会附加在请求信息中.然后再每一次的请求数据时，都会讲密文附加在请求头中。
#### 2.2. 认证的过程
        `BasicAuthenticationFilter` 过滤器在实现时继承了一个名字为 `OncePerRequestFilter` 的过滤器，`OncePerRequestFilter` 这
    个过滤器的主要作用就是屏蔽底层 Servlet 容器的过滤器对请求的处理次数的方式的不同，使得所有经过继承该类的过滤滤器的请求都只会被处理一
    次。
        `BasicAuthenticationFilter` 基本认证过程就是先调用父类的 `doFilter` 方法，然后在父类的 `doFilter` 方法中调用 
    `BasicAuthenticationFilter` 的 `doFilterInternal` 方法来实际的认证客户端是否合法。主要代码如下：   

##### 2.2.1 父类 `OncePerRequestFilter` 的 `doFilter` 方法
    在父类中主要是判断当前的过滤器是否已经处理过当前的请求，处理过就跳过，没处理就由子类处理并标记已处理过当前请求。
    主要的代码实现如下：
```java
public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
   throws ServletException, IOException {
  if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
    throw new ServletException("OncePerRequestFilter just supports HTTP requests");
  }
  HttpServletRequest httpRequest = (HttpServletRequest) request;
  HttpServletResponse httpResponse = (HttpServletResponse) response;
  // 1)、获取当前过滤器的名称,判断该过滤器是否已经处理过该请求
  String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
  boolean hasAlreadyFilteredAttribute = request.getAttribute(alreadyFilteredAttributeName) != null;
  if (hasAlreadyFilteredAttribute || skipDispatch(httpRequest) || shouldNotFilter(httpRequest)) {
    // 调过当前的过滤器，由下一个过滤器继续执行
    filterChain.doFilter(request, response);
  }
  else {
    // 2)、设置当前过滤器已经处理过当前请求，这个操作和上面的判断相关
    request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
    try {
      // 3)、调用子类的方式去实际的处理认证
      doFilterInternal(httpRequest, httpResponse, filterChain);
    }
    finally {
      // 认证完成了就需要移除掉当前请求的是否处理的标记
      request.removeAttribute(alreadyFilteredAttributeName);
    }
  }
}
```
    代码解释：
    1). 判断当前的 `Filter`(这里指 `BasicAuthenticationFilter`) 是否已经处理过当前的请求；如果处理过，则把当前的请求交给过滤器链中
    的下一个过滤器处理， 如果没有处理，则继续执行下面的步骤。
    2). 通过往 request 中设置属性的方式来标记当前的 `Filter`已经处理过该请求。
    3). 调用子类 `BasicAuthenticationFilter` 的 `doFilterInternal` 方法来具体的认证用户信息。
    4). 移出之前往 request 中设置的标记属性。

##### 2.2.2 子类 `BasicAuthenticationFilter` 的 `doFilterInternal` 方法
    子类的这个方法才是实际上认证客户端的地方。在这个方法里，Spring Security 会基于 Basic 认证方式从请求中拿出相应的信息来认证客户端。
    代码如下：
    
```java
@Override
protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    final boolean debug = this.logger.isDebugEnabled();
    // 1)、从请求头拿出认证信息，并根据拿到的信息判断是不是需要使用  BasicAuthenticationFilter 过滤器进行认证
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Basic ")) {
        // 不需要使用 BasicAuthenticationFilter 过滤器处理，直接交由下一个处理处理
        chain.doFilter(request, response);
        return;
    }

    try {
        // 2)、从请求头中拿到 用户名和密码信息，主要操作就是 BASE64解密和用户名密码拆分
        String[] tokens = extractAndDecodeHeader(header, request);
        // 要求认证信息是包含两部分（用户名和密码）
        assert tokens.length == 2;
        // 用户名
        String username = tokens[0];
        // 省略日志记录的代码
        // 3)、判断用户名是否需要认证（已经认证过的用户不需要认证，从上下文拿到认证信息）
        if (authenticationIsRequired(username)) {
          // 4)、根基用户名创建认证信息
          UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, tokens[1]);
          authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
          // 5)、从所有的认证管理器中的找出(如何找，后面分析)支持认证当前 authRequest 的认证器认证当前的 authRequest
          Authentication authResult = this.authenticationManager.authenticate(authRequest);
          SecurityContextHolder.getContext().setAuthentication(authResult);
          this.rememberMeServices.loginSuccess(request, response, authResult);
          // 6)、成功认证处理
          onSuccessfulAuthentication(request, response, authResult);
        }
    } 
    catch (AuthenticationException failed) {
        SecurityContextHolder.clearContext();
        this.rememberMeServices.loginFail(request, response);
        // 7)、认证失败处理
        onUnsuccessfulAuthentication(request, response, failed);
        if (this.ignoreFailure) {
          chain.doFilter(request, response);
        }
        else {
          this.authenticationEntryPoint.commence(request, response, failed);
        }
        return;
    }
    chain.doFilter(request, response);
}

/**
 * 从请求头中抽取用户名和密码信息
 */
private String[] extractAndDecodeHeader(String header, HttpServletRequest request)
			throws IOException {
    // 5.1)、请求头中 Authorization 属性的值得一部分（Basic  后面的一部分，注意空格）
    // Authorization 的值类似于 Basic jdfklasjkfagdfjsdasdasdasdasdas
    byte[] base64Token = header.substring(6).getBytes("UTF-8");
    byte[] decoded;
    try {
        // 2)、BASE64 解密 认证信息
        decoded = Base64.decode(base64Token);
    }
    catch (IllegalArgumentException e) {
        throw new BadCredentialsException( "Failed to decode basic authentication token");
    }
    String token = new String(decoded, getCredentialsCharset(request));
    // 认证信息中用户名和密码的分隔符
    int delim = token.indexOf(":");
    if (delim == -1) {
        throw new BadCredentialsException("Invalid basic authentication token");
    }
    // 把解密后的认证信息用 ':' 分隔符分开，并组成一个数组返回
    return new String[] { token.substring(0, delim), token.substring(delim + 1) };
	}
```
`BasicAuthenticationFilter` 的 `doFilterInternal` 认证客户端的过程总结就是：

    1). 从请求头里面拿到用户的认证信息（header  里面的 Authorization 属性）；如果拿不到，就直接把该请求交给下一个 `Filter` 处理，
    拿到了也需要判断是不是 `Basic` 认证，是的话才会继续下去。
    2). 从请求头里面取出用户名和密码两个信息：
      2.1). 截取请求头中 `Authorization` 属性值的一部分('Basic '后面的那部分)；
      2.2). 将截取得到的字符串进行 BASE64 解码，得到解码后的字符串；
      2.3). 根据 ':' 分隔符将解密后字符串分割成两部分，前一部分就是用户名，后一部分就是密码；
      2.4). 把用户名和密码组成数组返回；
    3). 判断用户是否需要认证，主要是判断当前用户名是否已经认证（从 `SecurityContextHolder` 中判断）,需要认证则继续下面的判断，否则
    交给下一个 `Filter`；
    4). 用前面拿到的用户名和密码构造一个还未认证的 `Authentication` 。
    5). 使用 Spring Security 的所有认证管理器(包括自定义的)中找出（怎么找在后面分析）支持认证当前 `Authentication` 的认证器去认证
    当前的 `Authentication`；
    6). 认证成功处理，和 `UsernamePasswordAuthenticationFilter` 一样，里面是支持自定义扩展的。
    7). 认证失败处理。

### 三、 AnonymousAuthenticationFilter 过滤器

   `AnonymousAuthenticationFilter` 这个过滤器在名称上就可以看出一个匿名认证的过滤器。它的主要作用就是在 Spring Security 需要认证，
   但 `SecurityContextHolder` 中恰巧又没有一个 `Authentication` 对象时，使用一个随机UUID、名称为`anonymousUser`的用户名和包含角
   色`ROLE_ANONYMOUS`的 authorities 去创建一个匿名的 `Authentication` 。并将这个 `Authentication` 放到 SecurityContextHolder
   中。
   具体的代码就是：
#### 3.1. `doFilter` 方法
```java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
   throws IOException, ServletException {

 if (SecurityContextHolder.getContext().getAuthentication() == null) {
   SecurityContextHolder.getContext().setAuthentication(
       createAuthentication((HttpServletRequest) req));
 }
 chain.doFilter(req, res);
}
```
从代码中可以看出在判断 SecurityContextHolder 中没有 `Authentication` 时，就去创建一个 `Authentication`。所以第二个看的就是它如何
创建一个 `Authentication` 的。

#### 3.2. 创建 `Authentication` 的方法
```java
protected Authentication createAuthentication(HttpServletRequest request) {
   AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(key,
       principal, authorities);
   auth.setDetails(authenticationDetailsSource.buildDetails(request));
   return auth;
    }
```
可以看出在创建这个 `Authorization`的时候，代码中使用一个三个变量：
* key, 来源于构造方法的参数，构造方法是在 `AnonymousConfigurer` 这个类的 `init`方法中调用的，而 `init` 方法是通过 
`UUID.randomUUID().toString()` 这样方式产生 key 的。所以说这个 key  就是一个随机的 UUID。
* principal, 相当于用户名，这个属性来源可以是`AnonymousConfigurer` 这个类的 `init` 方法,也可以 `AnonymousAuthenticationFilter`
本身构造方法的字符串常量。当然，这两个来源得到的值都是一样的，就是字符串 anonymousUser，所以这个类创建的 `Authentication` 的用户名
就是 anonymousUser，所以这个类创建的。
* authorities, 代表当前匿名用户拥有的角色，和 `principal` 变量一样，有两个来源，并且产生的结果都是包含一个名称为ROLE_ANONYMOUS 的角色。
