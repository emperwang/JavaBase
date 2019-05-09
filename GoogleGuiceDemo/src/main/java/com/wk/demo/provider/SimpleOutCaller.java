package com.wk.demo.provider;

import com.google.inject.Inject;

public class SimpleOutCaller {

    @Inject
    private static SimpleOutCaller simpleOutCaller;

    @Inject
    private SimpleOut simpleOut;

    public void testSimpleOut(){
        simpleOut.sayOut();
    }

    public SimpleOut getSimpleOut() {
        return simpleOut;
    }
}
