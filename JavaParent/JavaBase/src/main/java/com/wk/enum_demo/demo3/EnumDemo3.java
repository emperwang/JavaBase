package com.wk.enum_demo.demo3;

/**
 * 在switc语句中使用枚举
 */
public class EnumDemo3 {
    enum Singal{
        GREEN,YELLOW,RED
    }

    public static void main(String[] args) {
        Singal color = Singal.RED;
        switch (color){
            case RED:
                System.out.println("red");
                break;
            case GREEN:
                System.out.println("green");
                break;
            case YELLOW:
                System.out.println("yellow");
                break;
        }
    }
}
