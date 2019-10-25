package com.wk.asm.aop3.test;

public class StudentInfo {

    public void printStuInfo(){
        System.out.println("print studnet info....");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
