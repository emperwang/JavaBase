package com.wk.jvm_instruction;

public class JVMInvokeInstace {

    public static void main(String[] args) {
        InstanceForTest instanceForTest = new InstanceForTest();
        instanceForTest.printMsg("this is instance invoke");
    }
}
