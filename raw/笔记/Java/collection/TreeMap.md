        
        TreeMap 是基于 NavigableMap 实现的红黑树 map 结构。该 map 中的数据根据数据元素的 key 的自然顺序或创建时提供的Comparator 进行排序。

#### 1、定义
    TreeMap 实现了 NavigableMap、Cloneable、Serializable接口。继承了 AbstractMap.
    继承 AbstractMap 表示 TreeMap 是一个支持映射表的 map。
    实现 NavigableMap 接口表示它支持一系列的导航方法，有针对给定搜索条件返回最接近匹配的方法。
#### 2、有序性
        TreeMap 是一个有序的 map,针对内部元素的排序，有两种方式，第一种方式，也是默认的方式，根据元素 key 的自然顺序进行
    排序。第二种方式是根据我们在创建 Map的时候指定的 Comparator。
        注意，如果要正确的实现 Map 接口， 则有序映射所保持的顺序都必须与 equals 一致，关于这一点，在 Comparator 或者
    Comparable 中有说明（强烈建议但不要求 x.compareTo(y) == 0 与 x.equals(y) 的结果相等）。为什么要求这一点，是
    因为在 Map 中 比较两个键是否相等是根据 equals 方法比较的。
#### 3、非线程安全
        TreeMap 中的方法不是同步的，所以TreeMap实现的数据结构不是线程安全，存在并发问题。因此，如果有多个线程访问同一个
    TreeMap，并且至少有一个线程修改该 Map 的结构（是指添加或者删除一个或多个映射的操作，仅改变与现有键的关联值都不是结构上
    的修改），那么我们必须在外部进行同步。对于外部同步，我们一般有两种办法：
        ①：对拥有该 Map 的对象的执行同步操作。
        ②：使用 Collections.synchronizedSortedMap(NavigableMap<K,V> m) 方法来包装该 map。当然，这个操作最好是
    在创建时就完成。
#### 4、put 方法
       put 方法主要分为以下几步：
       ①：类型和 null 值检查
       ②：寻找父节点（以根节点为初始节点，使用比较器（默认的或者指定的）比较插入节点与初始节点的 key, 以找到最合适插入节点
          的父节点）
       ③：插入节点
       ④：调整(修复、平衡)红黑树


```java
public V put(K key, V value) {
        Entry<K,V> t = root;
        if (t == null) {
            // ①：类型和 null 值检查
            compare(key, key); // type (and possibly null) check
            // 使用 key 和 value 创建新节点，并作为根节点插入
            root = new Entry<>(key, value, null);
            // 更新容器大小
            size = 1;
            // 更新修改次数
            modCount++;
            return null;
        }
        // ②：寻找父节点
        // 当前排序返回的结果
        int cmp;
        Entry<K,V> parent;
        // 创建 TreeMap 时指定的比较器
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            // 采用指定的比较器进行排序
            do {  
                // 指向上一次循环后的节点
                parent = t;
                // 比较当前节点的 key 与新增节点 key 的大小
                cmp = cpr.compare(key, t.key);
                if (cmp < 0)
                    // 如果新增节点的 key 比较当前节点的 key 小，则把当前节点的左子节点作为新的当前节点
                    t = t.left;
                else if (cmp > 0)
                    // 如果新增节点的 key 比较当前节点的 key 大，则把当前节点的左右子节点作为新的当前节点
                    t = t.right;
                else
                  // 如果当前节点的 key 与新增节点的 key 相等，则替换当前节点的 value
                    return t.setValue(value);
            } while (t != null);
        }
        else {
            // 没有指定比较器，采用默认的比较器，具体的实现上面的使用指定比较器的方式相同
            if (key == null)
                throw new NullPointerException();
            @SuppressWarnings("unchecked")
                Comparable<? super K> k = (Comparable<? super K>) key;
            do {
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        // ③：插入节点
        Entry<K,V> e = new Entry<>(key, value, parent);
        if (cmp < 0)
            // 如果插入节点的 key 比找到的父节点的 key 要小，则把插入节点作为找到的父节点的左节点进行插入
            parent.left = e;
        else
            // 如果插入节点的 key 比找到的父节点的 key 要大，则把插入节点作为找到的父节点的右节点进行插入
            parent.right = e;
        // ④： 调整（平衡、修复）红黑树
        fixAfterInsertion(e);
        size++;
        modCount++;
        return null;
    }

```
#### 5、fixAfterInsertion 方法
       由于 TreeMap 的存储结构是基于红黑树的。由于红黑树的特点（平衡），所以在插入某个元素节点后，都需要将TreeMap
    的内部结构进行调整，以满足红黑树的特点。而 fixAfterInsertion 这个方法就是用来平衡、修复红黑树。
    以下就是 fixAfterInsertion 方法在 JDK 1.8 中源码：
```java
private void fixAfterInsertion(Entry<K,V> x) {
    // 设置新增节点的颜色为红色
    x.color = RED;
    // 循环到的节点 x 不等于空，不是根节点并且父节点的颜色是红色,父节点是黑色表示不需要修复
    while (x != null && x != root && x.parent.color == RED) {
        // 新增节点的父节点是作为左子节点
        if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
            Entry<K,V> y = rightOf(parentOf(parentOf(x))); // 新增节点的叔叔节点
            if (colorOf(y) == RED) { // 如果叔叔节点是红色的
                setColor(parentOf(x), BLACK); // 把父节点设置成黑色
                setColor(y, BLACK); // 把叔叔节点设置成黑色
                setColor(parentOf(parentOf(x)), RED); // 把爷爷节点设置成红色
                x = parentOf(parentOf(x)); // 将爷爷节点作为新增的节点,相当于继续调整上面的节点
            } else {
              // 叔叔节点是黑色
                if (x == rightOf(parentOf(x))) { // 新增的节点是其父节点的右子节点
                    x = parentOf(x); // 当前节点设置成其父节点
                    rotateLeft(x); // 左旋当前节点
                }
                setColor(parentOf(x), BLACK); // 设置当前节点的父节点的颜色黑色
                setColor(parentOf(parentOf(x)), RED); // 设置爷爷节点为红色
                rotateRight(parentOf(parentOf(x))); // 右旋爷爷节点
            }
        } else {
            Entry<K,V> y = leftOf(parentOf(parentOf(x)));
            if (colorOf(y) == RED) {
                setColor(parentOf(x), BLACK);
                setColor(y, BLACK);
                setColor(parentOf(parentOf(x)), RED);
                x = parentOf(parentOf(x));
            } else {
                if (x == leftOf(parentOf(x))) {
                    x = parentOf(x);
                    rotateRight(x);
                }
                setColor(parentOf(x), BLACK);
                setColor(parentOf(parentOf(x)), RED);
                rotateLeft(parentOf(parentOf(x)));
            }
        }
    }
    root.color = BLACK;
}
```
    左旋示意图（来源于网络）：
![左旋示意图](https://linetime-note-1253360328.cos.ap-guangzhou.myqcloud.com/rotate_left.gif)   
    右旋示意图（来源于网络）:
![右旋示意图](https://linetime-note-1253360328.cos.ap-guangzhou.myqcloud.com/rotate_right.gif)
