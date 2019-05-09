package com.wk.enum_demo.demo6;

interface Behaviour{
    void print();
    String getInfo();
}

/**
 * 因为enum已经继承了enum类，所有不能在集成,这就可以实现某个接口
 * 来统一接口
 */
public enum  EnumInterface implements Behaviour{
    RED("red",1),
    GREEN("green",2);

    private String name;
    private int index;

    private EnumInterface(String name,int index){
        this.name = name;
        this.index = index;
    }

    @Override
    public void print() {
        System.out.println("name= "+name+"-- index= "+index);
    }

    @Override
    public String getInfo() {
        return this.name+"__"+this.index;
    }
}
