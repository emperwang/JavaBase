package com.wk.JMX;

/**
 *  MBean的一个实现类
 *  名字必须是去掉MBean后  的名字
 */
public class Msg implements MsgMBean {
    private String message;
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String msg) {
        this.message = msg;
    }
}
