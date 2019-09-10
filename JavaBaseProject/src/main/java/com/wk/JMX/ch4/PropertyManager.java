package com.wk.JMX.ch4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class PropertyManager implements PropertyManagerMBean {
    // 存储配置的容器
    private Properties props = null;

    // 构造器初始化容器
    public PropertyManager(String path){
        props = new Properties();

        try {
            FileInputStream stream = new FileInputStream(path);
            props.load(stream);

            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getProperty(String key) {
        return props.getProperty(key);
    }

    @Override
    public void setProperty(String key, String value) {
        props.setProperty(key,value);
    }

    @Override
    public Enumeration keys() {
        Enumeration<Object> keys = props.keys();
        return keys;
    }

    @Override
    public void setSource(String path) {
        props = new Properties();

        try {
            FileInputStream inputStream = new FileInputStream(path);
            props.load(inputStream);

            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
