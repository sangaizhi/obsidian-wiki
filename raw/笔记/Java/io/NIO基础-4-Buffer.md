### 介绍
NIO 中的 Buffer 是用来与 NIO 的 Channel 进行交互用的。可以把数据从 Channel 写到 Buffer 中，也可以把 Buffer 中的数据写到 Channel 中。
### 基本用法
使用 Buffer 一般遵循以下四个步骤：
1. 写入数据到 Buffer 中；
2. 调用 flip() 方法把 Buffer 从写模式切换到读模式；
3. 从 Buffer 中读取数据；
4. 读完以后，通过调用 clear() 或者 compact() 方法清理 Buffer。
在第一步中，当向buffer写入数据时，buffer会记录下写了多少数据。
在第三步中，可以读取之前写入到buffer的所有数据
在第四步中，读完数据，就需要清理 Buffer，以便再次写入。 clear() 方法会清空真个缓冲区；compact() 方法只会清除已经读过的数据；然和未读过的数据都会移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面；
### 属性
#### capacity
Buffer 的容量，Buffer 作为一个内存块，有一个固定大小的值。当 Buffer 满了，需要进行清理（通过读数据或者清除数据）之后，才能继续我那个里写数据。
#### position
在写的模式下,position表示当前的位置。初始的position值为0.当一个byte、long等数据写到Buffer后，position会向前移动到下一个可插入数据的Buffer单元。position最大可为capacity – 1.
当读取数据时，也是从某个特定位置读。当将Buffer从写模式切换到读模式，position会被重置为0. 当从Buffer的position处读取数据时，position向前移动到下一个可读的位置。 
#### limit
在写模式下，Buffer的limit表示你最多能往Buffer里写多少数据。 写模式下，limit等于Buffer的capacity。
当切换Buffer到读模式时， limit表示你最多能读到多少数据。因此，当切换Buffer到读模式时，limit会被设置成写模式下的position值。换句话说，你能读到之前写入的所有数据（limit被设置成已写数据的数量，这个值在写模式下就是position）。

### 方法
#### mark() 与 reset()
通过调用Buffer.mark()方法，可以标记Buffer中的一个特定position。之后可以通过调用Buffer.reset()方法恢复到这个position。
#### rewind()方法
Buffer.rewind()将position设回0，所以你可以重读Buffer中的所有数据。limit保持不变，仍然表示能从Buffer中读取多少个元素（byte、char等）。
#### clear()与compact()方法
一旦读完Buffer中的数据，需要让Buffer准备好再次被写入。可以通过clear()或compact()方法来完成。
如果调用的是clear()方法，position将被设回0，limit被设置成 capacity的值。换句话说，Buffer 被清空了。Buffer中的数据并未清除，只是这些标记告诉我们可以从哪里开始往Buffer里写数据。
如果Buffer中有一些未读的数据，调用clear()方法，数据将“被遗忘”，意味着不再有任何标记会告诉你哪些数据被读过，哪些还没有。
如果Buffer中仍有未读的数据，且后续还需要这些数据，但是此时想要先写些数据，那么使用compact()方法。
compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。limit属性依然像clear()方法一样，设置成capacity。现在Buffer准备好写数据了，但是不会覆盖未读的数据。