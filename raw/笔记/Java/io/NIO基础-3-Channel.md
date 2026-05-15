### 介绍
在 NIO 中，基本上所有的 IO 操作都是从 Channel 开始的，数据可以从 Channel 读到 Buffer 中，也可以从 Buffer 中写到 Channel 中。所以在 NIO 中，Channel 必须配置者 Buffer 使用。
### Channel 的实现
Channel 在 Java 中，是作为一个接口的，仅仅是定义 IO 操作的连接与关闭。
```java
public interface Channel extends Closeable {
    /**
     * 判断当前的 Channel 是否处于打开状态
     */
    public boolean isOpen();
    /**
     * 关闭 Channel
     */
    public void close() throws IOException;
}
```
所以 Channel 有许多的实现类，最重要的就是如下四个
* SocketChannel：一个客户端用来发起 TCP 的 channel;
* ServerSocketChannel: 可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
* DatagramChannel ：通过 UDP 读写数据。
* FileChannel ：从文件中，读写数据。