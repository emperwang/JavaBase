package com.wk.dp.behavior.visitor;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:27
 * @Description
 */
public class Main {

    public static void main(String[] args) {
        EngineerVisitor engineerVisitor = new EngineerVisitor();
        StudentVisitor studentVisitor = new StudentVisitor();
        OtherVisitor otherVisitor = new OtherVisitor();
        Book book = new Book("java实战");
        VisitorManager visitorManager = new VisitorManager(book);
        visitorManager.addVisitor(engineerVisitor).addVisitor(studentVisitor).addVisitor(otherVisitor);

        visitorManager.action();
    }
}
