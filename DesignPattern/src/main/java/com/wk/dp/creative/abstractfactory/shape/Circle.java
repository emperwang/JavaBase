package com.wk.dp.creative.abstractfactory.shape;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 10:01
 * @Description
 */
public abstract class Circle implements Shape {

    @Override
    public String getType() {
        return "circle";
    }

    public abstract void draw();
}
