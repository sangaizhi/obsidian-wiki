---
type: concept
tags:
  - Kafka
  - 消息队列
  - 高可用
summary: "Kafka 基础与高可用围绕 Topic、Partition、Broker、Segment、索引、Consumer Group、offset 和副本同步机制展开。"
sources:
  - "raw/笔记/kafka/Kafka 面试题.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：Kafka 基础与高可用

## 定义

Kafka 是高吞吐分布式消息与日志系统。当前笔记以面试题形式梳理了 Kafka 的主题、分区、存储、消费和副本机制。

## 核心模型

- Topic：消息主题。
- Partition：主题的分区，决定并行度和消息顺序边界。
- Broker：Kafka 服务节点。
- Consumer Group：消费者组，一个分区同一时刻通常由组内一个消费者消费。
- Segment：日志文件分段，配套 log、index、timeindex 文件。

## 可靠性与高可用

- Producer 的 `acks` 参数影响发送确认强度。
- 副本分为 leader 和 follower，由 leader 处理读写，follower 同步数据。
- ISR 表示与 leader 保持同步的副本集合。
- HW 与 LEO 用于描述可消费位置和日志末尾位置。

## 关联页面

- [[entities/技术_Kafka|Kafka]]
- [[concepts/概念_RabbitMQ基础模型|RabbitMQ 基础模型]]
- [[sources/来源_Kafka面试题|Kafka 面试题]]
- [[overview/主题_Java后端技术栈综述|Java 后端技术栈综述]]

