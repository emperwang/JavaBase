package com.wk.enum_demo.demo4;

public enum  Color {
    RED("红色",1),
    GREEN("绿色",2),
    BLANK("黑色",3),
    YELLOW("黄色",4);

    private String name;
    private int index;

    private Color(String name,int index){
        this.name = name;
        this.index = index;
    }

    //普通静态方法,可通过枚举直接调用
    public static String getName(int index){
        for(Color c:Color.values()){
            if(c.index == index){
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
