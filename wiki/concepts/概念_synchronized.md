---
type: concept
tags:
  - Java
  - 并发
  - synchronized
summary: "synchronized 是 Java 内置互斥同步机制，通过对象监视器保护共享数据的临界区访问。"
sources:
  - "raw/笔记/Java/JUC/syncchronized/Synchronized.md"
  - "raw/笔记/Java/JUC/syncchronized/Synchronized.adoc"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：synchronized

## 定义

synchronized 是 Java 语言内置的同步关键字，用于在多线程访问共享数据时建立互斥访问边界。

## 应用方式

- 修饰实例方法：锁当前对象。
- 修饰静态方法：锁当前 Class 对象。
- 修饰代码块：锁指定对象。

## 实现理解

原始笔记将 synchronized 的使用动机概括为两个条件：存在共享数据，且多线程会操作共享数据。对象锁、偏向锁、轻量级锁、重量级锁等机制共同支撑其运行时表现。

## 关联页面

- [[concepts/概念_Java并发基础|Java 并发基础]]
- [[concepts/概念_Java线程通信|Java 线程通信]]
- [[concepts/概念_volatile|volatile]]
- [[sources/来源_Java并发编程笔记|Java 并发编程笔记]]

