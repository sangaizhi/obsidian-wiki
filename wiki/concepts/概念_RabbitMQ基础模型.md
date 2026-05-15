---
type: concept
tags:
  - RabbitMQ
  - AMQP
  - 消息队列
summary: "RabbitMQ 基础模型由 Exchange、Queue、Binding、Message、VirtualHost、Channel 等元素组成，负责消息路由与流转。"
sources:
  - "raw/笔记/rabbitmq/1、rabbitmq介绍.md"
  - "raw/笔记/rabbitmq/2、rabbitMQ整体架构与消息流转.md"
  - "raw/笔记/rabbitmq/6、Exchange 交换机.md"
  - "raw/笔记/rabbitmq/7、Binding-Queue-Message-VirtualHost .md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：RabbitMQ 基础模型

## 定义

RabbitMQ 基础模型描述生产者、交换机、队列、绑定关系、消费者之间的消息流转方式。

## 核心元素

- Producer：消息生产者。
- Exchange：接收消息并根据规则路由到队列。
- Queue：存储待消费消息。
- Binding：交换机与队列之间的路由关系。
- Message：消息本体及属性。
- VirtualHost：逻辑隔离空间。
- Channel：复用 TCP 连接的虚拟通信通道。

## Exchange 类型

- Direct：按 routing key 精确匹配。
- Topic：按通配符模式匹配。
- Fanout：广播到绑定队列。
- Headers：按消息头匹配。

## 关联页面

- [[entities/技术_RabbitMQ|RabbitMQ]]
- [[concepts/概念_RabbitMQ可靠性投递|RabbitMQ 可靠性投递]]
- [[concepts/概念_RabbitMQ消费端治理|RabbitMQ 消费端治理]]
- [[sources/来源_RabbitMQ笔记|RabbitMQ 笔记]]

