package com.wk.proxy.CglibProxy;

import com.wk.proxy.API.MathImpl;
import net.sf.cglib.core.DebuggingClassWriter;

public class TestMain {
    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "target/cglib");
        CglibProxy cglibProxy = new CglibProxy();
        MathImpl proxyObject = (MathImpl) cglibProxy.getProxyObject(new MathImpl());
        proxyObject.add(1,2);
    }
}
