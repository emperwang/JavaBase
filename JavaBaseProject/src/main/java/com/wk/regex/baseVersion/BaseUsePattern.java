package com.wk.regex.baseVersion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUsePattern {
    public static void main(String[] args) {
        //checkOne();
        //checkTwo();
        //checkThree();
        // checkFour();
        //checkFive();
        //checkSix();
        checkSeven();
    }

    public static void checkSeven(){
        Pattern pattern = Pattern.compile("cat");
        Matcher matcher = pattern.matcher("one cat,two cat,three cat");

        StringBuffer buffer = new StringBuffer();
        int findNum = 0;

        while (matcher.find()){
            // 边替换 边把值追加到buffer中
            matcher.appendReplacement(buffer,"dog");
            findNum++;
            if (findNum == 2){
                break;
            }
        }
        // 把字符串最后没有进行匹配的值 直接追加到buffer中
        matcher.appendTail(buffer);
        System.out.println(buffer.toString());
    }

    public static void checkSix(){
        String regex = "#";
        String str = "#replace first char";
        // 转义元字符 $, 使用 $ 替换 #
        String s = str.replaceAll(regex, "\\$");
        System.out.println(s);
    }

    public static void checkFive(){
        String regex = "(\\d{4})-(\\d{2})-(\\d{2})";
        String str = "today is 2019-09-09,yesterday is 2019-09-08";
        // 捕获分组信息
        String s = str.replaceAll(regex, "$1/$2/$3");
        System.out.println(s);
    }

    public static void checkFour(){
        String regex = "\\s+";
        String str = "hello                              world                   good";

        String replace = str.replaceAll(regex, " ");
        System.out.println(replace);
    }

    public static void checkThree(){
        String regex = "(\\d{4})-(\\d{2})-(\\d{2})";

        Pattern pattern = Pattern.compile(regex);

        String str = "today is 2019-09-09, yesterday is 2019-09-08";

        Matcher matcher = pattern.matcher(str);
        // 获取分组
        while (matcher.find()){
            System.out.println("year = " + matcher.group(1));
            System.out.println("month = "+matcher.group(2));
            System.out.println("day = "+matcher.group(3));
        }
    }

    public static void checkTwo(){
        String reges = "\\d{8}";
        String str = "12345678";
        Pattern compile = Pattern.compile(reges);
        Matcher matcher = compile.matcher(str);
        //boolean matches = matcher.matches();
        while (matcher.find()){
            String group = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            System.out.println("group = "+group +", start = "+start + ", end = "+end);
        }
    }

    public static void checkOne(){
        // 定义正则表达式
        String reges = "\\d{8}";
        String str = "12345678";

        boolean matches = str.matches(reges);
        System.out.println(matches);
    }
}
