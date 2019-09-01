package com.wk.tool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *  这里注意：
 *  1.当程序没有打包成jar包时，读取resources中配置文件使用  src/main/resources 目录前缀
 *  2.当然了，程序编译后都是在classes目录中，而下面两个方法都是从classes目录读取，使用相对目录就可以了
 *      比如: 当前读取文件使用 "keys/file.cer"  就可以读取到
 */
public class FilePathUtil {

    public static String getFilePath(String fileName){
        String path = ClassLoader.getSystemClassLoader().getResource(fileName).getPath();
        return path;
    }

    public static String getFilePathByThread(String fileName){
        String path = Thread.currentThread().getContextClassLoader().getResource(fileName).getPath();
        return path;
    }

    public static Properties getProp(String config) throws IOException {
        InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(config);
        Properties properties = new Properties();
        properties.load(resource);
        return properties;
    }

    public static void main(String[] args) throws IOException {
        Properties prop = getProp("config.properties");
        Object o = prop.get("serverjks");
        System.out.println(o);
        String filePath = getFilePathByThread("keys/client.cer");
        System.out.println(filePath);
    }
}
