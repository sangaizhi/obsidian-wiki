---
type: concept
tags:
  - Java
  - 集合框架
summary: "Java 集合框架用 List、Set、Map 三类抽象组织常见容器，分别解决有序、去重和键值映射问题。"
sources:
  - "raw/笔记/Java/collection/List-Set-Map-Compare.md"
  - "raw/笔记/Java/collection/HashSet.md"
  - "raw/笔记/Java/collection/HashMap.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：Java 集合框架

## 定义

Java 集合框架提供一组容器抽象，用于管理一组对象或键值映射。当前笔记重点覆盖 List、Set、Map 三条主线。

## 核心结构

- List：允许重复、有序，典型实现包括 ArrayList、LinkedList、Vector、Stack。
- Set：不允许重复，典型实现包括 HashSet、TreeSet。
- Map：维护 key 到 value 的映射，key 唯一，典型实现包括 [[concepts/概念_HashMap|HashMap]]、Hashtable、[[concepts/概念_有序Map|TreeMap/SortedMap]]。

## 使用判断

- 频繁随机访问优先 ArrayList。
- 频繁插入删除可考虑 LinkedList。
- 需要去重使用 Set，HashSet 依赖 HashMap 判断重复。
- 需要 key 排序或范围视图时使用 SortedMap/TreeMap。

## 关联页面

- [[entities/技术_Java|Java]]
- [[concepts/概念_HashMap|HashMap]]
- [[concepts/概念_有序Map|有序 Map]]
- [[sources/来源_Java集合框架笔记|Java 集合框架笔记]]

