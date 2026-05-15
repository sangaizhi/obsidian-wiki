---
type: concept
tags:
  - Java
  - 并发
  - 线程通信
summary: "Java 线程通信通过共享变量、volatile、synchronized、等待通知机制和 join 协调多个线程的执行关系。"
sources:
  - "raw/笔记/Java/concurrent/basic/2.线程间通信.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：Java 线程通信

## 定义

Java 线程通信是多个线程之间同步状态、传递信号和协调执行顺序的机制集合。

## 主要方式

- [[concepts/概念_volatile|volatile]]：保证共享变量修改对其他线程可见。
- [[concepts/概念_synchronized|synchronized]]：保证同一时刻只有一个线程进入临界区。
- 等待/通知机制：线程调用 wait 进入等待，由其他线程 notify 或 notifyAll 唤醒。
- Thread.join：当前线程等待目标线程执行结束。

## 使用注意

线程通信处理的是协作关系，同步处理的是互斥关系。实际代码中通常需要同时考虑可见性、原子性和有序性。

## 关联页面

- [[concepts/概念_Java并发基础|Java 并发基础]]
- [[concepts/概念_volatile|volatile]]
- [[concepts/概念_synchronized|synchronized]]
- [[sources/来源_Java并发编程笔记|Java 并发编程笔记]]

