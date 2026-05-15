---
type: concept
tags:
  - SpringSecurity
  - 认证
  - 授权
summary: "Spring Security 通过过滤器链和拦截器组织 Web 请求认证、身份上下文维护和访问授权。"
sources:
  - "raw/笔记/spring_security/1、Spring-Security源码分析-1.md"
  - "raw/笔记/spring_security/2、Spring-Security源码分析-2.md"
  - "raw/笔记/spring_security/3、Spring-Security源码分析-3.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：Spring Security

## 定义

Spring Security 是 Spring 生态中的安全框架，用过滤器链处理认证、授权和安全上下文。

## 过滤器链

- UsernamePasswordAuthenticationFilter：处理用户名密码登录。
- BasicAuthenticationFilter：处理 HTTP Basic 认证。
- AnonymousAuthenticationFilter：在未认证时构建匿名 Authentication。

## 授权拦截

- FilterSecurityInterceptor：保护 Web 请求，在调用目标服务前执行权限判断。
- MethodSecurityInterceptor：保护业务方法。
- AbstractSecurityInterceptor：为不同拦截器提供 beforeInvocation、finallyInvocation、afterInvocation 等通用流程。

## 关联页面

- [[entities/技术_Spring|Spring]]
- [[concepts/概念_OAuth2|OAuth2]]
- [[concepts/概念_SpringSocial|Spring Social]]
- [[sources/来源_SpringSecurity与OAuth笔记|Spring Security 与 OAuth 笔记]]

