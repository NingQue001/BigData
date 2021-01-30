package com.anven.BigData.zeroCopyAndNio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.*;

/**
 * BIO: Blocking I/O, 阻塞I/O
 */
public class BIOServer {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(9981);
            /**
             * 使用线程池实现伪异步IO
             */
            // 不建议使用Executors创建，应使用ThreadPoolExecutor来创建
//             ExecutorService executorService = Executors.newFixedThreadPool(5);
//            Executors.newSingleThreadExecutor()
            ExecutorService executorService =
                    new ThreadPoolExecutor(3, 10, 60L,
                            TimeUnit.SECONDS, new ArrayBlockingQueue<>(3));
            while(true) {
                System.out.println("等待连接...");
                // 阻塞操作，放弃CPU
                Socket clientSocket = serverSocket.accept();
                System.out.println("连接成功");

                System.out.println("等待数据...");
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("线程：" + Thread.currentThread().getName() + " 正在工作...");
                        try {
                            byte[] bytes = new byte[1024];
                            // 阻塞操作
                            clientSocket.getInputStream().read(bytes);
                            System.out.println("received from client:" + new String(bytes).trim());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("线程：" + Thread.currentThread().getName() + " 结束.");
                    }
                };
                executorService.submit(runnable);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
