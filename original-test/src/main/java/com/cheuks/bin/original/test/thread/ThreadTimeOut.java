package com.cheuks.bin.original.test.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ThreadTimeOut implements Runnable {

    static ExecutorService executorService = Executors.newCachedThreadPool();

    private volatile boolean interrupt;

    private long overTime = System.currentTimeMillis() + 100000;

    Thread thread = new Thread(new Runnable() {

        public void run() {
            while (!interrupt) {
                if (System.currentTimeMillis() > overTime) {
                    throw new RuntimeException("time out");
                }
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("结束");
        }
    });
    {
        thread.start();
    }

    public void run() {
        try {
            Thread.sleep(15000);
            interrupt = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("当前 结束");
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        Future future = executorService.submit(new ThreadTimeOut());
        future.get(55, TimeUnit.SECONDS);
        System.err.println("完成");
    }

}
