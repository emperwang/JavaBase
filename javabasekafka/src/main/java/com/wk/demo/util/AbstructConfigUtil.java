package com.wk.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class AbstructConfigUtil {

    /**
     * 编解码使用的格式
     */
    private static final String UTF_8 = "utf-8";

    private static final String TRUE_STR = "true";
    /**
     * 配置文件
     */
    private String propertiesFile = "";
    /**
     * 加载配置文件
     */
    private PropertiesConfiguration properties;
    /**
     * 是否已经加载
     */
    private boolean isSucess = false;

    public AbstructConfigUtil() {
        initializaConfig();
    }

    /**
     * 加载配置文件
     *
     * @return
     */
    private synchronized boolean initializaConfig() {
        if (isSucess) {
            return isSucess;
        }
        try {
            propertiesFile = URLDecoder.decode(getPropertiesFilePath(), this.UTF_8);
            properties = new PropertiesConfiguration(propertiesFile);
            FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
            strategy.setConfiguration(properties);
            properties.setReloadingStrategy(strategy);
            properties.setAutoSave(true);
            properties.setEncoding(this.UTF_8);
            log.info("Load properties file from {}.", propertiesFile);
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException in AbstractConfigUtil.initializeConfig()",
                    e);
        } catch (ConfigurationException e) {
            log.error("ConfigurationException in AbstractConfigUtil.initializeConfig()",
                    e);
        }
        isSucess = properties != null;
        return isSucess;
    }

/*    private String getPropertiesFilePath() {
        // String path = ClassLoader.getSystemResource(getPropertiesFileName()).getPath();
        String path = System.getProperty("config.path");
        log.info("get class path is:"+path);
        return path;
    }*/

    public abstract String getPropertiesFilePath();

    public abstract String getPropertiesFileName();

    /**
     * 返回key对应的值
     *
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        if (!isSucess) {
            log.info("Properties not initialize yet,start to initialize");
            initializaConfig();
        }

        String value = properties.getString(key);
        if (value == null) {
            isSucess = false;
            log.info("!!!Configuration:<{}, NULL>", key);
        }else{
            value = value.trim();
        }
        log.info("Configuration:<{},{}>", key, value);
        return value;
    }

    /**
     * 获取key的v值，如果不存在，则返回默认值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getStringValue(String key, String defaultValue) {
        if (!isSucess) {
            log.info("Properties not initialize yet,start to initialize");
            initializaConfig();
        }
        String value = properties.getString(key);
        log.info("configuration:<{},{}>", key, value);
        if (value == null || value.trim().equals("")) {
            return defaultValue;
        }
        return value.trim();
    }

    /**
     * 获取key对应的值，其value是多个
     *
     * @param key
     * @return
     */
    public List<String> getStringList(String key) {
        if (!isSucess) {
            log.info("Properties not initialize yet,start to initialize");
            initializaConfig();
        }
        List<String> strList = new ArrayList<>();
        String[] values = properties.getStringArray(key);
        if (values == null) {
            isSucess = false;
            log.info("!!!Configuration:<{}, NULL>", key);
            return strList;
        }
        log.info("configuration:<{},{}>", key, values);
        strList.addAll(Arrays.asList(values));
        return strList;
    }

    /**
     *  返回int类型的值
     * @param xpath
     * @return
     */
    public int getIntValue(String xpath) {
        int result = 0;
        try {
            result = Integer.parseInt(getStringValue(xpath));
        } catch (final NumberFormatException e) {
            isSucess = false;
            log.error("!!!Not integer value for : " + xpath
                    + ". Return default integer value 0.");
        }
        return result;
    }

    /**
     *  返回int类型的值
     * @param key
     * @param defaultValue
     * @return
     */
    public int getIntValue(String key, int defaultValue) {
        String str = getStringValue(key);
        if (str == null || str.trim().equals("")) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException ex) {
            log.error(
                    "!!!Not integer value for : " + key
                            + ". Return default integer value {}.",
                    defaultValue);
            return defaultValue;
        }
    }

    /**
     *  返回long类型的value
     * @param key
     * @return
     */
    public long getLongValue(String key) {
        long result = 0;
        try {
            result = Long.parseLong(getStringValue(key));
        } catch (final NumberFormatException e) {
            isSucess = false;
            log.error("!!!Not Long value for : " + key
                    + ". Return default long value 0.");
        }
        return result;
    }
}
