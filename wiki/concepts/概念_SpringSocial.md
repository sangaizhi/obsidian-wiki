---
type: concept
tags:
  - SpringSocial
  - OAuth2
  - 第三方登录
summary: "Spring Social 将基于 OAuth2 的第三方登录流程封装进 Spring Security 过滤器链和连接模型。"
sources:
  - "raw/笔记/spring_security/spring_social_介绍及原理.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：Spring Social

## 定义

Spring Social 是 Spring 生态中连接社交网络和第三方服务的框架，核心用途是封装基于 OAuth2 的第三方登录流程。

## 核心机制

- SocialAuthenticationFilter 加入 Spring Security 过滤器链，引导第三方授权登录流程。
- ServiceProvider 抽象第三方服务提供商。
- OAuth2Operations/OAuth2Template 封装 OAuth2 流程。
- APIAdapter 将服务商用户信息适配到 Connection。
- UsersConnectionRepository 维护本地用户与服务商用户之间的关联关系。

## 关联页面

- [[concepts/概念_OAuth2|OAuth2]]
- [[concepts/概念_SpringSecurity|Spring Security]]
- [[entities/技术_Spring|Spring]]
- [[sources/来源_SpringSecurity与OAuth笔记|Spring Security 与 OAuth 笔记]]

