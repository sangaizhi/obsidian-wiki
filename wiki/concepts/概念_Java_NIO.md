---
type: concept
tags:
  - Java
  - NIO
  - IO
summary: "Java NIO 以 Buffer、Channel、Selector 为核心，支持面向缓冲区和多路复用的非阻塞 IO 编程模型。"
sources:
  - "raw/笔记/Java/io/NIO基础-1-概述.md"
  - "raw/笔记/Java/io/NIO基础-2-Selector.md"
  - "raw/笔记/Java/io/NIO基础-3-Channel.md"
  - "raw/笔记/Java/io/NIO基础-4-Buffer.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：Java NIO

## 定义

Java NIO 是 Java 的新 IO 模型，围绕 Buffer、Channel、Selector 组织数据读写和多路复用。

## 核心组件

- Buffer：数据缓冲区，包含 capacity、position、limit 等状态。
- Channel：连接文件、Socket 等数据源与目标。
- Selector：管理多个 Channel 的就绪事件，实现单线程轮询多个连接。

## 基本流程

Buffer 使用时通常先写入数据，调用 `flip()` 切换到读模式，读取后通过 `clear()` 或 `compact()` 清理状态。Channel 注册到 Selector 后，由 Selector 轮询就绪事件并交给后续 IO 逻辑处理。

## 关联页面

- [[entities/技术_Java|Java]]
- [[concepts/概念_Java并发基础|Java 并发基础]]
- [[sources/来源_Java_NIO笔记|Java NIO 笔记]]

