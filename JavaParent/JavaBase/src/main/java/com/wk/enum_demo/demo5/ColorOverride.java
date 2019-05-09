package com.wk.enum_demo.demo5;

public enum  ColorOverride {
    RED("红色",1),
    GREEN("绿色",2),
    BLANK("黑色",3),
    YELLOW("黄色",4)
    ;

    private String name;
    private int index;

    private ColorOverride(String name,int index){
        this.name = name;
        this.index = index;
    }
    //覆盖枚举的toString方法
    @Override
    public String toString() {
        return this.index+"_"+this.name;
    }
}
