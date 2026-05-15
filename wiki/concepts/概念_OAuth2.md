---
type: concept
tags:
  - OAuth2
  - 授权
  - 安全
summary: "OAuth2 是让第三方应用在不获取用户密码的情况下获得有限资源访问权限的授权协议。"
sources:
  - "raw/笔记/spring_security/oauth.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：OAuth2

## 定义

OAuth2 是授权协议，用令牌替代账号密码，让第三方应用获得有限、可撤销、可过期的资源访问权限。

## 角色

- Resource Owner：资源所有者，通常是用户。
- Client：第三方应用。
- Authorization Server：认证服务器，负责认证用户并发放 token。
- Resource Server：资源服务器，保存资源并校验 token。
- Provider：服务提供商，包含认证服务器和资源服务器。

## 授权模式

- 授权码模式：流程完整严密，适合有后端的客户端。
- 简化模式：直接返回 token，适合静态客户端。
- 密码模式：用户向客户端提供账号密码，由客户端换取 token。
- 客户端模式：客户端以自身身份访问资源。

## 关联页面

- [[concepts/概念_SpringSecurity|Spring Security]]
- [[concepts/概念_SpringSocial|Spring Social]]
- [[sources/来源_SpringSecurity与OAuth笔记|Spring Security 与 OAuth 笔记]]

