package com.wk.system;

import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * 把标准输出指向到一个文件，这样输出到标准输出的内容就会输出到文件中
 */
public class SystemOutFile {
    //加载resources下的文件
   String path = getClass().getClassLoader().getResource("log.txt").getPath();

    public static void main(String[] args) throws FileNotFoundException {
        SystemOutFile outFile = new SystemOutFile();

        //得到标准输出,并存储到一个变量中
        PrintStream out = System.out;
        String path = "log.txt";
        System.out.println(outFile.getPath().substring(1));
        PrintStream printStream = new PrintStream(path);
        //设置输出到文件中
        System.setOut(printStream);
        int age = 11;
        System.out.println("age = "+age);
        String sex = "woman";
        System.out.println("sex = "+sex);

        System.setOut(out);
        System.out.println("程序运行完毕");
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
