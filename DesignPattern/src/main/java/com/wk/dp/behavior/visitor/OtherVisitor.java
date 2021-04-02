package com.wk.dp.behavior.visitor;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:24
 * @Description
 */
public class OtherVisitor implements Visitor<Book> {
    @Override
    public void visit(Book obj) {
        System.out.printf("otherVisitor, %s  图文并茂, 看起来还是很有趣的.\n", obj
        .getName());
    }
}
