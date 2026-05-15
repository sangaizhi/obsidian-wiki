---
type: source
tags:
  - source
  - Kafka
  - 消息队列
summary: "raw/笔记/kafka/Kafka 面试题.md 中关于 Kafka Topic、Partition、Broker、副本、索引、offset 和消费组的问答笔记。"
sources:
  - "raw/笔记/kafka/Kafka 面试题.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 来源：Kafka 面试题

## 来源信息

- 来源文件：`raw/笔记/kafka/Kafka 面试题.md`
- 类型：面试题式技术笔记
- 覆盖主题：[[concepts/概念_Kafka基础与高可用|Kafka 基础与高可用]]

## 核心要点

- Kafka 通过 Topic 组织消息主题，通过 Partition 提升并行度与吞吐。
- Producer 可根据 key、轮询或自定义策略选择分区，`acks` 参数影响发送可靠性。
- Segment、log、index、timeindex 共同组成磁盘存储与检索结构，稀疏索引减少索引体积。
- Consumer Group 负责消费扩展与分区分配，offset 记录消费进度。
- 副本机制通过 leader/follower、ISR、HW、LEO 等概念支撑高可用与数据一致性。

## 关联页面

- [[entities/技术_Kafka|Kafka]]
- [[concepts/概念_Kafka基础与高可用|Kafka 基础与高可用]]
- [[overview/主题_Java后端技术栈综述|Java 后端技术栈综述]]

