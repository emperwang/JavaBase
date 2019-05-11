package com.wk.proxy.CglibProxy;

import com.wk.proxy.API.MathImpl;

public class TestMain {
    public static void main(String[] args) {
        CglibProxy cglibProxy = new CglibProxy();
        MathImpl proxyObject = (MathImpl) cglibProxy.getProxyObject(new MathImpl());
        proxyObject.add(1,2);
    }
}
