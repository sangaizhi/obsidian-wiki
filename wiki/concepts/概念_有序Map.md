---
type: concept
tags:
  - Java
  - TreeMap
  - SortedMap
summary: "有序 Map 通过 key 的自然顺序或 Comparator 排序，TreeMap 基于红黑树提供有序映射和导航查找能力。"
sources:
  - "raw/笔记/Java/collection/SortedMap.md"
  - "raw/笔记/Java/collection/TreeMap.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：有序 Map

## 定义

有序 Map 指按照 key 的自然顺序或创建时提供的 Comparator 对映射项排序的 Map。SortedMap 定义有序映射能力，TreeMap 是常见实现。

## 核心机制

- key 必须可比较，或者创建 Map 时提供 Comparator。
- 排序规则应尽量与 equals 保持一致，否则可能出现 Map 语义上的异常直觉。
- SortedMap 支持 subMap、headMap、tailMap 等范围视图，视图更新会影响原映射。
- TreeMap 基于红黑树实现，支持 NavigableMap 的就近查找方法。

## 边界

TreeMap 不是线程安全容器。多线程并发修改时需要外部同步，或用 `Collections.synchronizedSortedMap` 包装。

## 关联页面

- [[concepts/概念_Java集合框架|Java 集合框架]]
- [[concepts/概念_HashMap|HashMap]]
- [[sources/来源_Java集合框架笔记|Java 集合框架笔记]]

