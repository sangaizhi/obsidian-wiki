---
type: source
tags:
  - source
  - Java
  - 集合框架
summary: "raw/笔记/Java/collection 下关于 List、Set、Map、HashMap、HashSet、SortedMap 与 TreeMap 的集合框架笔记。"
sources:
  - "raw/笔记/Java/collection/README.md"
  - "raw/笔记/Java/collection/List-Set-Map-Compare.md"
  - "raw/笔记/Java/collection/HashMap.md"
  - "raw/笔记/Java/collection/HashSet.md"
  - "raw/笔记/Java/collection/SortedMap.md"
  - "raw/笔记/Java/collection/TreeMap.md"
created: "2026-05-15"
updated: "2026-05-15"
---

# 来源：Java 集合框架笔记

## 来源信息

- 来源目录：`raw/笔记/Java/collection`
- 类型：技术学习笔记
- 覆盖主题：[[concepts/概念_Java集合框架|Java 集合框架]]、[[concepts/概念_HashMap|HashMap]]、[[concepts/概念_有序Map|有序 Map]]

## 核心要点

- List、Set、Map 分别面向有序可重复集合、不可重复集合、键值映射三类数据组织需求。
- [[concepts/概念_HashMap|HashMap]] 基于哈希桶数组组织数据，JDK 1.8 后在链表过长且数组容量足够时可树化为红黑树。
- HashSet 的不重复语义依赖内部维护的 HashMap，元素作为 key，固定对象作为 value。
- SortedMap 和 TreeMap 强调 key 的有序性，排序规则需要与 equals 尽量保持一致。
- TreeMap 基于红黑树实现，支持 NavigableMap 的就近查找能力，但不是线程安全容器。

## 关联页面

- [[entities/技术_Java|Java]]
- [[concepts/概念_Java集合框架|Java 集合框架]]
- [[concepts/概念_HashMap|HashMap]]
- [[concepts/概念_有序Map|有序 Map]]
- [[overview/主题_Java后端技术栈综述|Java 后端技术栈综述]]

