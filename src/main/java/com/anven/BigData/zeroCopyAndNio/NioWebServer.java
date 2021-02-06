package com.anven.BigData.zeroCopyAndNio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 8888));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer writeBuffer = ByteBuffer.allocate(32);
        ByteBuffer readBuffer = ByteBuffer.allocate(32);
        writeBuffer.put("received".getBytes());
        writeBuffer.flip();

        while (true) {
            selector.select(); // Selector开始工作
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                if(key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    // 注意这里是key.channel(),而不是serverSocketChannel.accept()
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    readBuffer.clear();
                    socketChannel.read(readBuffer);

                    readBuffer.flip();
                    while (readBuffer.hasRemaining()) {
                        System.out.println("客户端说： " + (char)readBuffer.get());
                    }
                    key.interestOps(SelectionKey.OP_WRITE);
                } else if (key.isWritable()) {
                    // 注意这里是key.channel(),而不是serverSocketChannel.accept()
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    writeBuffer.rewind();
                    socketChannel.write(writeBuffer);

                    key.interestOps(SelectionKey.OP_READ);
                }
            }
        }
    }
}
