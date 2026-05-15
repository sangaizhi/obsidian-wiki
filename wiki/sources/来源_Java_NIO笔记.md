---
type: source
tags:
  - source
  - Java
  - NIO
summary: "raw/笔记/Java/io 下关于 Java NIO 概述、Selector、Channel 与 Buffer 的基础笔记。"
sources:
  - "raw/笔记/Java/io/NIO基础-1-概述.md"
  - "raw/笔记/Java/io/NIO基础-2-Selector.md"
  - "raw/笔记/Java/io/NIO基础-3-Channel.md"
  - "raw/笔记/Java/io/NIO基础-4-Buffer.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 来源：Java NIO 笔记

## 来源信息

- 来源目录：`raw/笔记/Java/io`
- 类型：技术学习笔记
- 覆盖主题：[[concepts/概念_Java_NIO|Java NIO]]

## 核心要点

- Java NIO 的核心组件是 Buffer、Channel 和 Selector。
- Channel 负责连接数据源和目标，Buffer 承载读写过程中的数据，Selector 负责轮询多个 Channel 的就绪事件。
- Buffer 在写入和读取之间通过 `flip()` 切换模式，通过 `clear()` 或 `compact()` 清理读后状态。
- Selector 需要先注册 Channel 与感兴趣事件，再通过 SelectionKey 获取就绪通道执行 IO。

## 关联页面

- [[entities/技术_Java|Java]]
- [[concepts/概念_Java_NIO|Java NIO]]
- [[overview/主题_Java后端技术栈综述|Java 后端技术栈综述]]

