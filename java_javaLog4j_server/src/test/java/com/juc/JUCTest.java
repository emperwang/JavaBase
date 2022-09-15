package com.juc;

import org.junit.Test;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author: Sparks
 * @Date: 2022/9/11 18:32
 * @Description
 */
public class JUCTest {

    @Test
    public void test1(){
        // 1. get logger
        Logger logger = Logger.getLogger(JUCTest.class.getName());
        ConsoleHandler handler = new ConsoleHandler();
        logger.addHandler(handler);

        logger.setLevel(Level.ALL);

        logger.severe("severe msg");
        logger.warning("warning message");
        logger.info("info message");
        logger.config("config message");
        logger.fine("fine message");
        logger.finer("finer message");
    }


}
