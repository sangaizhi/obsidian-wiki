package com.linetime.blog.thread.sample;

/**
 * @author LT
 * @date 2021年10月13日
 */
public class ThreadJoinDemo {

    public static void main(String[] args) {
        Thread pre = Thread.currentThread();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new JoinThread(pre),"join-thread-"+i);
            thread.start();
            pre = thread;
        }
        SleepUtils.second(5);
        System.out.println(Thread.currentThread().getName()+" terminate");
    }

    static class JoinThread implements Runnable{
        private Thread thread;
        public JoinThread(Thread thread){
            this.thread = thread;
        }
        @Override
        public void run() {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" terminate");
        }
    }
}
