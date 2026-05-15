### 介绍
Selector，顾名思义称之为选择器，是Java NIO核心组件之一，用来轮询一个或多个 Channel 的状态（是否处于可读、可写）。是 NIO 实现非阻
塞的关键。因此，NIO 只需要一个线程管理 Selector 就可以实现管理多个 Channel，也就是说可以管理多个网络连接。所以，Selector 也被称
之为多路复用器。

### Selector 管理多个 Channel 的机制
Selector 管理多个 Channel 的方式就是依靠轮询管理的Channel，以下是具体步骤：
1. 首先，Selector 轮询的是 Channel，所以需要把 Channel 注册到 Selector 上，并且声明需要 Selector 监听的事件。如此 Selector 才
知道哪些 Channel 是需要管理的。
一共有四种事件可以监听：
> 1. connect 事件：客户端连接服务端的事件，对应的SelectionKey 为 SelectionKey.OP_CONNECT(8)；
> 2. accept 事件：服务端接收客户端连接事件，对应的SelectionKey 为 SelectionKey.OP_ACCEPT(16)；
> 3. read 读事件：对应的SelectionKey 为 SelectionKey.OP_READ(1)；
> 4. write 事件：写事件，对应的SelectionKey 为 SelectionKey.OP_WRITE(4)；
2. 其后，Selector 这个线程会不断的轮询注册在其上的 Channel，一旦发现某个 Channel 上发生了读或者写事件，这个 Channel 就会处于被Selector 轮询出来进入就绪状态，然后通过注册时 SelectionKey 可以获取就绪的 Channel，进行后续的 IO 操作。

### Channel 注册到 Selector 中
Selector 需要管理 Channel，所以 Channel 需要注册到 Selector 上。
```java
channel.configureBlocking(false); 
SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
```
* 需要注意的是，如果一个 Channel 需要注册到 Selector 上，那么该 Channel 必须是非阻塞的，所以才有了`channel.configureBlocking(false); ` 这行代码，也因此，FileChannel 是不能注册到 Selector 上的， 因为它是阻塞的。
* channel 注册方法的第二个参数表示的是一个“interest 集合”，就是告诉 Selector，它应该对当前 Channel 的这些事件感兴趣，需要进行监
听。 多个事件时可以通过 `|` 运算符来组合多个事件。


