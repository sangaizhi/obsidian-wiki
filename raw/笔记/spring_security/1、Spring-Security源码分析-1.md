## <p align='center'>Spring Security 基本原理</p>
       Spring Security 是一个可以为基于Spring的应用系统提供声明式的安全访问控制的安全框架。它提供一组可在上下文配置的Bean，通过这些
    扩展和配置这些Bean，我们可以实现功能强大的、高度定制化的身份验证和访问的控制框架。
       
       Spring Security 这个框架是非常复杂的，里面涉及N多的设计模式。所以在分析源码之前，需要对它有一个基本的认识。      
       
       Spring Security在集成到应用系统内后，它可以保护系统内的资源。具体的保护方式就是先拦截请求再进行认证身份和权限验证。所以它会把
    所有的请求都拦截下来，并判断这些请求的资源是否需要认证以及认证的来决定是否能够访问具体的资源；当然，我们是可以配置哪些资源是需要保护
    的；这就是Spring Security 所做的事情。在了解Spring Security干了些啥之后，，我们就可以开始去了解Spring Security的基本原理。
       
       下面这张图表明了请求经过 Spring Security的过程。
   ![Spring Security 过滤器链](https://blog-1253360328.cos.ap-guangzhou.myqcloud.com/assets/spring/spring_security/SpringSecurityFilterChain.png)

       从图中可以看出，Spring Security 是通过一组过滤器对我们访问服务的请求进行过滤，这也是Spring Security最核心的东西。所以所有访
       问服务的请求都会经过这个过滤器链，同样，响应也会从这组过滤器中经过，最后返回到页面。但是这个过滤器链中每一个过滤器都不一样，每一
       个的作用也都不相同。
       eg：图中绿色部分的过滤器主要是针对请求中的信息构建相应的 `Authentication`，并把 `Authenticaion` 放到 `SecurityContext`中。
       最后一个深黄色的过滤器的主要作用主要进行权限校验以及真正的 REST 服务调用。倒数第二个的过滤器主要是做异常转换的，它会把最后一个过
       滤器抛出的异常转换成对应绿色部分过滤器可以处理的异常。在 Spring Boot 应用程序启动时，会自动的把这些过滤器配置到系统中。
