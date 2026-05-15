---
type: concept
tags:
  - RabbitMQ
  - 可靠性
  - 消息投递
summary: "RabbitMQ 可靠性投递通过消息落库、Confirm、Return、备份交换机和队列持久化等机制降低消息丢失风险。"
sources:
  - "raw/笔记/rabbitmq/8、如何保证消息100%投递成功 .md"
  - "raw/笔记/rabbitmq/10、Confirm 确认消息.md"
  - "raw/笔记/rabbitmq/11、Return 消息机制.md"
  - "raw/笔记/rabbitmq/19、RabbitMQ 面试题.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：RabbitMQ 可靠性投递

## 定义

RabbitMQ 可靠性投递关注消息从生产者发送到 Broker、从交换机路由到队列、在队列中存储、再投递给消费者的全链路可靠性。

## 生产端机制

- 消息落库并记录状态。
- Confirm 确认消息是否到达 Broker。
- Return 处理不可路由消息。
- 延迟投递与二次确认用于补偿回查。

## Broker 与路由机制

- 队列、交换机和消息持久化降低 Broker 重启导致的丢失。
- mandatory + Return 或 alternate-exchange 处理无法路由消息。

## 关联页面

- [[concepts/概念_RabbitMQ基础模型|RabbitMQ 基础模型]]
- [[concepts/概念_RabbitMQ消费端治理|RabbitMQ 消费端治理]]
- [[concepts/概念_RabbitMQ与Spring集成|RabbitMQ 与 Spring 集成]]
- [[sources/来源_RabbitMQ笔记|RabbitMQ 笔记]]

