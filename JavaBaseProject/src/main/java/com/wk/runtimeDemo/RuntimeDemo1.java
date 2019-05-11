package com.wk.runtimeDemo;

import java.io.IOException;

public class RuntimeDemo1 {
    public static void main(String[] args) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        int availableProcessors = runtime.availableProcessors();
        long maxMemory = runtime.maxMemory();
        long freeMemory = runtime.freeMemory();
        System.out.println("totalMemory = "+totalMemory);
        System.out.println("availableProcessors = "+availableProcessors);
        System.out.println("maxMemory = "+maxMemory);
        System.out.println("freeMemory = "+freeMemory);
        System.out.println("-----------------------------------------");
        //调用外部的命令行命令
        Process calc = runtime.exec("calc.exe");
        System.out.println(calc);
    }
}
