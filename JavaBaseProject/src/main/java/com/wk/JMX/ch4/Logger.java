package com.wk.JMX.ch4;

import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Logger implements LoggerMBean,MBeanRegistration {
    // Field
    private int logLevel = Logger.ALL;

    // level
    public static final int ALL = 3;
    public static final int ERRORS = 2;
    public static final int NONE = 1;

    // 具体的打印函数
    private PrintWriter out = null;

    private MBeanServer server = null;
    // 在构造函数初始化 打印函数
    public Logger(){
        try {
            out = new PrintWriter(new FileOutputStream("record.log"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setLogLevel(int level) {
        this.logLevel = level;
    }

    @Override
    public int getLogLevel() {
        return logLevel;
    }

    @Override
    public String retrieveLog(int linesback) {
        // 检索日志文件中的具体的行
        return null;
    }

    @Override
    public void writeLog(String message, int type) {
        if (type <= logLevel){
            out.println(message);
        }
    }

    @Override
    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        this.server = server;

        ObjectName objectName = new ObjectName("JMXBookAgent:name=property");
        Object[] params = {"loglevel"};

        String[] sig = {"java.lang.String"};

        String value = (String) server.invoke(objectName, "getProperty", params, sig);
        logLevel = Integer.parseInt(value);
        return name;
    }

    @Override
    public void postRegister(Boolean registrationDone) {

    }

    @Override
    public void preDeregister() throws Exception {

    }

    @Override
    public void postDeregister() {

    }
}
