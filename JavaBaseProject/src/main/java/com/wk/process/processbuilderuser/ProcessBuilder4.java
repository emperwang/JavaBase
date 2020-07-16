package com.wk.process.processbuilderuser;

import java.io.*;
import java.util.ArrayList;

/**
 * 调用java程序
 */
public class ProcessBuilder4 {
    public static void main(String[] args) throws IOException, InterruptedException {
        String jarP = "F:\\github_code\\Mine\\JavaBase\\ForCommandCall\\target\\command-call.jar";
        // 指定路径
        String path = "F:\\github_code\\Mine\\JavaBase\\JavaBaseProject\\src\\main\\java\\com\\wk\\process\\processbuilderuser\\";
        String errFile = path + "stderr";
        String outFile = path + "stdout";
        // 构建日志文件
        File error = new File(errFile);
        File stdout = new File(outFile);

        // 构建命令
        ArrayList<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(jarP);
        command.add("com.wk.StartMain");
        command.add(" this  ");
        command.add(" is  ");
        command.add(" call ");
        command.add(" from  ");
        command.add(" process ");

        // 构建 processBuilder
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        // 重定向 输出; 这导致每次回把文件中的内容覆盖
        //builder.redirectOutput(stdout);
        //builder.redirectError(error);

        // 启动
        Process process = builder.start();

        // 追加 输出到文件
        OutputStreamWriter errorWriter = new OutputStreamWriter(new FileOutputStream(errFile,true));
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(outFile, true));
        InputStream errorStream = process.getErrorStream();
        //OutputStream outputStream = process.getOutputStream();
        InputStream outpuStream = process.getInputStream();
        char[] buf1 = new char[1024];
        char[] buf2 = new char[1024];
        int len1 = -1;
        int len2 = -1;
        InputStreamReader errorReader = new InputStreamReader(errorStream);
        InputStreamReader outputReader = new InputStreamReader(outpuStream);
        while ((len1 = errorReader.read(buf1)) != -1){
            errorWriter.write(buf1,0,len1);
        }
        while ((len2 = outputReader.read(buf2)) != -1){
            outputStreamWriter.write(buf2,0,len2);
        }

        // 等待完成
        if (process.isAlive())
            process.waitFor();

        errorReader.close();
        outputReader.close();
        errorWriter.close();
        outputStreamWriter.close();
        System.out.println("exit code: " + process.exitValue());
    }
}
