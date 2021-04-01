package com.wk.dp.structural.decorate;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 14:18
 * @Description
 */
public class TShirtDecorate extends Decorator {
    public TShirtDecorate(Person person) {
        super(person);
    }

    @Override
    void decorate() {
        System.out.println("一件纯棉T shirt");
    }
}
