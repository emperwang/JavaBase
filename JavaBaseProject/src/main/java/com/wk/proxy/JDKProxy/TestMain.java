package com.wk.proxy.JDKProxy;

import com.wk.proxy.API.MathCalc;
import com.wk.proxy.API.MathImpl;

public class TestMain {
    public static void main(String[] args) {
        JDKProxy jdkProxy = new JDKProxy();
        MathCalc mathCalc = (MathCalc) jdkProxy.getProxyObject(new MathImpl());
        mathCalc.add(1,2);
    }
}
