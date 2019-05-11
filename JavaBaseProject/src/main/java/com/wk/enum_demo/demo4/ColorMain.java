package com.wk.enum_demo.demo4;

public class ColorMain {
    public static void main(String[] args) {
        //调用静态方法,根据index获取name
        String name = Color.BLANK.getName(1);
        System.out.println(name);
        //直接调用getName,获取的当前实体的name
        String name1 = Color.BLANK.getName();
        System.out.println(name1);
    }
}
