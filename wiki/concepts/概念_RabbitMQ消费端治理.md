---
type: concept
tags:
  - RabbitMQ
  - 消费端
  - 消息治理
summary: "RabbitMQ 消费端治理围绕 ACK/NACK、重回队列、限流、TTL、死信队列和幂等消费控制消息处理质量。"
sources:
  - "raw/笔记/rabbitmq/9、幂等性及主流解决方案.md"
  - "raw/笔记/rabbitmq/13、消费端的限流.md"
  - "raw/笔记/rabbitmq/14、消费端ACK与重回队列.md"
  - "raw/笔记/rabbitmq/15、TTL队列与消息.md"
  - "raw/笔记/rabbitmq/16、死信队列.md"
  - "raw/笔记/rabbitmq/19、RabbitMQ 面试题.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：RabbitMQ 消费端治理

## 定义

RabbitMQ 消费端治理是消费者侧处理吞吐、失败、重试、过期、死信和重复消费的机制集合。

## 关键机制

- 手工 ACK/NACK：消费者主动确认或拒绝消息。
- 重回队列：处理失败后让消息重新进入队列，但需要防止无限重试。
- 消费端限流：通过 prefetch 等方式控制未确认消息数量。
- TTL：设置消息或队列过期时间。
- 死信队列：接收被拒绝、过期或超过队列长度限制的消息。
- 幂等消费：通过唯一 ID + 指纹码或 Redis 原子操作避免重复处理造成副作用。

## 关联页面

- [[concepts/概念_RabbitMQ基础模型|RabbitMQ 基础模型]]
- [[concepts/概念_RabbitMQ可靠性投递|RabbitMQ 可靠性投递]]
- [[sources/来源_RabbitMQ笔记|RabbitMQ 笔记]]

