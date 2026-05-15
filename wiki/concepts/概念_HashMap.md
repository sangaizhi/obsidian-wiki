---
type: concept
tags:
  - Java
  - HashMap
  - 集合框架
summary: "HashMap 是基于哈希桶数组的 Map 实现，通过数组、链表和红黑树处理键值映射与哈希冲突。"
sources:
  - "raw/笔记/Java/collection/HashMap.md"
  - "raw/笔记/Java/collection/HashSet.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：HashMap

## 定义

HashMap 是 Map 接口的哈希表实现，用于存储 key-value 映射。它允许一个 null key 和多个 null value，不保证遍历顺序，且不是线程安全容器。

## 存储结构

- JDK 1.8 之前：数组 + 链表。
- JDK 1.8 及之后：数组 + 链表 + 红黑树。
- hash 定位通过扰动后的 hash 值与数组长度计算桶下标。
- 当链表长度超过阈值且数组容量达到条件时，链表可树化以降低冲突后的查找成本。

## 关键流程

- put 时先初始化或扩容 table，再定位桶位置。
- 桶为空则直接插入 Node。
- key 已存在则覆盖 value。
- 桶为红黑树则走树节点插入，否则遍历链表并在必要时树化。
- size 超过 threshold 时触发 resize。

## 边界

HashMap 在并发写入时可能出现数据不一致，应在并发场景使用外部同步或并发容器。

## 关联页面

- [[concepts/概念_Java集合框架|Java 集合框架]]
- [[concepts/概念_有序Map|有序 Map]]
- [[sources/来源_Java集合框架笔记|Java 集合框架笔记]]

