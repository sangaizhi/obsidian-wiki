---
type: concept
tags:
  - Java
  - 并发
  - 线程池
summary: "Java 线程池通过复用线程、任务队列和饱和策略降低线程创建销毁成本并提升系统吞吐。"
sources:
  - "raw/笔记/Java/concurrent/thread_pool/线程池技术.md"
  - "raw/笔记/Java/concurrent/executor/2.ThreadPoolExecutor.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：Java 线程池

## 定义

线程池是管理和复用工作线程的组件，用于降低线程反复创建销毁的成本，并通过队列和参数控制并发压力。

## 执行流程

1. 如果核心线程未满，创建工作线程执行任务。
2. 如果核心线程已满，尝试将任务放入工作队列。
3. 如果队列已满，尝试创建非核心线程。
4. 如果线程数达到上限，执行饱和策略。

## 关键配置

- corePoolSize：核心线程数。
- maximumPoolSize：最大线程数。
- workQueue：任务队列。
- keepAliveTime：空闲线程存活时间。
- RejectedExecutionHandler：拒绝策略。

## 常见实现

- FixedThreadPool：固定线程数，通常配合无界队列。
- CachedThreadPool：按需创建线程，适合短任务。
- SingleThreadExecutor：单线程串行执行。
- ScheduledThreadPoolExecutor：定时和周期任务。

## 关联页面

- [[concepts/概念_Executor框架|Executor 框架]]
- [[concepts/概念_Java并发基础|Java 并发基础]]
- [[sources/来源_Java并发编程笔记|Java 并发编程笔记]]

