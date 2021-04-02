package com.wk.dp.behavior.visitor;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:22
 * @Description
 */
public class Book {
    private String name;
    public Book(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
