package com.wk.java.nio.NIO.FileChannelDemo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *  fileChannel 入门程序
 */
public class FileChannelTest {
    public static void main(String[] args) throws IOException {
        // 1.创建一个RandomAccessFile 对象
        RandomAccessFile accessFile = new RandomAccessFile("E:\\test.txt", "rw");
        // 2.通过RandomAccessFile 得到fileChannel
        FileChannel fileChannel = accessFile.getChannel();
        // 3.创建一个读数据缓冲区
        ByteBuffer buf = ByteBuffer.allocate(48);
        // 4. 从通道中读取数据
        int byteRead = fileChannel.read(buf);
        // 5. 创建一个写数据缓冲区
        ByteBuffer bufWrite = ByteBuffer.allocate(48);
        // 6. 写入数据
        bufWrite.put("FileChannel test".getBytes());
        // 7. 设置limit为当前的position位置
        bufWrite.flip();
        // 8. 写入数据
        fileChannel.write(buf);
        while (byteRead != -1){
            System.out.println("Read "+byteRead);
            // 9. 把limit设置到position位置，也就是读取时读取写入的数据就好
            buf.flip();
            // 10. 打印
            while (buf.hasRemaining()){
                System.out.print((char)buf.get());
            }
            // 11. 情况缓冲区
            buf.clear();
            // 12. 读取数据
            byteRead = fileChannel.read(buf);
        }
        fileChannel.close();
    }
}
