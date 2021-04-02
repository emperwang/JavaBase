package com.wk.dp.behavior.visitor;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:24
 * @Description
 */
public class EngineerVisitor implements Visitor<Book> {
    @Override
    public void visit(Book obj) {
        System.out.printf("Engineer, %s 内容详实, 通俗易懂, 可见作者功力之深. \n", obj.getName());
    }
}
