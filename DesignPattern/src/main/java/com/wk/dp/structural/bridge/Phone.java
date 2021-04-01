package com.wk.dp.structural.bridge;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 17:21
 * @Description
 */
public abstract class Phone {
    private Memory memory;
    private String brand;

    public Phone(String brand){
        this.brand = brand;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public String getBrand() {
        return brand;
    }
}
