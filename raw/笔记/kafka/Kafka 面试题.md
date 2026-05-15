#### 1、kafka 为什么要安装依赖 Zookeeper?

​	因为 kafka 利用 ZK 的有序节点、临时节点和监听机制，ZK帮 kafka 做了这些事情：

配置中心(管理 Broker、Topic、Partition、Consumer 的信息，包括元数据的变动)、

负载均衡、命名服务、分布式通知、集群管理和选举、分布式锁。

#### 2、pull 和 push,Kafka支持哪种消费模型，为什么？

​	Kafka 只支持 pull 模式。因为在 push  模式下，如果消息的生产速度远远大于消费者消费消息的速率，那么消费者就会不堪重负，直接挂掉。在 Kafka 中，通过参数(`max.poll.records`) 控制消费者一次到底取多少条消息，消费者在创建consumer时指定。

#### 3、Topic 的用途是什么？为什么 Kafka没有队列？

​	Topic 的用途是用来存储很传输消息， 是一组消息的集合。生产者和消费者就是通过 topic 关联起来。也就是说生产者发送消息到指定的topic,消费者从指定的topic消费消息。

​	Topic 在 Kafka 里面担任的就是队列的角色。

#### 4、分区 Partition 的作用是什么？

​	如果一个 topic 的消息太多，就会带来两个问题：

​          1. 不方便横向扩展，比如我想要在集群中把数据分布在不同的机器上实现扩展，如果一个 Topic 的消息无法在物理上拆分到多台机器的时候，这个是做不到的。

​		  2. 并发和负载的问题，所有的客户端操作的都是同一个 Topic，在高并发的场景下性能会大大下降。

​	针对这两个问题，Kafka 引入了分区的概念，把一个 Topic 划分为多个分区，每一个 Topic 至少有一个分区。分区在思想上有点类似于分库分表，实现的是**横向扩展和负载**的目的。

​	举例：Topic 有三个分区，生产者一次发送9条消息(对消息进行了编号)。最终消息的存储结果：第一个分区存消息 1、4、7，第二个分区存 2、5、8，第三个分区存 3、6、9。

#### 5、分区数和副本数怎么设置？

​	分区的数量并不是越多越好，不同的网络环境，这个数量也不尽相同，最好是通过性能测试的脚本验证。

​    一个 partition 可以有若干个副本，副本必须在不同的 Broker 上。在创建 Topic 时可以指定副本数量，也可以通过 `offsets.topic.replication.factor` 参数控制默认的副本数；但是，需要注意一点，**副本数量必须小于等于 Broker 的数量**，不然在创建 Topic 是报错。

#### 6、分区的副本在Broker上是怎么分布的？假设有3个Broker，一个Topic 有两个分区，每个分区有两个副本，可能的分布情况是什么样的？

​	首先，可以确定的是副本的数量小于等于Broker 的数量。一个partition的不同副本不会分布在同一 Broker上。

 	在 Kafka 集群中，每一个 Broker 都有均等分配 Partition 的 leader 机会。在进行分配时，会将 Broker 和分区进行排序。首先会随机选择一个Broker 存放 第0个分区的主副本，而这个分区的其他副本会顺序的分布在后面的 Broker 上面。第二个分区的主副本会分布在上一个分区主副本的所在Broker 的下一个 Broker 上面。

<img src="C:\Users\sangaizhi\AppData\Roaming\Typora\typora-user-images\image-20210520123854808.png" alt="image-20210520123854808" style="zoom:50%;" />

#### 7、Segment 的作用是什么？

​	Kafka 的数据是存放在 XXX.log 文件里面。如果一个 partition 只有一个 log 文件，随着消息的增加，这个 log 文件也坏变得越来越大，这个时候检索数据的效率就很低了。所以为了来解决文件太大的问题，Kafka 干脆把 log 文件做了一个切分，切分出来的单位叫做段(Segment)。所以 Kafka 的存储文件是划分成段来存储的。

segment 的大小可以通过 `log.segment.bytes` 参数控制，默认为 1073741824 bytes（1GB）。

#### 8、Consumer Group 的含义是什么？

​	当生产者生成消息的速率过快，会造成消息在 Broker 堆积，影响 Broker 的性能。为了提升性能，需要增加消费者的数量，而为了让这些消费者消费的是同一个 Topic，就引入了 Consumer Group 的概念，在代码中通过 group id来配置。消费同一个 Topic 的消费者不一定是同一个 Consumer Group, 但是 同一个 Consumer Group 的消费者一定是消费同一个 Topic。**需要注意的是，同一个 group 中的消费者，不能消费相同的 partition，也就是说partition需要在这个 group 中的 消费者之间进行分配**。

所以就有一下情况：

	1. 消费者比 partition 少，就意味着一个消费者可以消费多个 partition。
 	2. 如果消费者比partition多，那肯定有消费者没有 partition可以消费，不会出现一个 group 中的消费者消费同一个 partition的情况。

#### 9、ConsumerGroup 上次消费的偏移量记录在哪里？怎么更新？

在服务端的 __consumer_offsets 的 Topic 里面。

更新：消费者自动提交或者手动提交，由 `enable.auto.commit` 参数控制。还可以通过

`auto.commit.interval.ms`来控制自动提交的频率。如果不提交或者提交失败，Borker 的

offset不会更新，消费者下次消息的时候会消费到重复的消息。

#### 10、生产者发送消息如何选择分区？

   1. 指定 partitioin;

   2. 没有指定 partition,自定义了分区器;

   3. 没有指定 partition，没有自定义分区器，但是 key 不为空;

      将 key 的hash值与 topic 的partition 数进行取余得到 partition。    

        4. 没有指定 partition，没有自定义分区器，但是 key 是空的；

       第一次调用时随机生成一个整数(后面每一次调用在这个整数上递增),将这个值与 topic 可用的 partition哦 总数取余得到 partition值，也就是常说的 round-robin 算法。

#### 11、生产者的acks参数的1,0，-1 的含义是什么？

acks==0: 生产者不等待 broker 的ack,此时提供最低的延迟和，broker 接收到消息还没有写入磁盘就已经返回，当 broker 故障时有可能丢失数据。

acks==1: 生产者等待 broker 的ack,partition 的 leader落盘成功后返回 ack,如果在 follower 同步成功之前 leader 故障，那么将会丢失数据。

acks==-1:生产者等待 broker 的ack，partition 的leader和follower 全部落盘成功后才返回 ack。如果在follower 同步完成后，broker发送ack之前，leader发生故障，没有给生产者发送ack，那么就会造成数据重复。这种情况下，把reties设置为0，才不会重发。

#### 12、消息的主要物理存储文件有哪些？

​	topic-XXX.log   存储消息的文件 

​    topic-XXX.index  偏移量索引文件  

​	topic-XXX.timeindex  时间戳索引文件

#### 13、Kafka 中哪些索引类型？消息怎么通过索引检索？

索引类型：偏移量索引和时间戳索引。

偏移量索引是稀疏索引（通过`log.index.interval.bytes`控制多少字节的数据就生成一个索引，默认4KB）。

消息索引检索：

1. 消息消费的时候是能够确定分区的，所以第一步是找到在哪个 segment中，Segment 文件是用base offset 命名的，所以可以用二分法很快确定（找到名字小于消息偏移量的segment）。
2. 这个 segment 有对应的索引文件，它们是成套出现的。所以现在要在索引文件中根据offset 找 position。
3. 得到position之后，到对应的log文件开始查找 offset，和消息的offset进行比较，直到找到消息。

#### 14、kafka的稀疏索引间隔有什么决定 ？为什么不用B+Tree?

​	kafka 的稀疏索引通过 `log.index.interval.bytes` 控制，只要写入的消息超过了这个数值，偏移量索引和时间戳索引就会增加一条索引记录。

​	B+Tree 索引的查询效率高，但是在进行插入任何一条数据时都需要更新索引，效率低。而kafka 只有单个索引文件是通过追加的方式进行，只有在新的索引文件建立时才会更新索引。

#### 15、Kafka 的日志文件不可能无限扩大，那可以通过哪些方式释放磁盘空间？

​	针对这些比较久远的消息日志文件，Kafka 提供了一个 `log.cleaner.enable=true` 参数设置是否清理久远的日志。Kafka 提供了两种方式进行清理这些久远的数据,,通过参数 `log.cleaner.policy`控制：

 1. 直接删除 delete

    选定该方式清理日志文件数据时，Kafka 会通过定时任务扫描看看是否有需要删除的数据，扫描频率可以通过参数 `log.retention.check.interval.ms` 控制， 默认300000ms(5 分钟)执行一次。删除数据是从最老的数据进行删除。

    * 按日志数据时间范围删除

      其中针对多久之前的数据是久远数据，Kafka也提供了参数进行设置。

      * `log.retention.hours`: 通过小时设置，默认168小时(一周);

      * log.retention.minutes`: 通过分钟设置，默认为空，优先级比 `log.retention.hours`高;

      * log.retention.ms`: 通过毫秒设置，默认值为空，优先级比 `log.retention.minutes`高。

    * 按照日志文件的大小

      在按照日志数据的时间久远程度进行删除时，存在一个问题，如果生产者生产消息的速度不均匀，有时候一周几百万，有时候一周几千条，那这个时候按照时间删除就不是那么合理了。所以 Kafka 就提供了这种根据日志大小删除，先删除旧的消息，删到不超过这个大小为止。通过 `log.retention.bytes` 参数控制日志文件的大小。默认值为 -1，代表不限制，想写多少消息写多少消息。

 2. 对日志文件进行压缩  compact

    这种策略是不删除，而是对日志数据进行压缩。

    生产者生产的消息可能重复的 key,针对同一个 key 重复写入多次，Kafka 的压缩策略就会把相同的 key 合并为最后一个 value。

    比如：用来存储消费者id 和 partition的offset关系的 `__consumer_offsets` 这个特殊的 Topic，消费者不断的消费消息 commit 的时候，肯定会针对同一个消费者不断的写入新的offset，这样的话就浪费了很多存储空间，这个时候这种对日志文件进行压缩的策略就可以解决空间浪费的问题。

#### 16、Kafka 副本 leader 怎么选举？谁来选举？选谁为leader?什么是AR?什么是 ISR?是什么是 OSR？

​	首先，Kafka在进行 副本选举的时候，不是所有的副本都参与 leader 选举，而是由其中一个 Broker 同一来指挥，这个 Broker 的角色叫做 Controller（控制器）。所以 Kafka 要在 所有的 Broker 中选出唯一的一个 Controller。Kafka 选择 Controller  的过程依赖于Zookeeper,所有的 Broker 会尝试在 zookeeper 中创建临时节点 `/controller`,只有一个能创建成功(先到先得)。如果 Controller  挂掉或者网络出现问题，ZK上的临时节点会消失。这时其他的 Broker 会通过watch监听到 Controller 下线，就会竞选新的 controller，方法和之前一样，写入 `/controller` 临时节点。一个节点称为 controller 之后，它就需要做以下的事情：

 * 监听 Broker 的变化
 * 监听 Topic 的变化
 * 监听 Partition 的变化
 * 获取和管理 Broker、Topic、Partition 的信息
 * 管理 Partition 的 主从信息

​    在 Controller 确定之后，就可以开始做分区选主的事情了，在分区选主之前，先了解几个概念：

* AR: 一个分区的所有副本;
* ISR(In-Sync Replicas): 分区所有副本中，和 leader 数据保持一定程度同步的副本;这些副本并不是固定的，而是一个动态的列表。如果同步延迟超过 30秒，就会被移出 ISR，进入OSR。如果赶上来了，就加入 ISR。
* OSR(Out-Sync Replicas): 分区副本中，跟 leader 同步滞后过多的副本。

​    分区选主：

 1. 确定哪些副本可以参加选举

    可以参加选择副本就是 ISR 中那些副本。

	2. 选举算法

    Kafka 并没有使用常见的 ZAB(ZK)、Raft(Redis Sentinel) 等选举协议。而是用一种自己实现的算法。最接近的是微软的 PacificA 算法。这种算法默认是让 ISR 中的第一个副本编程 leader。

#### 17、Kafka 副本 follower怎么向leader 同步？什么事HW？是么是LEO？

​	要了解同步过程，需要了解几个概念：

 * LEO (Log End Offset)：下一条等待写入的消息的 offset；
 * HW (Hign Watermark)：ISR 中最小 LEO的副本，Leader 会管理所有ISR 中最小的 LEO 作为HW，如下图，目前是6

<img src="C:\Users\sangaizhi\AppData\Roaming\Typora\typora-user-images\image-20210520153436089.png" alt="image-20210520153436089" style="zoom:67%;" />

**<font color="red">注意：consumer 最多只能消费到 HW之前的位置，也就是说其他副本没有同步过去的消息是不能被消费的。</font>**

​	这样设计的目的是因为担心 consumer group 的 offset 会偏大，一旦 leader 崩溃，中间会缺失消息。

​	有了这两个 LEO 和 HW 之后，就可以了解消息的同步过程了。

 * 假设 follower1 同步了一条消息，follower2 同步了2条消息。此时 HW推进了2，变成了8。

   <img src="C:\Users\sangaizhi\AppData\Roaming\Typora\typora-user-images\image-20210520154126397.png" alt="image-20210520154126397" style="zoom:67%;" />

* 在之后，follower1 同步了0条消息，follower2同步了一条消息。此时HW推进了1，变成了9。LEO和HW重叠，所有的消息都可以消费了。

  <img src="C:\Users\sangaizhi\AppData\Roaming\Typora\typora-user-images\image-20210520154308560.png" alt="image-20210520154308560" style="zoom: 67%;" />

  从节点跟主节点保持同步的过程：

  1. follower 节点会向 leader 节点发送一个 丰fetch 请求，leader 向 follower 发送数据后，就需要更新 follower 的LEO。
  2. follower 接收到数据响应后，依次写入并更新 LEO。
  3. leader 更新 HW。

#### 18、Kafka 的消费者的 offset 怎么维护？存在哪里？什么时候更新？

1. **offset 的维护**

​    要了解消费者的 offset 怎么存储，就需要了解消费者是怎么消费消息的。正常情况下，我们希望消费没有被消费过的数据，而且是从最先发送(序号小)的开始消费。对于一个 Partition，消费者怎么才能做到接着上次消费的位置(offset)继续消费呢？肯定需要把这个对应关系保存起来，保存在服务端一个叫做 `__consumer_offsets`的特殊 topic 中，下次消费的时候查找一下。通过命令可以查看一个consumer group 和 topic 分区的偏移量关系。

eg:

| PARTITION | CURRENT-OFFSET | LOG-END-OFFSET | LAG  | CONSUMER-ID |
| --------- | -------------- | -------------- | ---- | ----------- |
| 0         | 5              | 5              | 0    | consumer-1  |
| 1         | 5              | 5              | 0    | consumer-1  |
| 2         | 5              | 5              | 0    | consumer-1  |
| 3         | 5              | 5              | 0    | consumer-2  |
| 4         | 5              | 5              | 0    | consumer-3  |

*CURRENT-OFFSET 指的是下一个未使用的 offset;

*LEO(LOG-END-OFFSET)：下一条等待写入的消息的OFFSET(最新的offset+1)

*LAG是延迟量

注意：不是一个消费者和一个Topic 的关系，而是一个 consumer group 和 topic中的一个 partition 的关系（offset在 partition中连续编号而不是全局连续编号）。

​	`__consumer_offsets`这个topic里面存放是对象类型的value(经过序列化)。这个topic 主要存储两种对象：

 * GroupMetadata: 保存了消费者组中各个消费者的信息(每个消费者有编号)
 * OffsetAndMetadata: 保存了消费者组和各个 Partition 的 offset 位移信息元数据

__consumer_offsets 大致的数据结构是这个样子的：

```txt
[Group,Topic,Partition]::[OffsetMetadata[Offset,Metadata],CommitTime,ExpirationTime]
[consumer-group-1,ass5part,0]::OffsetAndMetadata(offset=6,leaderEpoch=Optional[2],metadata=,commitTimesctamp=1596726098944,expireTimestamp=None)

[consumer-group-1,ass5part,1]::OffsetAndMetadata(offset=6,leaderEpoch=Optional[0],metadata=,commitTimesctamp=1596726098944,expireTimestamp=None)

[consumer-group-1,ass5part,2]::OffsetAndMetadata(offset=6,leaderEpoch=Optional[2],metadata=,commitTimesctamp=1596726098944,expireTimestamp=None)

[consumer-group-1,ass5part,3]::OffsetAndMetadata(offset=6,leaderEpoch=Optional[2],metadata=,commitTimesctamp=1596726098944,expireTimestamp=None)

[consumer-group-1,ass5part,4]::OffsetAndMetadata(offset=6,leaderEpoch=Optional[0],metadata=,commitTimesctamp=1596726098944,expireTimestamp=None)
```

<img src="C:\Users\sangaizhi\AppData\Roaming\Typora\typora-user-images\image-20210520163136873.png" alt="image-20210520163136873" style="zoom: 80%;" />

consumer group 的 offset 放在这个 topic 的哪个分区是通过消费者名称的hash值对分区数量取模得到的。 

针对第一次消费的消费组，在服务端是找不到 offset的，此时可以通过消费者代码中的参数`auto.offset.reset`来控制从哪里开始消费。这个参数有三个可选值：

* latest：默认值，从最新的消息(最后发送的)开始消费，也就是说不消费历史消息。

* earliest：从最早的消息开始消费，可以消费开始消息。

* none：如果 consumer group 在服务端找不到 offset 就报错。

  

2. **Offset 的更新：**

​	消费者在消费了消息之后必须要有一个 commit 的操作，可以是自动 commit ，也可以是手动 commit。通过参数 `enable.auto.commit`控制。为 `true`时消费消息后自动提交，此时 Broker 会更新消费组的 offset；当然也可以使用参数 `auto.commit.interval.ms` 控制自动提交的频率。为 `false` 时代表手动提交，手动提交有两种方式：consumer.commitSync()的同步提交和consumer.commitAsync()异步提交。如果不提交或者提交失败，Broker 的 offset不会更新，消费者下次消费的时候会消费到重复的消息。

#### 19、一个消费组中的消费者与分区的关系是什么样的？例如 topic 有3个分区，假如有两个消费者，怎么分配？假如有4个消费者，怎么分配？

情况1：消费者数量与分区数量一致，则一个消费者一个分区

情况2：消费者数量小于分区数量，则有消费者消费多个分区

情况3：消费者数量大于分区数量，则存在消费者没有分区可以消费

针对情况2：

​	消费组中的消费者与分区的分配有三个策略：
 1. RangeAssignor: 范围分配策略，是默认的分配策略。

       每次对一个主题分配消费者，分区数能整除消费者数，则每个消费者得到分区数一样，如果有余数，排在前面的消费者在有多的剩余分区情况下，就会多分配一个分区。
          eg: 2 个消费者，3个分区，消费者与分区关系就是：
              C1：P0、P1
              C2：P2  

 2. RoundBobinAssignor: 轮询分配策略

          C1: P0、P2
          C2: P1

 3. StickyAssignor: 粘滞策略

       这种策略复杂一点、但是相对均匀一点,每次的结果都可能不一样。但是遵循以下原则：
       a. 分区的分配尽可能均匀
       b. 分区的分配尽可能和上次分配保持相同



针对情况3：没有分区消费的消费者不会一直这样下去，在发生以下两种情况时，会重新分配分区与消费者的关系：

	1. 消费者的消费者数量发生变化。
 	2. Topic 的分区数量发生变化。

重新分配分为以下几步：

​	1、找一个话事人，它起到一个监督和保证公平的作用。每个 Broker 上都有一个用来管理 offset、消费者组的实例，叫做 GroupCoordinator。第一步就是要从所有的 GroupCoordinator中找一个话事人出来。

​	2、清点所有的消费者。所有的消费者连接到 GroupCoordinator 报数，这个叫 join group 请求。

​    3、选组长，GroupCoordinator 从所有的消费者里面选择一个 leader,这个消费者会根据消费者的情况和设置的策略，确定一个方案。Leader 把方案上报给 GroupCoordinator，GroupCoordinator会通知所有的消费者。