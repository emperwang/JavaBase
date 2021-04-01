package com.wk.dp.creative.abstractfactory;

import com.wk.dp.creative.abstractfactory.shape.RedCircle;
import com.wk.dp.creative.abstractfactory.shape.RedRectangle;
import com.wk.dp.creative.abstractfactory.shape.Shape;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 10:08
 * @Description
 */
public class RedShapeFactory implements ShapeFactory{
    @Override
    public Shape getCircle() {
        System.out.println("get red circle");
        return new RedCircle();
    }

    @Override
    public Shape getRectangle() {
        System.out.println("get red rectangle");
        return new RedRectangle();
    }
}
