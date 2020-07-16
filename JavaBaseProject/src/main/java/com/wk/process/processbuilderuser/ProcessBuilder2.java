package com.wk.process.processbuilderuser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *  读取子进程的输出结果
 */
public class ProcessBuilder2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> command = new ArrayList<>();
        command.add("ipconfig");
        builder.command(command);
        // 将标准输出流和错误输出流合并，通过标准输入流读取消息
        builder.redirectErrorStream(true);
        // 进程启动
        Process process = builder.start();
        // 获取进程的输出流
        InputStream inputStream = process.getInputStream();
        // 转换为字符输入流
        InputStreamReader reader = new InputStreamReader(inputStream,"GBK");
        int len = -1;
        char[] c = new char[1024];
        StringBuffer buffer = new StringBuffer();
        while ((len = reader.read(c)) != -1){
            String str = new String(c, 0, len);
            buffer.append(str);
        }
        System.out.println(buffer.toString());
        // 等待子进程结束
        if (process.isAlive())
            process.waitFor();
        System.out.println("子进程结束");
    }
}
