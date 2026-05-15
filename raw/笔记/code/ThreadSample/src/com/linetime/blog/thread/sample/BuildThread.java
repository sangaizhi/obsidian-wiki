package com.linetime.blog.thread.sample;

import java.util.concurrent.*;

/**
 * @author LT
 * @date 2021年09月23日
 */
public class BuildThread {

    public static class BuildByRunnable implements Runnable {
        @Override
        public void run() { }
    }

    public static class BuildByThead extends Thread {
        @Override
        public void run() { super.run(); }
    }

    public static class BuildByCallable implements Callable {
        @Override
        public String call() { return "BuildByCallable"; }
    }

    public static void main(String[] args) {

        // 实现 Runnable 接口
        BuildByRunnable runnable = new BuildByRunnable();
        new Thread(runnable).start();

        // 实现 Callable 接口并且具有返回值
        new Thread(new FutureTask<>(new BuildByCallable())).start();

        // 继承 Thread 类
        new BuildByThead().start();

        // 线程池
        // 固定线程数量的线程池
        Executors.newFixedThreadPool(5);
        // 单个线程的线程池
        Executors.newSingleThreadExecutor();
        // 缓存线程池
        Executors.newCachedThreadPool();
        // 可延迟的定期执行的线程池
        Executors.newScheduledThreadPool(10);

        new Thread(() -> System.out.println("run...")).start();
    }




}
