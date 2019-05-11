package com.wk.enum_demo.demo8;


import java.util.EnumSet;

enum Color{
    GREEN,RED,BLUE,BLANK;
}

public class EnumSetDemo {
    public static void main(String[] args) {
        //定义空集合
        EnumSet<Color> enumSet = EnumSet.noneOf(Color.class);
        System.out.println("添加前:"+enumSet.toString());
        //添加元素
        enumSet.add(Color.BLANK);
        // 会自动去重
        enumSet.add(Color.BLANK);
        enumSet.add(Color.RED);
        enumSet.add(Color.BLUE);
        enumSet.add(Color.GREEN);
        System.out.println("添加后:"+enumSet.toString());

        System.out.println("-----------------------------------------------");

        //获取指定范围的元素集合
        EnumSet<Color> range = EnumSet.range(Color.GREEN, Color.BLUE);
        System.out.println("指定初始范围:"+range.toString());
        System.out.println("-----------------------------------------------");
        //指定补集,也就是从全部枚举类型中取出参数集合中的元素
        EnumSet<Color> complementOf = EnumSet.complementOf(range);
        System.out.println(complementOf.toString());
        System.out.println("-----------------------------------------------");

        //初始化直接指定的元素
        EnumSet<Color> blank = EnumSet.of(Color.BLANK);
        System.out.println(blank.toString());

        EnumSet<Color> colors = EnumSet.of(Color.RED, Color.BLUE);
        System.out.println(colors.toString());
        System.out.println("-----------------------------------------------");

        //赋值colors 中的数据为初始化数据
        EnumSet<Color> copyOf = EnumSet.copyOf(colors);
        System.out.println(copyOf.toString());
        System.out.println("-----------------------------------------------");
    }
}
