package com.wk.dp.behavior.template;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 15:39
 * @Description
 */
public abstract class AbstractFunctionAlg implements FunctionAlg {

    @Override
    public void getResult() {
        step1();
        step2();
        step3();
        step4();
    }
}
