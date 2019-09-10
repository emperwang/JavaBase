package com.wk.JMX.ch4;

public interface LoggerMBean {
    // 设置日志等级
    void setLogLevel(int level);
    // 获取日志等级
    int getLogLevel();
    // 检索日志
    String retrieveLog(int linesback);
    // 写日志
    void writeLog(String message,int type);
}
