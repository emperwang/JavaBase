package com.wk.dp.structural.decorate;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 14:17
 * @Description
 */
public abstract class Decorator extends Person{
    private Person person;
    public Decorator(Person person){
        this.person = person;
    }

    @Override
    public void wear() {
        this.person.wear();
        this.decorate();
    }

    abstract void decorate();
}
