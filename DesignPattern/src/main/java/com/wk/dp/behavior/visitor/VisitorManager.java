package com.wk.dp.behavior.visitor;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:28
 * @Description
 */
public class VisitorManager {
    private List<Visitor> visitors;
    private Book target;

    public VisitorManager(Book target){
        visitors = new ArrayList<>();
        this.target = target;
    }

    public VisitorManager addVisitor(Visitor visitor){
        this.visitors.add(visitor);
        return this;
    }

    public VisitorManager removeVisitor(Visitor visitor){
        this.visitors.remove(visitor);
        return this;
    }

    public void action(){
        visitors.forEach(visitor -> {
            target.accept(visitor);
        });
    }
}

