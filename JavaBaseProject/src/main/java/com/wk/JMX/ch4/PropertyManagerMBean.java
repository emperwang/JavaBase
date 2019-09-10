package com.wk.JMX.ch4;

import java.util.Enumeration;

public interface PropertyManagerMBean {
    // 获取key对应的value
    String getProperty(String key);
    // 设置props
    void setProperty(String key,String value);
    // 获取配置文件的key
    Enumeration keys();
    // 设置配置的源
    void setSource(String path);
}
