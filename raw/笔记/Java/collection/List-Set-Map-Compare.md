---
title: List、Set和 Map
date: 2018-11-19 14:50:24
categories: Collection
tags:
  - collection
  - List
  - Set
  - Map

---
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;List、Set和 Map 都是 Java中常用的集合类型，各自采用不同的数据结构实现了不同的功能。

<!-- more -->

一、 List
    List 是一个继承了 Collection 接口的集合接口，是一个允许重复的有序（插入和去除的顺序相同）集合。
    1、ArrayList
       ArrayList 是基于动态数组结构实现的。因为地址连续，所以查询操作效率会非常高，同样，因为要移动数据，所以插入和删除数据效率会比较低。
    2、LinkedList
       LinkedList是基于链表结构实现的。所以地址不是连续的。因此，插入和删除数据的效率会非常高（只需要修改前后两个数据节点的头尾指针）;
       相对，查询数据会比较慢（需要移动指针）。
    3、Vector
      Vector实现了一个类似数组一样的表，会自动扩容，是线程安全的（使用 synchronized 同步方法的方式实现），所以 Vector 的性能相对较差。
    4、Stsck
      Stsck 类是从 Vector 派生而来，实现了栈的功能(一种后进先出的结构)
    因此，在需要对数据进行频繁访问的时候，选用 ArrayList；在需要对数据进行频繁插入删除的时候，选中 LinkedList，但如果需要线程安全，
    可以选用 Vector（不推荐，效率确实低）。



二、Set
   Set 也是一个继承了 Collection 接口的集合接口。但 Set 是一个不允许有重复元素的集合。所有的方法都是来自于父类。
   1、HashSet
      HashSet 是基于哈希表实现的，内部维护了一个 HashMap。内部数据是无序的，允许放入 null 值，但仅仅只能放入一个。
      另外，由于基于哈希表实现，要求所有放入的元素都需要实现 hashCode 方法。可以利用 初始容量和负载银子进行初始化调优。
   2、TreeSet
      TreeSet 是基于二叉树实现的，内部的数据都是已经排好序（升序）的，并且不允许放入 null 值。由于树的平衡性，
      所有 TreeSet 没有可以利用的初始化调优参数。另外，由于有序性，所有的元素都必须是可排序的。


三、Map
   Map 不是 Collection 接口的子接口。实现用于维护 键-值 关联的的结构。按定义，该接口实现的是从不重复的键到值得映射。
   键和值都可以为 null，但是，键是唯一的。此外注意，不能把键或值添加给自身。
   1、Map.Entry 接口
      Map.Entry 中的每一个对象都是底层 Map 中一个特定 键-值 对。
   2、HashMap
      基于哈希表数组实现的 Map 结构，线程不安全，为了解决 hash 冲突，内部采用基础数组、链表和红黑树（JDK 1.7 没有红黑树）
      的数据结构存储数据。允许 key 为 null(但是仅能有一个为 null),为了实现 key 的唯一，定义的键的类必须实现 hashCode 和 equals 方法。
   3、HashTable
      所有的 key 必须不为 null,线程安全（使用 synchronized 同步方法实现）

   4、TreeMap
     TreeMap  采用树的结构实现一个键值对映射，对象按照升序排列。
