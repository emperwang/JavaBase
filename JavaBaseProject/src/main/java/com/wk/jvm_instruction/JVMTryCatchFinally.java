package com.wk.jvm_instruction;

public class JVMTryCatchFinally {
    public static void main(String[] args) {
        int getvalue = getvalue();
        System.out.println("value is :"+getvalue);
    }

    public static int getvalue(){
        int a = 1;
        try{
            a += 1;
            Thread.sleep(100);
            return a;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            a += 2;
//            return a;
        }
        return a;
    }
}
