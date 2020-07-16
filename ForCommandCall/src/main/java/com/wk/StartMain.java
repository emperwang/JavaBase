package com.wk;

public class StartMain {
    public static void main(String[] args) {
        if (args == null || args.length <= 0){
            System.out.println("No parameter");
            return;
        }

        for (String arg : args) {
            System.out.println("args : " + arg);
        }
    }
}
