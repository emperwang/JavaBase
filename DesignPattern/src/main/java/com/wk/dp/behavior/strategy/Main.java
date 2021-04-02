package com.wk.dp.behavior.strategy;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 9:43
 * @Description
 */
public class Main {
    public static void main(String[] args) {
        Strategy walkStrategy = new WalkStrategy();
        Strategy subwayStrategy = new SubwayStrategy();
        Strategy taxiStrategy = new TaxiStrategy();
        Person person = new Person(walkStrategy,"zhangsan");
        person.outStyle();
    }
}
