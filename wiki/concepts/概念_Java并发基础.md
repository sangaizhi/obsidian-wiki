---
type: concept
tags:
  - Java
  - 并发
  - 线程
summary: "Java 并发基础围绕线程生命周期、并发与并行、线程优先级、启动终止和共享状态治理展开。"
sources:
  - "raw/笔记/Java/concurrent/basic/1.了解多线程.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：Java 并发基础

## 定义

Java 并发基础关注如何用线程在同一时间段内处理多个任务，并在共享内存模型下管理线程生命周期、状态转换和协作。

## 核心内容

- 并发：多个事件在同一时间间隔发生。
- 并行：多个事件在同一时刻发生。
- 线程是操作系统调度的基本单元，一个进程可包含多个线程。
- Java 线程状态包括 NEW、RUNNABLE、BLOCKED、WAITING、TIMED_WAITING、TERMINATED。
- 创建线程可通过继承 Thread、实现 Runnable、使用 Callable/Future 等方式。

## 使用场景

- 利用多核处理器缩短任务执行时间。
- 将耗时或非强一致性操作拆分到后台执行，提升响应速度。
- 为 [[concepts/概念_Java线程池|线程池]]、[[concepts/概念_Java线程通信|线程通信]] 和同步机制提供基础。

## 关联页面

- [[concepts/概念_Java线程通信|Java 线程通信]]
- [[concepts/概念_Java线程池|Java 线程池]]
- [[concepts/概念_volatile|volatile]]
- [[concepts/概念_synchronized|synchronized]]
- [[sources/来源_Java并发编程笔记|Java 并发编程笔记]]

