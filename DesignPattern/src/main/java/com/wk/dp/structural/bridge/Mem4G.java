package com.wk.dp.structural.bridge;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 17:27
 * @Description
 */
public class Mem4G implements Memory {
    private final String mem = "4g";
    @Override
    public String getMem() {
        return mem;
    }
}
