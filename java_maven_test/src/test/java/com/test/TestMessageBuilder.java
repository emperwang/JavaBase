package com.test;

import com.wk.examples.MessagesBuilder;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * @author: wk
 * @Date: 2020/12/11 16:08
 * @Description
 */
public class TestMessageBuilder {
    /*
    使用jacoco进行股覆盖时, 如果不添加test注解, 代码不会运行
     */
    @Test
    public void testName(){
        final MessagesBuilder builder = new MessagesBuilder();
        assertEquals("hello world", builder.getMessage("world"));
    }
//    @Test
//    public void testNameEmpty(){
//        final MessagesBuilder builder = new MessagesBuilder();
//        assertEquals("please provide a name", builder.getMessage(""));
//    }
//
//    @Test
//    public void testNameNUll(){
//        final MessagesBuilder builder = new MessagesBuilder();
//        assertEquals("please provide a name", builder.getMessage(null));
//    }
}
