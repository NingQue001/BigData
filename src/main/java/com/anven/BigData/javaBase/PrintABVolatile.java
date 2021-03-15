package com.anven.BigData.javaBase;

import java.util.concurrent.atomic.AtomicInteger;

public class PrintABVolatile {
    private static volatile boolean loopForOdd = true;
    private static volatile boolean loopForEven = true;
    private static volatile int count = 1;

    public static void main(String[] args) {
        // 打印奇数线程
        new Thread(() -> {
            while (true) {
                while (loopForOdd) {

                }

                int count = PrintABVolatile.count;
                if(count > 100) break;
                System.out.println(Thread.currentThread().getName() + ": " +count);
                PrintABVolatile.count ++;

                loopForEven = false;
                loopForOdd = true;
            }

        }).start();

        // 打印偶数线程
        new Thread(() -> {
            while (true) {
                while (loopForEven) {

                }

                int count = PrintABVolatile.count;
                if(count > 100) break;
                System.out.println(Thread.currentThread().getName() + ": " +count);
                PrintABVolatile.count ++;

                loopForOdd = false;
                loopForEven = true;
            }

        }).start();

        loopForOdd = false;
    }
}
