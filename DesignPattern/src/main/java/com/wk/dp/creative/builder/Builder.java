package com.wk.dp.creative.builder;

/**
 * @author: ekiawna
 * @Date: 2021/3/31 17:12
 * @Description
 */
public class Builder {

    /*
        具有多个创建对象的builder,根据传递的参数不同,来构建同步的对象
        此方法和factory区别是: builder强调的是对象的创建过程; 而factory强调的是直接就创建好对象,直接看结果,不重视过程
     */

    public void buildCar(){
        Car car = new Car();
        car.addComponent(new Component("轮子"));
        car.addComponent(new Component("发动机"));
        car.addComponent(new Component("外壳"));
        car.showInfo();
    }

    public void buildHouse(){
        House house = new House();
        house.addComponent(new Component("墙体"));
        house.addComponent(new Component("地板"));
        house.addComponent(new Component("家具"));
        house.showInfo();
    }
}
