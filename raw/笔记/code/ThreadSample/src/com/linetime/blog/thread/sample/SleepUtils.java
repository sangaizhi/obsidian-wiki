package com.linetime.blog.thread.sample;

import java.util.concurrent.TimeUnit;

/**
 * @author LT
 * @date 2021年10月12日
 */
public class SleepUtils {

    public static final void second(long seconds){
        try{
            TimeUnit.SECONDS.sleep(seconds);
        }catch (InterruptedException e){

        }
    }
}
