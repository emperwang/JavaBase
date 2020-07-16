package com.wk.process.runtimeexec;

import java.io.*;

/**
 * Runtime.exec 底层仍然是使用 process来实现的
 */
public class ExecJar {
    public static void main(String[] args) throws IOException, InterruptedException {
        String jarP = "F:\\github_code\\Mine\\JavaBase\\ForCommandCall\\target\\command-call.jar";
        // 拼接命令
        String cmd = "java -cp "+jarP+" com.wk.StartMain this is call from exec";

        Process process = Runtime.getRuntime().exec(cmd);

        // 把进程的内容输出到 终端
        InputStream inputStream = process.getInputStream();
        InputStream errorStream = process.getErrorStream();

        //appendFile(inputStream, errorStream);
        appendToConsole(inputStream,errorStream);

        if (process.isAlive()){
            process.waitFor();
        }
        inputStream.close();
        errorStream.close();
        System.out.println("prcess finished");
    }

    // 把进程的输出内容输出到  终端
    public static void appendToConsole(InputStream inputStream,InputStream errorStream)
            throws IOException {
        // 指定路径
        String path = "F:\\github_code\\Mine\\JavaBase\\JavaBaseProject\\src\\main\\java\\com\\wk\\process\\processbuilderuser\\";
        String errFile = path + "stderr";
        String outFile = path + "stdout";
        // 构建日志文件
        File error = new File(errFile);
        File stdout = new File(outFile);
        // 追加 输出到文件

        InputStreamReader outputReader = new InputStreamReader(inputStream, "GBK");
        InputStreamReader errorReader = new InputStreamReader(errorStream, "GBK");

        char[] buf1 = new char[1024];
        char[] buf2 = new char[1024];
        int len1 = -1;
        int len2 = -1;
        while ((len1 = errorReader.read(buf1)) != -1){
            System.out.println(new String(buf1,0, len1));
        }
        while ((len2 = outputReader.read(buf2)) != -1){
            System.out.println(new String(buf2, 0, len2));
        }

    }

    // 把进程的输出内容追加到文件中
    public static void appendFile(InputStream inputStream,InputStream errorStream)
            throws IOException {
        // 指定路径
        String path = "F:\\github_code\\Mine\\JavaBase\\JavaBaseProject\\src\\main\\java\\com\\wk\\process\\processbuilderuser\\";
        String errFile = path + "stderr";
        String outFile = path + "stdout";
        // 构建日志文件
        File error = new File(errFile);
        File stdout = new File(outFile);
        // 追加 输出到文件
        OutputStreamWriter errorWriter = new OutputStreamWriter(new FileOutputStream(errFile,true));
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(outFile, true));
        InputStreamReader outputReader = new InputStreamReader(inputStream, "GBK");
        InputStreamReader errorReader = new InputStreamReader(errorStream, "GBK");

        char[] buf1 = new char[1024];
        char[] buf2 = new char[1024];
        int len1 = -1;
        int len2 = -1;
        while ((len1 = errorReader.read(buf1)) != -1){
            errorWriter.write(buf1,0,len1);
        }
        while ((len2 = outputReader.read(buf2)) != -1){
            outputStreamWriter.write(buf2,0,len2);
        }

        errorWriter.close();
        outputStreamWriter.close();
    }
}
