---
type: concept
tags:
  - Java
  - JVM
  - GC
summary: "Java 垃圾回收通过可达性判断、回收算法和垃圾收集器组合管理堆和方法区中的无用对象。"
sources:
  - "raw/笔记/Java/JVM/gc/1.GC收集器.md"
  - "raw/笔记/Java/JVM/gc/2.确定被回收的内存.md"
  - "raw/笔记/Java/JVM/gc/3.收集算法.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：Java 垃圾回收

## 定义

Java 垃圾回收负责识别和回收不再被程序使用的对象，降低手动内存管理成本。

## 回收判断

- 引用计数法：通过引用数量判断对象是否可回收，但难以处理循环引用。
- 可达性分析：从 GC Roots 出发，无法到达的对象可被视为回收候选。
- 方法区回收关注废弃常量和无用类。

## 回收算法

- 标记-清除：先标记后清除，可能产生碎片。
- 复制算法：将存活对象复制到另一块区域。
- 标记-整理：标记后移动对象整理空间。
- 分代收集：根据对象生命周期分代选择算法。

## 收集器

原始笔记覆盖 Serial、ParNew、Parallel Scavenge、Serial Old、Parallel Old、CMS、G1 等收集器。

## 关联页面

- [[concepts/概念_JVM类加载|JVM 类加载]]
- [[concepts/概念_JVM运行时内存|JVM 运行时内存]]
- [[sources/来源_JVM笔记|JVM 笔记]]

