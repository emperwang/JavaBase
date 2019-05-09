package com.wk.system;

import java.util.Map;
import java.util.Properties;

public class SystemDemo {
    public static void main(String[] args) {
        //获取系统环境变量
        Map<String, String> env = System.getenv();

 /*       for(Map.Entry<String,String> entry:env.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }*/
        //获取所有的参数
        Properties properties = System.getProperties();
        System.out.println(properties.get("env"));
        System.out.println("---------------------------------");

        //java运行时版本
        System.out.println("java.version="+System.getProperty("java.version"));
        //java的安装目录
        System.out.println("java.home="+System.getProperty("java.home"));
        //java类格式的版本号
        System.out.println("java.class.version="+System.getProperty("java.class.version"));
        //java的查找路径
        System.out.println("java.class.path="+System.getProperty("java.class.path"));
        //默认的临时目录
        System.out.println("java.io.tmpdir="+System.getProperty("java.io.tmpdir"));
        //java所使用的及时编译器
        System.out.println("java.compiler="+System.getProperty("java.compiler"));
        //java扩展包的目录
        System.out.println("java.ext.dirs="+System.getProperty("java.ext.dirs"));
        //操作系统名字
        System.out.println("os.name="+System.getProperty("os.name"));
        //操作系统体系架构
        System.out.println("os.arch="+System.getProperty("os.arch"));
        //操作系统版本
        System.out.println("os.version="+System.getProperty("os.version"));
        //文件分隔符
        System.out.println("file.separator="+System.getProperty("file.separator"));
         //路径分隔符
        System.out.println("path.separator="+System.getProperty("path.separator"));
        //换行符
        System.out.println("line.separator="+System.getProperty("line.separator"));
        //用户账号名
        System.out.println("user.name="+System.getProperty("user.name"));
        // 用户家目录
        System.out.println("user.home="+System.getProperty("user.home"));
         //用户当前的工作目录
        System.out.println("user.dir="+System.getProperty("user.dir"));
    }
}
