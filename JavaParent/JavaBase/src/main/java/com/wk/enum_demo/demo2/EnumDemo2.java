package com.wk.enum_demo.demo2;

public enum  EnumDemo2 {
    //定义实例 并实现抽象方法
    FIRST{
        @Override
        public String getInfo() {
            return "first instance";
        }
    },
    SECOND {
        @Override
        public String getInfo() {
            return "second instance";
        }
    };

    //定义抽象方法
    public abstract String getInfo();

    //测试
    public static void main(String[] args) {
        System.out.println("F:"+EnumDemo2.FIRST);
        System.out.println("S:"+EnumDemo2.SECOND);
    }
}
