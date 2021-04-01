package com.wk.dp.structural.flyweight;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 16:51
 * @Description
 */
// 颜料盒, 共用
public abstract class PigmentBox {
    private ColorEnum color;
    private boolean inUser = false;

    public PigmentBox(ColorEnum color){
        this.color = color;
    }


    public void setUse(){
        inUser = true;
    }

    public void release(){
        inUser = false;
    }

    public boolean isInUser(){
        return inUser;
    }

    public void setColor(ColorEnum color) {
        this.color = color;
    }

    public ColorEnum getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "PigmentBox{" +
                "color=" + color +
                ", inUser=" + inUser +
                '}';
    }
}
