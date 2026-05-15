HashSet 是Java 集合的重要组成部分，也是经常使用的一个集合类。实现了 Set 接口。
<!-- more -->
## 一、HashSet 是如何保证不重复的
    HashSet 内部维护了一个 HashMap，在添加元素的时候就是把元素放入维护的map 中，并且key是添加的元素，
    value是常量对象 PRESENT。
    ```
     private static final Object PRESENT = new Object();
     public boolean add(E e) {
        return map.put(e, PRESENT)==null;
     }
    ```
    所以 HashSet 实现不能重复的功能是依赖于 HashMap。  
    若要将对象存放到HashSet中并保证对象不重复，应根据实际情况将对象的hashCode方法和equals方法进行重写。
