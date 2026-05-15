package com.linetime.blog.thread.sample;

import java.util.concurrent.TimeUnit;

/**
 * @author LT
 * @date 2021年10月12日
 */
public class StopThreadDemo {


    public static void main(String[] args) throws InterruptedException {
        Runner one = new Runner();
        Thread countThread = new Thread(one, "CountThreadOne");
        countThread.start();
        TimeUnit.MILLISECONDS.sleep(500);
        // 通过中断终止线程
        countThread.interrupt();

        Runner two =new Runner();
        countThread = new Thread(two,"CountThreadTwo");
        countThread.start();
        TimeUnit.MILLISECONDS.sleep(500);
        // 通过变量终止任务
        two.cancel();
    }

    private static class Runner implements Runnable {
        private long i = 0L;
        private volatile boolean on = true;

        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()) {
                i++;
            }
            System.out.println(Thread.currentThread().getName()+" count i:" + i);
        }
        public void cancel() {
            on = false;
        }
    }
}
