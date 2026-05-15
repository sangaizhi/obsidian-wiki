
在我们开发的应用中，通常会有第三方登录的需求（例如：QQ、微信、微博）。而第三方服务商开放的登录接口一般都是基于 OAuth 协议（国内一般基于 OAuth2 协议）。所以 Spring 官方为了开发便利，提供了 Spring Social  框架。 Spring Social 框架的目的就是将我们的系统连接社交网络。

### 一、Spring Social 的基本原理
#### 1、基本原理
下面通过一张图的方式了解 Spring Social 的基本原理：
![Spring Social的基本原理](https://blog-1253360328.cos.ap-guangzhou.myqcloud.com/assets/spring/spring_security/image1.png)
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在上图的第8步中，根据用户构建了一个 Authentication 放进 SecurityContext里面就意味着用户使用第三方登录成功了。
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如果我们引导用户完成了上图的流程，实际上就表示用户使用第三方服务提供商完成了登录。这就是常见的第三方登录的基本原理和流程。
#### 2、Spring Social 与 Spring Security 的结合    
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在完成整个流程的过程中，Spring Social 在其中有做了什么事情？实际上，Spring Social是把上面的整个步骤封装了起来，做成了过滤器(SocialAuthenticationFilter)并加入的 Spring Security 的过滤器链中(在UsernamePasswordAuthenticationFilter)之后。这样的话当我们在访问一个请求的时候，SocialAuthenticationFilter 拦截器就会把我们的这个请求拦截下来，然后引导我们走完上图的整个授权流程，从而实现第三方登录。这就是 Spring Social 做的最核心的事情。Spring Social 是如何把上面的流程封装到 Spring Security 所特有的接口和类中的呢？这些接口和类之间的依赖关系又是怎样的。
下面通过图的方式来说明这些接口和类。后续再详细说明
![Spring Social的基本原理](https://blog-1253360328.cos.ap-guangzhou.myqcloud.com/assets/spring/spring_security/image2.png)       

##### 2.1、ServiceProvider 接口
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ServiceProvider 接口实际上是对服务提供商的抽象，每一个服务提供商都会提供一个自己的 ServiceProvider 的实现。当然， Spring Social 也为我们提供了一个默认实现：AbstractOAuth2ServiceProvider。这个抽象类中已经为我们实现了一些服务提供商共有的东西。
##### 2.2、OAuthOperations 接口
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;从 Spring Social的基本原理流程图上可以看出，第2步到第6步都是一个标准化的流程。对于这部分的流程，Spring Social 也为我们提供一个接口(OAuth2Operations)。在这个接口中，定义了关于 OAuth2 协议相关的操作。同样，该接口也有一个默认的抽象实现：OAuth2Template,OAuth2Template 这个类会帮助我们去完成 OAuth2 协议的执行的流程。所以在一般情况下，我们只需要继承该类，就可以操作 OAuth2 协议。
##### 2.3、 API 接口
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在Spring Social 的基本原理流程图上的第7步是获取用户信息，但是，针对不同的服务提供商，各自的用户信息也是有区别的，所有 API 是没有明确定义的。所以在这一步，Spring Social 并没有做过多的事情。所以这一步需要我们自己去写一个接口来封装获取用户信息的行为。当然，Spring Social 也为我们提供了一个 AbstractOAuth2APIBinding 抽象类,用来帮助我们快速实现 APIBinding 接口。
##### 2.4、Connection  接口
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Connection 接口是用来封装第7步获取到的用户信息，常用的是 OAuth2Connection。Connection 是由一个 ConnectionFactory 类创建的。一般我们常用的 ConnectionFactory 是 OAuth2ConnectionFactory。这个工厂负责创建 Connection 实例，也就是创建一个包含了用户信息的一个对象。为了创建这个对象，我们就需要拿到用户信息，而用户信息是通过完成完成第2到第7这几个步拿到的，所以为了完成这几个步骤，就需要一个 ServiceProvider 对象。当然在 ConnectionFactory 中实际上是包含一个 ServiceProvider 实例的。走到这里我们已经拿到了一个封装用户信息的 Connection 实例。但是这个 Connection 对象的数据结构是固定的，而服务提供商提供的数据接口确实不同。所以为了能把服务提供商的用户信息封装到 Connection 实例中，我们就需要一个中间类来完成这个步骤；所以 Spring Social 提供了了一个 APIAdapter 接口来帮助我们完成用户信息到 Connection 的适配。
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;到此，我们已经清楚 Connection 封装的是服务提供商提供的用户信息。而在我们的实际业务系统中，我们一般也会用一张表来存储我们自己的用户信息。那么我们该怎么把服务提供商的用户信息与我们业务系统的用户信息进行关联呢？换句话说，就是怎么确定服务提供商的A用户登录代表我们业务系统的B用户登录呢？实际上，Spring Social 已经为我们考虑了这个问题。Spring Social 提供了一张关联表(表名是 UserConnection,具体的SQL文件在 spring-social-core-x.xx.jar 中的 org.springframework.social.connect.jdbc 包下)，这张关联表存储了我们业务系统用户的id和服务提供商用户信息的关联关系。既然有了表，那么我们就得有操作这张表的操作类；对此，Spring Social  中有一个 UsersConnectionRepository 接口，当然既然是操作数据库，我们就是使用它的实现类 JdbcUsersConnectionRepository。从这个类的名字可以看出，这是一个存储器，在这个类中，我们只需要提供一个 DataSource，它就可以帮助我们根据 Connection 实例来操作 UserConnection 表。
