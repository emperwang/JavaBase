package com.wk.process.processbuilderuser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 简单入门案例
 * 使用与 linux
 */
public class ProcessBuilder1 {
    public static void main(String[] args) throws IOException, InterruptedException {
        String file="process.txt";
        ArrayList<String> command = new ArrayList<>();
        command.add("bin/echo");
        command.add("aa");
        command.add(" > ");
        command.add(file);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        Process process = builder.start();
        if (process.isAlive())
            process.waitFor();  // 等待子进程退出

    }
}
