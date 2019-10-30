package com.wk.jvm_instruction;

public class JVMInvokeStatic {
    public static void main(String[] args) {
        String out="123";
        System.out.println("output is: "+out);
        InstanceForTest.printInfo();
    }
}
