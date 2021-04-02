package com.wk.dp.behavior.strategy;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 9:40
 * @Description
 */
public class Person {
    private Strategy strategy;
    private String name;
    public Person(Strategy strategy, String name){
        this.strategy = strategy;
        this.name = name;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void outStyle(){
        System.out.println(name+" the way to the destination: ");
        strategy.outStyle();
    }
}
