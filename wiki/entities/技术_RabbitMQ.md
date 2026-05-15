---
type: entity
tags:
  - RabbitMQ
  - 消息队列
summary: "RabbitMQ 是基于 AMQP 的消息队列中间件，当前笔记覆盖模型、路由、可靠投递、消费治理和 Spring 集成。"
sources:
  - "raw/笔记/rabbitmq/1、rabbitmq介绍.md"
  - "raw/笔记/rabbitmq/19、RabbitMQ 面试题.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 技术：RabbitMQ

## 基本信息

RabbitMQ 是基于 AMQP 的消息队列中间件，用于异步解耦、削峰填谷、消息路由、可靠投递和事件驱动通信。

## 相关能力

- [[concepts/概念_RabbitMQ基础模型|RabbitMQ 基础模型]]：Exchange、Queue、Binding、Message、VirtualHost、Channel。
- [[concepts/概念_RabbitMQ可靠性投递|RabbitMQ 可靠性投递]]：Confirm、Return、消息落库、备份交换机。
- [[concepts/概念_RabbitMQ消费端治理|RabbitMQ 消费端治理]]：ACK/NACK、限流、TTL、死信队列、幂等消费。
- [[concepts/概念_RabbitMQ与Spring集成|RabbitMQ 与 Spring 集成]]：RabbitAdmin、RabbitTemplate、监听容器、消息转换器。

## 来源

- [[sources/来源_RabbitMQ笔记|RabbitMQ 笔记]]

