package com.anven.BigData.basic.executorPro;

import java.util.concurrent.*;

/**
 * Java Executor框架
 * 1. Callable于Runnable类型，区别是Callable有返回值
 * 2. Future,用于接收异步任务的返回
 */
public class CallableAndFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Executors.newSingleThreadExecutor的实现
        ExecutorService executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        Future<String> future1 = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {

                return Thread.currentThread().getName() + " 小老弟，嗨起来";
            }
        });
        Future future2 = executor.submit(new Runnable() {
            @Override
            public void run() {

            }
        });
        System.out.println(future1.get());
        System.out.println(future2.get());
        executor.shutdown();
    }
}
