package com.wk;

import com.wk.protobuf.FirstProtobuf;
import sun.security.util.DerEncoder;

import java.io.*;

public class BufMain {
    private static String udir = System.getProperty("user.dir");
    private static String path = udir + "/protobuf/src/main/resources/object.bin";
    public static void main(String[] args) throws IOException {
        SerialObject();
        DeserialObj();
    }

    public static void DeserialObj() throws IOException {
        System.out.println("begin to deserial");
        FileInputStream inputStream = new FileInputStream(new File(path));
        FirstProtobuf.testBuf testBuf = FirstProtobuf.testBuf.parseFrom(inputStream);
        System.out.println("Deserial Done.");
        System.out.println("testBuf Id:" + testBuf.getID());
        System.out.println("testBuf Url:"+ testBuf.getUrl());
    }

    public static void SerialObject() throws IOException {
        FirstProtobuf.testBuf.Builder builder = FirstProtobuf.testBuf.newBuilder();
        builder.setID(1);
        builder.setUrl("www.baidu.com");
        FirstProtobuf.testBuf buf = builder.build();
        byte[] bytes = buf.toByteArray();
        System.out.println("byte length : " + bytes.length +", begin to serial");
        FileOutputStream stream = new FileOutputStream(new File(path));
        stream.write(bytes);
        stream.flush();
        stream.close();
        System.out.println("Done");
    }

}
