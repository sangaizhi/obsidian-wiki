---
type: source
tags:
  - source
  - SpringSecurity
  - OAuth2
  - SpringSocial
summary: "raw/笔记/spring_security 下关于 Spring Security 过滤器、拦截器、OAuth2 与 Spring Social 的源码分析笔记。"
sources:
  - "raw/笔记/spring_security/1、Spring-Security源码分析-1.md"
  - "raw/笔记/spring_security/2、Spring-Security源码分析-2.md"
  - "raw/笔记/spring_security/3、Spring-Security源码分析-3.md"
  - "raw/笔记/spring_security/oauth.md"
  - "raw/笔记/spring_security/spring_social_介绍及原理.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 来源：Spring Security 与 OAuth 笔记

## 来源信息

- 来源目录：`raw/笔记/spring_security`
- 类型：源码分析与协议学习笔记
- 覆盖主题：[[concepts/概念_SpringSecurity|Spring Security]]、[[concepts/概念_OAuth2|OAuth2]]、[[concepts/概念_SpringSocial|Spring Social]]

## 核心要点

- Spring Security 的 Web 安全能力主要通过过滤器链组织认证与授权逻辑。
- UsernamePasswordAuthenticationFilter、BasicAuthenticationFilter、AnonymousAuthenticationFilter 分别处理表单登录、HTTP Basic 和匿名认证。
- FilterSecurityInterceptor 与 MethodSecurityInterceptor 在请求或方法调用前后执行权限校验。
- OAuth2 通过授权码、隐式、密码、客户端等模式让第三方应用在不接触用户密码的情况下获得有限授权。
- Spring Social 将第三方登录流程封装成过滤器，并通过 ServiceProvider、OAuthOperations、ConnectionFactory、UsersConnectionRepository 等接口完成适配。

## 关联页面

- [[entities/技术_Spring|Spring]]
- [[concepts/概念_SpringSecurity|Spring Security]]
- [[concepts/概念_OAuth2|OAuth2]]
- [[concepts/概念_SpringSocial|Spring Social]]
- [[overview/主题_Java后端技术栈综述|Java 后端技术栈综述]]

