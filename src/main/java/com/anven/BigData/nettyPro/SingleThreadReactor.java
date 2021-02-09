package com.anven.BigData.nettyPro;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 单线程Reactor
 * http://gee.cs.oswego.edu/dl/cpjslides/nio.pdf
 *
 * 1. Reactor通过Selector监控客户端事件，收到事件后通过Dispatch进行分发
 * 2. 如果是连接请求事件，则由Acceptor通过Accept处理连接请求，并新建一个Handler对象处理连接完成后的业务代码
 * 3. 如果不是连接请求事件，则Reactor会分发给Handler完成实际的业务处理流程
 */
public class SingleThreadReactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel serverSocket;
    SingleThreadReactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        // 绑定指定端口的Socket
        serverSocket.socket().bind( new InetSocketAddress(port));
        // 设置为非阻塞，才能注册Selector
        serverSocket.configureBlocking(false);
        // SelectionKey 处理IO事件的状态和绑定
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }

    @Override
    public void run() {
        // normally in a new Thread
        try {
            // 线程正常运行
            while (!Thread.interrupted()) {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()) {
                    dispatch((SelectionKey) it.next());
                }
                selected.clear();
            }
        } catch (IOException ex) {
            /* ... */
        }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable)(k.attachment());
        if (r != null) {
            r.run();
        }
    }

    class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel c = serverSocket.accept();
                if (c != null) {
                    new Handler(selector, c);
                }
            } catch(IOException ex) {
                /* ... */
            }
        }
    }
}

class Handler implements Runnable {
    final SocketChannel socket;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;
    Handler(Selector sel, SocketChannel c) throws IOException {
        socket = c;
        c.configureBlocking(false);
        // Optionally try first read now
        sk = socket.register(sel, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        sel.wakeup();
    }
    boolean inputIsComplete() {
        /* ... */
        return false;
    }
    boolean outputIsComplete() {
        /* ... */
        return false;
    }
    void process() {
        /* ... */
    }

    @Override
    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                send();
            }
        } catch (IOException ex) { /* ... */ }
    }

    void read() throws IOException {
        socket.read(input);
        if (inputIsComplete()) {
            process();
            state = SENDING;
            // Normally also do first write now
            sk.interestOps(SelectionKey.OP_WRITE);
        }
    }
    void send() throws IOException {
        socket.write(output);
        if (outputIsComplete()) {
            sk.cancel();
        }
    }
}
