package com.wk.jvm_instruction;

import com.wk.jvm_instruction.service.TestService;

public class InterfaceMethodInvoke {
    public static void main(String[] args) {
        TestService testService = new TestServiceImpl();
        testService.printInfo();
    }
}
