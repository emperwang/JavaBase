package com.wk.jvm_instruction;

import com.wk.jvm_instruction.service.TestService;

public class TestServiceImpl implements TestService {

    @Override
    public void printInfo() {
        System.out.println("this is interface instance ...");
    }
}
