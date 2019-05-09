package com.wk.enum_demo.demo6;

public class Main {
    public static void main(String[] args) {
        String info = EnumInterface.GREEN.getInfo();
        System.out.println(info);
        System.out.println(EnumInterface.RED.getInfo());
    }
}
