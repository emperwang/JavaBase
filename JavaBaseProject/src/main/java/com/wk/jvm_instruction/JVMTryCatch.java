package com.wk.jvm_instruction;

public class JVMTryCatch {
    public static void main(String[] args) {

        try{
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
