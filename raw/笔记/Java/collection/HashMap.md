
#### 简介
HashMap 是基于哈希表的 Map 接口实现，用来存放键值对。具有以下特点：    
  + Key 和 Value 都可以为空，但是 Key 为空的键值对只能有一个；
  + 非线程安全，多个线程同时访问同一个 HashMap 时，可能导致数据不一致问题；
  + 元素插入顺序和访问顺序不一致；
  + 遍历时不能执行 `remove()` 操作和 `put()` 操作，会快速失败，抛出 `ConcurrentModificationException`。只能通过 Iterator 本身的 `remove()` 方法实现。

#### 存储结构
在 JDK1.8 之前，HashMap 是通过**数组**和**链表**组成的，数组是主要结构，链表是用来解决Hash冲突的。
在 JDK1.8及之后，HashMap 的组成就多了一个**红黑树**，也就是说 HashMap 可能由**数组**+**链表**或者由**数组**+**红黑树**组成。当发生以下两种情况之一，HashMap 的链表就会转化成红黑树：
  * 链表的长度大于阈值（默认是8）；
  * 数组的长度大于 64；    

##### JDK1.8 之前

##### JDK1.8 及之后
#### 一、HashMap 的存储结构
    HashMap 的存储结构是使用 **数组+链表+红黑树** 实现的。
   ![hashmap 内存结构图](	https://linetime-note-1253360328.cos.ap-guangzhou.myqcloud.com/hashMap%20put%E6%96%B9%E6%B3%95%E6%89%A7%E8%A1%8C%E6%B5%81%E7%A8%8B%E5%9B%BE.png)
    1、HashMap 中有一个 **Node[] table** 字段，就是 哈希桶数组，**Node** 内部类实现了 Map.Entry 接口，本质是一个键值对的映射。上图中的每一个小黑点就是一个 Node 对象。
    2、HashMap 就是使用哈希表来存储数据的。HashMap 为了解决元素冲突，采用数组+链表相结合的方式存储元素。即在每一个数组元素上都挂一个链表。当数据被 Hash 后，可以根据 key 经过 hash 算法（把key 的 hash 值经过高位运算）计算后的结果来得到数据应该存放在数组的哪个位置，然后把数据存放在下标元素对应的链表上。
    具体过程如下：
```
// hash 算法，取 key 的 hash 值
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
(table.length - 1) & hash]   // 确定数组的下标
```
    在定位元素的存储位置时，可能会发生 Hash 碰撞（两个 key 定位到相同的位置）。当然，hash 算法结算结果越分散，
    Hash 碰撞的概率就会越小，map 的存储效率就会越高。另外，如果哈希桶数组越大，碰撞的概率也会相应减少。
    所以，需要在空间和时间上做平衡，根据实际情况确定哈希桶数组的大小，并在此基础上设计 hash 算法来减少碰撞。
    HashMap 通过扩容机制来减少碰撞。

#### 二、put() 方法的介绍
   总体过程如下图：
![image](http://tech.meituan.com/img/java-hashmap/hashMap%20put%E6%96%B9%E6%B3%95%E6%89%A7%E8%A1%8C%E6%B5%81%E7%A8%8B%E5%9B%BE.png)
   1、判断键值对数组是否为空或者长度为0，否则执行 resize() 方法进行扩容，并返回扩容后的大小。    
   2、根据 key 计算 hash值得到带插入的数组索引 i,
   如果键值对数组对应索引位置为空，则直接新建一个键值对节点，放在该位置。    
   3、判断 table[i] 的链表中的第一个元素的 key 和 hash 值是否与 新加的元素一样（通过 hashCode()
   和 equals() 方法），如果一样，直接覆盖原值。     
   4、判断 table[i] 是否为 treeNode, 即 table[i] 是否是红黑树，如果是红黑树，则直接在树中插入键值对。    
   5、遍历 table[i] 的链表，判断链表的长度是否大于 TREEIFY_THRESHOLD(8)，是的话就把链表转成红黑树，
   在红黑树中执行插入操作，否则进行链表的插入操作，在遍历的过程中如果发现 key 已经存在，则直接覆盖value。    
   6、插入成功后，判断实际存在的键值对数量 size 是否超过了最大容量 threshold，如果超过则进行扩容。
```java
     // onlyIfAbsent 重写旧值得条件，当对应key已经存在，并且值不为null的情况是否可以覆盖重写，
     // true 表示不重写，false 表示重写
     final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length; // 1
        if ((p = tab[i = (n - 1) & hash]) == null) // 2
            tab[i] = newNode(hash, key, value, null);
        else {  // 存在
            Node<K,V> e; K k;
            if (p.hash == hash &&  ((k = p.key) == key || (key != null && key.equals(k))))  // 3
                e = p;
            else if (p instanceof TreeNode)  // 4
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            // 覆盖 value
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;  
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold) // 6
            resize();
        afterNodeInsertion(evict);
        return null;
    }
```

#### 三、HashMap 的 扩容机制（resize）
    HashMap 在键值对超出最大容量阈值限制（size * loadFactor），会自动重新计算容量。哈希桶数组扩容方法
    就是使用一个新的大数组代替已有的小数组。
    1、复制旧的哈希桶数组
    2、修改 HashMap 的容量及阈，有以下几种情况
      2.1、如果 HashMap 已经初始化（旧的容量大于0），就判断以下条件：
        2.1.1、如果旧的哈希桶数据大小已经超过 1 << 30 (2^30),则把新的容量阈值(threshold)修改为
        Integer的最大值，这样的话，下次就不会扩容了。
        2.1.2、把新的容量设置为旧容量的两倍，如果旧的容量要大于初始容量并且新的容量小于 2^30 ，
        则会更新新的阈值为就阈值的两倍。（如果执行了该判断（不论结果如何），HaspMap 的容量就会扩大两倍，
        如果判断结果为true,那么阈值也会翻倍）。
      2.2、在旧容量小于等于0并且旧阈值大于0的情况下(旧容量被旧的阈值重置过)，就把旧的阈值设置为新的容量。
      2.3、在其他情况下，就把容量和阈值设置为默认值；
    4、如果旧的阈值大于0（初始容量被阈值重置过），就把旧的阈值大小设置新的容量大小.
    5、复制移动 HashMap 中的元素，把每个bucket都移动到新的buckets中。
      5.1、把 HashMap 中的哈希桶数组设置为一个空的新的数组，后续会把 HashMap 中的元素放到这个新数组中。
      5.2、遍历之前的哈希桶数组，如果遍历的当前位置有数据，才会执行数据移动操作。
        5.2.1、第一种情况，如果当前位置 bucket 中只有一个数据，则直接把这个元素移动新的哈希桶数组对应的位置。
        5.2.2、第二种情况，如果当前位置的 bucket 是一个红黑树。就把这个红黑树进行拆分成两个树或者链表，
        并插入到哈希桶的对应位置。
        5.2.3、第三种情况，当前位置的 bucket是一个链表，则会进行 链表优化重 hash。

**注意：** HashMap 每一次的扩容都是在之前的基础上翻倍，那么在容量扩大之后，移动数据的情况就会有一些特别的地方，
 不在像JDK1.7 那样重新 hash 找位置。JDK1.8 的做法是把元素的hash值与哈希桶数据的长度-1 作‘与’运算，
 判断计算结果为0或者1，为0的情况下，就表示该元素不需要移动位置，反之需。
 就是这一行代码  `if ((e.hash & oldCap) == 0)`  判断是否需要移动位置。

 示例图：
 图1：
 ![hashMap 1.8 哈希算法例图1.png](	https://linetime-note-1253360328.cos.ap-guangzhou.myqcloud.com/hashMap%201.8%20%E5%93%88%E5%B8%8C%E7%AE%97%E6%B3%95%E4%BE%8B%E5%9B%BE1.png)
    元素在重新计算 Hash 后，因为哈希桶的长度 n 变为 2 倍，那个 n-1 的 mask 范围在高位多 1bit，因此新的 index 就会发生这样的变化：
    ![hashMap 1.8 哈希算法例图2](	https://linetime-note-1253360328.cos.ap-guangzhou.myqcloud.com/hashMap%201.8%20%E5%93%88%E5%B8%8C%E7%AE%97%E6%B3%95%E4%BE%8B%E5%9B%BE2.png)
下图为16扩充为32的resize示意图：
![jdk1.8 hashMap扩容例图.png](https://linetime-note-1253360328.cos.ap-guangzhou.myqcloud.com/jdk1.8%20hashMap%E6%89%A9%E5%AE%B9%E4%BE%8B%E5%9B%BE.png)

```java
final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table; // 1
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {  // 2.1
            if (oldCap >= MAXIMUM_CAPACITY) {  // 2.1.1
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY) // 2.1.2
                newThr = oldThr << 1; // 阈值*2
        }
        else if (oldThr > 0) // 2.2  容量被阈值设置过
            newCap = oldThr;
        else {   // 2.3            // 初始化
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        // 5 以下是复制移动元素的过程
        @SuppressWarnings({"rawtypes","unchecked"})
            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;   // 5.1
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {   // 5.2
                Node<K,V> e;
                if ((e = oldTab[j]) != null) { //5.2.1
                    oldTab[j] = null;
                    if (e.next == null)  // 5.2.1.1
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode) // 5.2.1.2
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order  // 5.2.1.3
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;

```

#### 四、HashMap 的线程安全性
    HashMap 是线程不安全的。在多线程的环境下，应尽力避免使用线程不安全的HashMap,
    而去使用线程安全的 ConcurrentHashMap。
    那么 HashMap 为什么不是线程安全的呢？
    HashMap 线程不安全主要还是因为 hash碰撞和扩容导致的。
    1：hash 碰撞
    eg:有两个线程往 HashMap 中添加元素(putVal)时，正好两个线程添加的元素的key的hash值是一样的。
    此时可能就会发生如下情况：
        当线程A执行到 if ((p = tab[i = (n - 1) & hash]) == null) 这行代码并且判断结果为true
        （没有 hash 碰撞），但是还没有执行插入操作；此时CPU把执行资源让给了线程B，正好线程B也执行到该
         if 语句，判断结果也是true(线程A还没有插入)，但是线程B执行插入语句。之后CPU又把资源让给了线程A，
        这时候线程A执行插入就会覆盖线程B插入的数据。
    发生在链表插入数据的情况也是如此。
    2：扩容
    扩容造成线程不安全主要体现在以下两个方面：
    2.1、扩容时添加数据
        添加数据时，正好 HashMap 需要扩容，在扩容的过程中会先把创建一个新的空哈希桶数组。
        如果在把旧数据复制到新的哈希桶数组内之前添加数据，那么就不会检查哈希碰撞的问题，
        新加的数据就直接加入了，但是在之后把旧数据复制新的哈希桶数组时，可能就会覆盖新加的数据。
    2.2、删除数据
        删除数据的时候正好 HashMap 在执行扩容，但是还没有复制数据。
        此时执行删除，会在新的哈希桶数组中删除数据，但是新的哈希桶数组并没有数据，所有删除操作并没有删除
    2.2、扩容时可能会造成死锁
