---
type: concept
tags:
  - SpringCloud
  - Config
  - 微服务
summary: "Spring Cloud Config 用 Config Server 和 Config Client 为微服务提供集中化外部配置管理。"
sources:
  - "raw/笔记/springcloud/4、Spring Cloud Config使用和原理.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：Spring Cloud Config

## 定义

Spring Cloud Config 是 Spring Cloud 体系中的集中配置组件，用于让多个微服务从统一配置源获取配置。

## 解决问题

分布式系统中每个服务都有配置文件，手工维护容易分散、重复和不一致。Config 将配置集中到统一仓库，由服务端读取，再由客户端远程获取。

## 基本结构

- Config Server：从配置仓库读取配置并提供接口。
- Config Client：启动或刷新时从 Config Server 拉取配置。

## 关联页面

- [[entities/技术_Spring|Spring]]
- [[concepts/概念_Spring核心思想|Spring 核心思想]]
- [[sources/来源_SpringCloudConfig笔记|Spring Cloud Config 笔记]]

