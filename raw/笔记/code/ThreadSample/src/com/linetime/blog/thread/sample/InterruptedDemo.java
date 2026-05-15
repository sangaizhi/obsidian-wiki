package com.linetime.blog.thread.sample;

import java.util.concurrent.TimeUnit;

/**
 * @author LT
 * @date 2021年10月12日
 */
public class InterruptedDemo {

    public static void main(String[] args) {
        Thread sleepThread = new Thread(new SleepRunner(),"sleep-thread");
        sleepThread.setDaemon(true);
        Thread busyThread = new Thread(new BusyRunner(),"busy-thread");
        sleepThread.setDaemon(true);

        sleepThread.start();
        busyThread.start();

        SleepUtils.second(5);

        sleepThread.interrupt();
        busyThread.interrupt();

        System.out.println("SleepThread interrupted is:"+ sleepThread.isInterrupted());
        System.out.println("BUsyThread interrupted is:"+ busyThread.isInterrupted());

        SleepUtils.second(2);
    }

    static class SleepRunner implements Runnable{

        @Override
        public void run() {
            while (true){
                try{
                    TimeUnit.SECONDS.sleep(10);
                }catch (InterruptedException e){

                }
            }
        }
    }
    static class BusyRunner implements Runnable{

        @Override
        public void run() {
            while (true){

            }
        }
    }
}
