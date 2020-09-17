package com.wk.jdkJMX;

import java.lang.management.ManagementFactory;

public class GetPid {
    public static void main(String[] args) {
        getPid();
    }

    // 获取当前java程序的pid
    public static void getPid(){
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);
        String pid = name.split("@")[0];
        System.out.println("pid = " +pid);
    }
}
