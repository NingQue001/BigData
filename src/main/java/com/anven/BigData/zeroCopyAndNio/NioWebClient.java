package com.anven.BigData.zeroCopyAndNio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioWebClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));

        ByteBuffer writeBuffer = ByteBuffer.allocate(32);
        ByteBuffer readBuffer = ByteBuffer.allocate(32);
        writeBuffer.put("hello".getBytes("utf-8"));
        writeBuffer.flip();

        while (true) {
            writeBuffer.rewind();
            socketChannel.write(writeBuffer);

            readBuffer.clear();
            socketChannel.read(readBuffer);
            readBuffer.flip();
            while (readBuffer.hasRemaining()) {
                System.out.println("服务器说： " + (char)readBuffer.get());
            }
        }
    }
}
