package com.wk.dp.creative.abstractfactory.shape;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 10:03
 * @Description
 */
public abstract class Rectangle implements Shape {
    @Override
    public String getType() {
        return "rectangle";
    }
    public abstract void draw();
}
