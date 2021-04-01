package com.wk.dp.creative.abstractfactory;

import com.wk.dp.creative.abstractfactory.shape.Shape;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 10:07
 * @Description
 */
public interface ShapeFactory {
    Shape getCircle();
    Shape getRectangle();
}
