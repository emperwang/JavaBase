package com.wk;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Sparks
 * @Date: 2022/9/15 13:50
 * @Description
 */
public class test_logback {

    @Test
    public void testQuickly(){
        Logger logger = LoggerFactory.getLogger(test_logback.class);
        logger.error("error msg");
        logger.warn("warn msg");
        logger.debug("debug msg");
        logger.info("info msg");
    }

    @Test
    public void testQuick2(){

    }
}
