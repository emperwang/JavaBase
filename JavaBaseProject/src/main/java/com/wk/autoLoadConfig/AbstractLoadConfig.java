package com.wk.autoLoadConfig;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public abstract class AbstractLoadConfig {

    private static final Logger log = LoggerFactory.getLogger(AbstractLoadConfig.class);

    private static final String UTF_8 = "UTF-8";

    // 配置文件路径
    private String propertiesFile = "";

    // 加载的配置文件
    private PropertiesConfiguration propertiesConfiguration;

    private boolean isLoaded = false;

    public AbstractLoadConfig(){
        initializaConfig();
    }

    public abstract String getPropertiesFilePath();
    /**
     *  加载配置文件
     */
    private boolean initializaConfig() {
        if (isLoaded){
            return isLoaded;
        }
        try {
            propertiesFile = URLDecoder.decode(getPropertiesFilePath(), UTF_8);
            propertiesConfiguration = new PropertiesConfiguration(propertiesFile);
            FileChangedReloadingStrategy reloadingStrategy = new FileChangedReloadingStrategy();
            reloadingStrategy.setConfiguration(propertiesConfiguration);
            propertiesConfiguration.setReloadingStrategy(reloadingStrategy);
            propertiesConfiguration.setAutoSave(true);
            propertiesConfiguration.setEncoding(UTF_8);
            log.info("Load properties file from :" + propertiesFile);
        } catch (UnsupportedEncodingException e) {
            log.error("initializaConfig UnsupportedEncodingException,error msg is:"+e.getMessage());
            e.printStackTrace();
        } catch (ConfigurationException e) {
            log.error("initializaConfig ConfigurationException ,error msg:"+e.getMessage());
            e.printStackTrace();
        }
        isLoaded = propertiesConfiguration != null;

        return isLoaded;
    }

    /**
     *   获取key对应的值
     * @param key
     * @return
     */
    public String getStringValue(String key){
        if (!isLoaded){
            log.info("Properties not initialize yet,start to load config");
            initializaConfig();
        }

        String value = propertiesConfiguration.getString(key);
        if (value == null){
            isLoaded = false;
            log.info("Configuration :<{},NULL>",key);
        }

        log.info("Configuration:<{},{}>",key,value);
        value = value.trim();
        return value;
    }
}
