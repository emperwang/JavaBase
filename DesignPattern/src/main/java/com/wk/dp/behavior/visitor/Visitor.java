package com.wk.dp.behavior.visitor;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:21
 * @Description
 */
public interface Visitor<T> {
    void visit(T obj);
}
