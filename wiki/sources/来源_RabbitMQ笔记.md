---
type: source
tags:
  - source
  - RabbitMQ
  - 消息队列
summary: "raw/笔记/rabbitmq 下关于 AMQP、RabbitMQ 模型、路由、可靠投递、消费治理、Spring 集成和面试题的笔记。"
sources:
  - "raw/笔记/rabbitmq/1、rabbitmq介绍.md"
  - "raw/笔记/rabbitmq/2、rabbitMQ整体架构与消息流转.md"
  - "raw/笔记/rabbitmq/6、Exchange 交换机.md"
  - "raw/笔记/rabbitmq/7、Binding-Queue-Message-VirtualHost .md"
  - "raw/笔记/rabbitmq/8、如何保证消息100%投递成功 .md"
  - "raw/笔记/rabbitmq/9、幂等性及主流解决方案.md"
  - "raw/笔记/rabbitmq/10、Confirm 确认消息.md"
  - "raw/笔记/rabbitmq/11、Return 消息机制.md"
  - "raw/笔记/rabbitmq/13、消费端的限流.md"
  - "raw/笔记/rabbitmq/14、消费端ACK与重回队列.md"
  - "raw/笔记/rabbitmq/15、TTL队列与消息.md"
  - "raw/笔记/rabbitmq/16、死信队列.md"
  - "raw/笔记/rabbitmq/17、整合Spring AMQP.md"
  - "raw/笔记/rabbitmq/18、整合Spring Boot.md"
  - "raw/笔记/rabbitmq/19、RabbitMQ 面试题.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 来源：RabbitMQ 笔记

## 来源信息

- 来源目录：`raw/笔记/rabbitmq`
- 类型：技术学习笔记、面试题、示例项目资料
- 覆盖主题：[[concepts/概念_RabbitMQ基础模型|RabbitMQ 基础模型]]、[[concepts/概念_RabbitMQ可靠性投递|RabbitMQ 可靠性投递]]、[[concepts/概念_RabbitMQ消费端治理|RabbitMQ 消费端治理]]、[[concepts/概念_RabbitMQ与Spring集成|RabbitMQ 与 Spring 集成]]

## 核心要点

- RabbitMQ 基于 AMQP 模型，围绕 Exchange、Queue、Binding、Message、VirtualHost、Channel 等概念组织消息流转。
- Exchange 负责路由消息，Direct、Topic、Fanout、Headers 等类型适配不同路由场景。
- 生产端可靠投递依赖消息落库、Confirm、Return、备份交换机等机制组合。
- 消费端治理关注手工 ACK/NACK、重回队列、消费限流、TTL、死信队列和幂等消费。
- Spring AMQP 与 Spring Boot 集成提供 RabbitAdmin、RabbitTemplate、监听容器、消息转换器等工程接口。

## 关联页面

- [[entities/技术_RabbitMQ|RabbitMQ]]
- [[concepts/概念_RabbitMQ基础模型|RabbitMQ 基础模型]]
- [[concepts/概念_RabbitMQ可靠性投递|RabbitMQ 可靠性投递]]
- [[concepts/概念_RabbitMQ消费端治理|RabbitMQ 消费端治理]]
- [[concepts/概念_RabbitMQ与Spring集成|RabbitMQ 与 Spring 集成]]
- [[overview/主题_Java后端技术栈综述|Java 后端技术栈综述]]

