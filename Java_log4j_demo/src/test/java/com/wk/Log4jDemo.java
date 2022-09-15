package com.wk;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Sparks
 * @Date: 2022/9/15 6:50
 * @Description
 */
public class Log4jDemo {

    @Test
    public void testQuick(){
        Logger logger = LoggerFactory.getLogger(Log4jDemo.class);

        logger.error("error msg");
        logger.warn("warn msg");
        logger.debug("debug msg");
        logger.info("info msg");
    }


    @Test
    public void testQuick2(){
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Log4jDemo.class);

        logger.error("error msg");
        logger.warn("warn msg");
        logger.info("info msg");
        logger.debug("debug msg");
        logger.trace("trace msg");
    }
}
