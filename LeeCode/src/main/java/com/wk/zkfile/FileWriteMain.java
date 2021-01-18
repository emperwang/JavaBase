package com.wk.zkfile;

import java.io.*;
import java.util.zip.Checksum;

/**
 * @author: wk
 * @Date: 2021/1/14 16:23
 * @Description
 */
public class FileWriteMain {
    File logFileWrite = null;
    private FilePadding filePadding = new FilePadding();
    volatile BufferedOutputStream logStream = null;
    volatile OutputArchive oa;
    volatile FileOutputStream fos = null;

    public synchronized boolean append(byte[] buf)
            throws IOException
    {
        if (logStream==null) {
            logFileWrite = new File("C:\\code-workspace\\ttt.log");
            fos = new FileOutputStream(logFileWrite);
            logStream=new BufferedOutputStream(fos);
            oa = BinaryOutputArchive.getArchive(logStream);
            // Make sure that the magic number is written before padding.
            filePadding.setCurrentSize(fos.getChannel().position());
        }
        final long padFile = filePadding.padFile(fos.getChannel());
        System.out.println("padfile size: "+ padFile);
        writeTxnBytes(oa,buf);
        logStream.flush();
        return true;
    }

    public static void writeTxnBytes(OutputArchive oa, byte[] bytes)
            throws IOException {
        oa.writeBuffer(bytes, "txnEntry");
        oa.writeByte((byte) 0x42, "EOR"); // 'B'
    }

    public static void main(String[] args) throws IOException {
        final long size = FilePadding.calculateFileSizeWithPadding(12 * 1024, 10 * 1024, 20 * 1024);
        System.out.println(size/1024);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputArchive boa = BinaryOutputArchive.getArchive(baos);
        // byte = 8 bit
        // int = 4 byte
//        String s= "Hello World";
//        System.out.println("str length is :" + s.length()); // 11
//        boa.writeString(s,"test");
//        System.out.println("byteArray length:" + baos.toByteArray().length); // 15
        byte[] bytes = new byte[1024*1024];  // 1k
        final FileWriteMain writeMain = new FileWriteMain();
        for (int i =0;i <10;i++){
            writeMain.append(bytes);
        }

    }
}
