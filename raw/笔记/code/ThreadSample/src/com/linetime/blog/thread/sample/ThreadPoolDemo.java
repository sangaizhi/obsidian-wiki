package com.linetime.blog.thread.sample;

import java.util.concurrent.*;

/**
 * @author LT
 * @date 2021年10月14日
 */
public class ThreadPoolDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建线程池
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(10,20,1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
//        executor.execute(() -> System.out.println("execute runnable"));
//       Future<String> future = executor.submit(() -> {
//           System.out.println("submit callable");
//           return "success";
//       });
//       String result = future.get();
//        System.out.println("result:"+result);
//
//        Executors.newCachedThreadPool().submit(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("ScheduledExecutorService");
            }
        });
    }
}
