package com.wk.stringtest;

/**
 *  在常量池中 123  abc  ABCD  456 789  pol等都会在常量池中产生一个字符串常量
 *  并且: build =  |,buffer = |, third = |, fourth= 也会在常量池中产生字符串常量
 */
public class BuilderAndplus {
    public static void main(String[] args) {
        String build = new StringBuilder().append("123").append("abc").append("ABCD").toString();
        String buffer = new StringBuffer().append("qwe").append("asd").toString();
        String first = "456";
        String second = "789";
        String fourth = new String("pol");
        String third = first+second;
        System.out.println("build = "+build+",buffer = "+buffer+", third = "+third+", fourth="+fourth);
    }
}
