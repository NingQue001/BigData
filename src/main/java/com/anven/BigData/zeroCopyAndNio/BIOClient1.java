package com.anven.BigData.zeroCopyAndNio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * BIO: Blocking I/O, 阻塞I/O
 */
public class BIOClient1 {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9981);
            Scanner scanner = new Scanner(System.in);
            String message = scanner.next();
            socket.getOutputStream().write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
