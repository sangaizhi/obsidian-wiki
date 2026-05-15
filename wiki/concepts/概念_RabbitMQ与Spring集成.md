---
type: concept
tags:
  - RabbitMQ
  - Spring
  - SpringAMQP
summary: "RabbitMQ 与 Spring 集成通过 Spring AMQP、RabbitTemplate、监听容器和消息转换器降低消息收发工程复杂度。"
sources:
  - "raw/笔记/rabbitmq/17、整合Spring AMQP.md"
  - "raw/笔记/rabbitmq/18、整合Spring Boot.md"
  - "raw/笔记/rabbitmq/19、RabbitMQ 面试题.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：RabbitMQ 与 Spring 集成

## 定义

RabbitMQ 与 Spring 集成指通过 Spring AMQP 和 Spring Boot 自动配置来声明队列、发送消息、监听消费和转换消息体。

## 关键组件

- RabbitAdmin：声明和管理交换机、队列、绑定等资源。
- RabbitTemplate：生产端消息发送模板。
- SimpleMessageListenerContainer：消费端监听容器。
- MessageListenerAdapter：适配消息监听方法。
- MessageConverter：处理文本、JSON、二进制等消息转换。

## 工程要点

Spring Boot 集成中通常需要配置 publisher-confirm 和 publisher-returns，以配合 [[concepts/概念_RabbitMQ可靠性投递|可靠性投递]]。

## 关联页面

- [[entities/技术_RabbitMQ|RabbitMQ]]
- [[entities/技术_Spring|Spring]]
- [[concepts/概念_RabbitMQ基础模型|RabbitMQ 基础模型]]
- [[sources/来源_RabbitMQ笔记|RabbitMQ 笔记]]

