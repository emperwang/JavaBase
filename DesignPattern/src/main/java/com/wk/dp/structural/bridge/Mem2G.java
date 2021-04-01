package com.wk.dp.structural.bridge;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 17:26
 * @Description
 */
public class Mem2G implements Memory{
    private final String mem = "2g";

    @Override
    public String getMem() {
        return mem;
    }
}
