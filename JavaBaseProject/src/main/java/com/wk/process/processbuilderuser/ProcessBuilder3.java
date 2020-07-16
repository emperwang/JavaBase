package com.wk.process.processbuilderuser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 将子进程的标准输出  错误输出 重定向到文件
 */
public class ProcessBuilder3 {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 指定路径
        String path = "F:\\github_code\\Mine\\JavaBase\\JavaBaseProject\\src\\main\\java\\com\\wk\\process\\processbuilderuser\\";
        String errFile = path + "stderr";
        String outFile = path + "stdout";
        // 构建日志文件
        File error = new File(errFile);
        File stdout = new File(outFile);

        // 构建命令
        ArrayList<String> command = new ArrayList<>();
        command.add("ipconfig");

        // 构建进程
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);

        // 重定向标准输出流  标准错误流
        builder.redirectError(error);
        builder.redirectOutput(stdout);

        // 进程启动
        Process process = builder.start();

        // 等待完成
        if (process.isAlive())
            process.waitFor();
        System.out.println("完成, return  code: " + process.exitValue());
    }
}
