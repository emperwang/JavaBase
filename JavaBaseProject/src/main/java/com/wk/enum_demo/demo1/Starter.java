package com.wk.enum_demo.demo1;

public class Starter {
    public static void main(String[] args) {
        DayEnum[] values = DayEnum.values();
        for (int i=0;i<values.length;i++){
            System.out.println(values[i]);
        }
    }
}
