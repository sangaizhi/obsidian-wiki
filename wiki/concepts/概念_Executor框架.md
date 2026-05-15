---
type: concept
tags:
  - Java
  - Executor
  - 并发
summary: "Executor 框架把任务提交与线程调度解耦，是 Java 并发任务执行与线程池体系的顶层抽象。"
sources:
  - "raw/笔记/Java/concurrent/executor/1.Executor框架.md"
  - "raw/笔记/Java/concurrent/executor/2.ThreadPoolExecutor.md"
  - "raw/笔记/Java/concurrent/executor/3.ScheduledThreadPoolExecutor.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：Executor 框架

## 定义

Executor 框架是 Java 并发包中用于执行异步任务的框架，把任务提交方与具体线程调度策略解耦。

## 核心组成

- Executor：最基础的任务执行接口。
- ExecutorService：增加生命周期管理和提交 Callable/Future 的能力。
- ThreadPoolExecutor：通用线程池实现。
- ScheduledThreadPoolExecutor：支持延迟与周期任务。
- Future/FutureTask：表示异步任务结果。

## 运行理解

Executor 框架形成两级调度模型：应用把任务交给 Executor，Executor 再将任务映射到实际线程执行。这样业务代码不直接管理线程生命周期。

## 关联页面

- [[concepts/概念_Java线程池|Java 线程池]]
- [[concepts/概念_Java并发基础|Java 并发基础]]
- [[sources/来源_Java并发编程笔记|Java 并发编程笔记]]

