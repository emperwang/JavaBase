package com.wk.dp.structural.decorate;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 14:35
 * @Description
 */
public class LeatherDecorate extends Decorator {

    public LeatherDecorate(Person person) {
        super(person);
    }

    @Override
    void decorate() {
        System.out.println("一双休闲皮鞋");
    }
}
