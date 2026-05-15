# Synchronized

## 一、为什么使用Synchronized
        在并发编程中存在线程安全问题，主要原因有：1：存在共享数据；2：多线程操作共享数据。   
        关键字 Synchronized 可以保证在同一时刻，只有一个线程可以执行某个方法或者某个代码块，同事 Synchronized 可以包含保证一个线程的变化可见，即可以代替 Volatile。

## 二、应用方式
        Java中每一个对象都可以作为锁，这是 Synchronized 实现同步的基础。
        1. 普通同步方法(实例方法)，锁是当前实例对象，进入同步代码钱需要获取当前实例的锁；
        2. 静态同步方法，锁是当前类的 class 对象，进入同步代码前需要获得当前类对象的锁；
        3. 同步方法块，锁是括号里面的独享，对给定对象加锁，进入同步代码块前需要获取给定对象的锁。   


## 三、实现原理
        从 JVM 规范中可以看到 Synchronized 的实现原理：JVM 基于进入和退出 Monitor 对象来实现方法和代码块的同步，但两者的实现细节不一样。
        monitorenter 指令是在编译后插入到同步代码块的开始位置，而 monitorexit 指令是插入到方法结束处和异常处，JVM 要保证每一个 monitorenter 必须有对应的 monitorexit 与之配对。任何对象都有一个 monitor 与之关联，当且一个 monitor被持有后，它将处于锁定状态。线程执行到 monitorenter指令时，将会尝试获得对象所对应的monitor 的所有权，即尝试获取对象的锁。

## 四、对象的锁
        synchronized 实现同步的关键就是锁，二对象的锁就是存在 Java 对象头里的。如果对象是数组类型，则虚拟机用 3 个字宽存储对象头；如果对象是非数组类型，则用 2 字宽存储对象头。在 32 位虚拟机中，1字宽 = 4字节 = 32 bit。

<center><bold>Java 对象头的长度</bold></center>

|长度|内容|说明|
|:----:|:-----:|:-----:|
|32/64 bit|Mark Word|存储对象的 hashCode 或锁信息等|
|32/64 bit| Class Metadata address| 存储到对象类型数据的指针|
|32/64 bit| Array length| 数组的长度（如果当前对象是数组）    
    
    
Java 对象头里的 Mark Word 默认存储对象的 hashCode、分代年龄和锁标记位。32位 JVM 的 Mark Word 的默认存储结构如下：
<center><bold>32位 JVM 的 Mark Word 的默认存储结构</bold></center>    

|锁状态|25 bit| 4 bit| 1 bit 是否是偏向锁 | 2 bit 锁标志位|
|:---:|:----:|:----:|:------------------:|:------------:|
|无锁状态|对象的hashCode|对象的分代年龄|0|01|    

在运行期间， Mark Word 里存储的数据会随着锁标志位的变化而变化。 Mark Word 可能变化为存储以下 4 中数据：
<center><bold>32位 JVM 的 Mark Word 的状态变化</bold></center>   

