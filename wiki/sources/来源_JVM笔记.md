---
type: source
tags:
  - source
  - Java
  - JVM
summary: "raw/笔记/Java/JVM 下关于类加载机制、类加载器、运行时数据区、垃圾回收判断、算法与收集器的 JVM 笔记。"
sources:
  - "raw/笔记/Java/JVM/1.JVM类加载机制.md"
  - "raw/笔记/Java/JVM/2.JVM类加载器.md"
  - "raw/笔记/Java/JVM/3.JVM内存模型.md"
  - "raw/笔记/Java/JVM/gc/1.GC收集器.md"
  - "raw/笔记/Java/JVM/gc/2.确定被回收的内存.md"
  - "raw/笔记/Java/JVM/gc/3.收集算法.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 来源：JVM 笔记

## 来源信息

- 来源目录：`raw/笔记/Java/JVM`
- 类型：技术学习笔记
- 覆盖主题：[[concepts/概念_JVM类加载|JVM 类加载]]、[[concepts/概念_JVM运行时内存|JVM 运行时内存]]、[[concepts/概念_Java垃圾回收|Java 垃圾回收]]

## 核心要点

- 类加载过程包括加载、验证、准备、解析、初始化等阶段。
- 类加载器体系包含启动类加载器、扩展类加载器、应用程序类加载器，并通过双亲委派模型组织加载顺序。
- JVM 运行时数据区包括程序计数器、虚拟机栈、本地方法栈、堆、方法区和运行时常量池。
- 垃圾回收需要先判断对象是否可达，再按不同算法与收集器完成回收。
- GC 算法包括标记-清除、复制、标记-整理、分代收集；收集器包括 Serial、ParNew、Parallel、CMS、G1 等。

## 关联页面

- [[entities/技术_Java|Java]]
- [[concepts/概念_JVM类加载|JVM 类加载]]
- [[concepts/概念_JVM运行时内存|JVM 运行时内存]]
- [[concepts/概念_Java垃圾回收|Java 垃圾回收]]
- [[overview/主题_Java后端技术栈综述|Java 后端技术栈综述]]

