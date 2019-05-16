package com.wk.beans;

import org.springframework.beans.factory.FactoryBean;

public class ColorFactoryBean implements FactoryBean<Color> {
    //具体的创建方法
    @Override
    public Color getObject() throws Exception {
        return new Color();
    }
    //创建的类型
    @Override
    public Class<?> getObjectType() {
        return Color.class;
    }
    //是否是单例
    @Override
    public boolean isSingleton() {
        return true;
    }
}
