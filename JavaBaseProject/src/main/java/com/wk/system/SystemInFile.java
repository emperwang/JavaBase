package com.wk.system;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * 把标准输入指向到一个文件，这样就会从文件中读取内容
 */
public class SystemInFile {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream in = System.in;
        FileInputStream fileInputStream = new FileInputStream("log.txt");
        System.setIn(fileInputStream);
        Scanner scanner = new Scanner(System.in);
        String line = "";
        while (scanner.hasNextLine()){
            line = scanner.nextLine();
            System.out.println(line);
        }
    }
}
