package com.wk.dp.creative.abstractfactory;

import com.wk.dp.creative.abstractfactory.shape.BlueCircle;
import com.wk.dp.creative.abstractfactory.shape.BlueRectangle;
import com.wk.dp.creative.abstractfactory.shape.Shape;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 10:11
 * @Description
 */
public class BlueShapeFactory implements ShapeFactory {
    @Override
    public Shape getCircle() {
        System.out.println("Get blue circle");
        return new BlueCircle();
    }

    @Override
    public Shape getRectangle() {
        System.out.println("get blue rectangle");
        return new BlueRectangle();
    }
}
