package com.wk;

import org.apache.logging.log4j.LogManager;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Sparks
 * @Date: 2022/9/15 13:53
 * @Description
 */
public class testLog4j2 {

    @Test
    public void testquick(){
        Logger logger = LoggerFactory.getLogger(testLog4j2.class);
        logger.error("error msg");
        logger.warn("warn msg");
        logger.debug("debug msg");
        logger.info("info msg");
    }


    @Test
    public void testQuick(){
        org.apache.logging.log4j.Logger logger = LogManager.getLogger(testLog4j2.class);

        logger.error("error msg");
        logger.warn("warn msg");
        logger.debug("debug msg");
        logger.info("info msg");
    }
}
