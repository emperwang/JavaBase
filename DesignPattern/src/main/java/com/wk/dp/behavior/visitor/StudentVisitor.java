package com.wk.dp.behavior.visitor;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:23
 * @Description
 */
public class StudentVisitor implements Visitor<Book> {

    @Override
    public void visit(Book obj) {
        System.out.printf("Student, %s 理论实践并行, 对学习帮助很大. \n", obj.getName());
    }
}
