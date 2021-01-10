package com.anven.BigData.zeroCopyAndNio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * 零拷贝: 采用操作系统的sendfile这样一个system call来直接操作主内存的数据，即DMA(Direct Memory Access)技术
 * kafka的零拷贝技术使用了nio FileChannel的transferTo方法
 *
 */
public class KafkaZeroCopy {
    public static void main(String[] args) throws Exception {
        RandomAccessFile fromFile = new RandomAccessFile("/usr/local/data/nio/from.txt", "rw");
        FileChannel fromFc = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("/usr/local/data/nio/to.txt", "rw");
        FileChannel toFc = toFile.getChannel();

        fromFc.transferTo(0, fromFile.length(), toFc);
        fromFc.close();
        toFc.close();
    }
}
