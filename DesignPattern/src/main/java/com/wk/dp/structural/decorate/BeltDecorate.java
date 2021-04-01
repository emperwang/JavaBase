package com.wk.dp.structural.decorate;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 14:34
 * @Description
 */
public class BeltDecorate extends Decorator {
    public BeltDecorate(Person person) {
        super(person);
    }

    @Override
    void decorate() {
        System.out.println("一条银色针扣头的腰带");
    }
}
