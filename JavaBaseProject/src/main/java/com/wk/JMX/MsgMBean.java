package com.wk.JMX;

/**
 *  向外暴露的MBean
 *  名字必须是以MBean结尾
 */
public interface MsgMBean {

    String getMessage();

    void setMessage(String msg);
}
