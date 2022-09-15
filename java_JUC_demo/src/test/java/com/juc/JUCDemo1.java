package com.juc;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Sparks
 * @Date: 2022/9/15 6:38
 * @Description
 */
public class JUCDemo1 {

    // use slf4j api
    @Test
    public void testQuick(){
        Logger logger = LoggerFactory.getLogger(JUCDemo1.class);

        logger.error("error msg");
        logger.warn("warn msg");
        logger.info("info msg");
        logger.debug("debug msg");
    }

    // use original juc api
    @Test
    public void  testQuick2(){
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JUCDemo1.class.getName());

        logger.severe("severe msg");
        logger.warning("warn msg");
        logger.info("info msg");
        logger.fine("fine msg");
    }
}
