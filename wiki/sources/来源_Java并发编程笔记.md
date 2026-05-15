---
type: source
tags:
  - source
  - Java
  - 并发
summary: "raw/笔记/Java/concurrent 与 JUC 目录下关于线程、通信、volatile、synchronized、Executor 与线程池的并发编程笔记。"
sources:
  - "raw/笔记/Java/concurrent/basic/1.了解多线程.md"
  - "raw/笔记/Java/concurrent/basic/2.线程间通信.md"
  - "raw/笔记/Java/concurrent/memory_model/4.volatile.md"
  - "raw/笔记/Java/JUC/Volatile.md"
  - "raw/笔记/Java/JUC/syncchronized/Synchronized.md"
  - "raw/笔记/Java/JUC/syncchronized/Synchronized.adoc"
  - "raw/笔记/Java/concurrent/executor/1.Executor框架.md"
  - "raw/笔记/Java/concurrent/executor/2.ThreadPoolExecutor.md"
  - "raw/笔记/Java/concurrent/executor/3.ScheduledThreadPoolExecutor.md"
  - "raw/笔记/Java/concurrent/thread_pool/线程池技术.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 来源：Java 并发编程笔记

## 来源信息

- 来源目录：`raw/笔记/Java/concurrent`、`raw/笔记/Java/JUC`
- 类型：技术学习笔记
- 覆盖主题：[[concepts/概念_Java并发基础|Java 并发基础]]、[[concepts/概念_Java线程通信|Java 线程通信]]、[[concepts/概念_volatile|volatile]]、[[concepts/概念_synchronized|synchronized]]、[[concepts/概念_Executor框架|Executor 框架]]、[[concepts/概念_Java线程池|Java 线程池]]

## 核心要点

- 并发强调同一时间段内处理多个任务，并行强调同一时刻真正同时执行。
- Java 线程生命周期包含 NEW、RUNNABLE、BLOCKED、WAITING、TIMED_WAITING、TERMINATED。
- 线程间通信主要依赖共享内存、volatile 可见性、synchronized 互斥、等待/通知机制和 join。
- [[concepts/概念_volatile|volatile]] 适合保证共享变量可见性，但不能替代复合操作的互斥。
- [[concepts/概念_synchronized|synchronized]] 通过对象监视器保证互斥访问，是 Java 内置同步机制。
- [[concepts/概念_Executor框架|Executor 框架]] 将任务提交与线程执行解耦，ThreadPoolExecutor 是核心实现。

## 关联页面

- [[entities/技术_Java|Java]]
- [[concepts/概念_Java并发基础|Java 并发基础]]
- [[concepts/概念_Java线程池|Java 线程池]]
- [[concepts/概念_Executor框架|Executor 框架]]
- [[overview/主题_Java后端技术栈综述|Java 后端技术栈综述]]

