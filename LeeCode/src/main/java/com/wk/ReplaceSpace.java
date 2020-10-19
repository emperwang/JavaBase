package com.wk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: wk
 * @Date: 2020/10/19 14:38
 * @Description
 */
/*
请实现一个函数，把字符串 s 中的每个空格替换成"%20"。

示例 1：
输入：s = "We are happy."
输出："We%20are%20happy.

 */
public class ReplaceSpace {
    static String s = "We are happy.";

    public static String replaceSpace(String s) {
        String pat = "\\s";
        if (s != null && s.length() >0){
            final Pattern compile = Pattern.compile(pat);
            final Matcher matcher = compile.matcher(s);
            return matcher.replaceAll("%20");
        }
        return null;
    }
    public static String replaceSpace2(String s) {
        String pat = "\\s";
        if (s != null && s.length() >0){
            final Pattern compile = Pattern.compile(pat);
            final Matcher matcher = compile.matcher(s);
            final StringBuffer buffer = new StringBuffer();
            while (matcher.find()){
                matcher.appendReplacement(buffer,"%20");
            }
            matcher.appendTail(buffer);
            return buffer.toString();
        }
        return null;
    }

    public static String replaceSpace3(String s) {
        char[] cs = new char[s.length()*3];
        int idx = 0;
        if (s != null && s.length() >0){
            for(int i=0;i<s.length();i++){
                final char c = s.charAt(i);
                if (c == ' '){
                    cs[idx++] = '%';
                    cs[idx++] = '2';
                    cs[idx++] = '0';
                }else{
                    cs[idx++] = c;
                }
            }
            return new String(cs,0,idx);
        }
        return null;
    }



    public static void main(String[] args) {
        System.out.println(replaceSpace(s));
        System.out.println(replaceSpace2(s));
        System.out.println(replaceSpace3(s));
    }
}
