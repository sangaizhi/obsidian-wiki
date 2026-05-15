---
type: concept
tags:
  - Java
  - JMM
  - volatile
summary: "volatile 是 Java 提供的轻量级同步机制，主要用于保证共享变量可见性和一定的有序性。"
sources:
  - "raw/笔记/Java/concurrent/memory_model/4.volatile.md"
  - "raw/笔记/Java/JUC/Volatile.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：volatile

## 定义

volatile 是 Java 提供的一种轻量级同步机制，用于确保共享变量的更新能被其他线程及时看到。

## 核心作用

- 保证可见性：写入 volatile 变量会刷新到主内存，读取 volatile 变量会从主内存获取最新值。
- 限制重排序：通过内存屏障约束编译器和处理器重排序。
- 不保证复合操作原子性：例如 `i++` 仍然需要锁或原子类。

## 使用场景

- 状态标记位。
- 单写多读的轻量同步。
- 与其他同步机制配合做线程协作。

## 关联页面

- [[concepts/概念_Java并发基础|Java 并发基础]]
- [[concepts/概念_Java线程通信|Java 线程通信]]
- [[concepts/概念_synchronized|synchronized]]
- [[sources/来源_Java并发编程笔记|Java 并发编程笔记]]

