package com.wk.JMX.ch3.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanException;

public class ExceptionUtil {
    private static final Logger log = LoggerFactory.getLogger(ExceptionUtil.class);
    public static void printException(Exception e){
        StringBuffer buffer = new StringBuffer();
        Exception exce = null;
        log.info("--------------[ Exception ]----------------");
        e.printStackTrace();

        if (e instanceof MBeanException){
            boolean hasEmbeddedExceptions = true;
            Exception embeddException = e;
            while (hasEmbeddedExceptions){
                embeddException = ((MBeanException)embeddException).getTargetException();
                log.info("---------[ Embeded  Exception ]------------------");
                embeddException.printStackTrace();
                if (!(embeddException instanceof MBeanException)){
                    hasEmbeddedExceptions = false;
                }
            }
        }
    }
}
