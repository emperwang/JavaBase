package com.wk.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author: wk
 * @Date: 2020/12/10 17:04
 * @Description
 */
public class TestTwo extends TestCase {

    public TestTwo(String method){
        super(method);
    }
    /*
    setUp 可以事先准备一下环境
    tearDown 运行完成后 释放资源
     */
    @Override
    protected void setUp() throws Exception {

    }

    @Override
    protected void tearDown() throws Exception {

    }

    public void test1(){
        System.out.println("test two test2");
        assertTrue(3>2);
        //fail("fail message");
        //assertEquals(5,10);
        // message:  输出的信息
        // expected: 预期值
        // actual:  实际的值
        // delta:  比较浮点数时 指定误差值,误差内都认为是相等的
        assertEquals("test two  compare float and specific delate",0.3, 1.0/3.0,0.1);
    }

    public void test2(){
        System.out.println("test two test2");
        assertEquals("test two test2 for suit",5,5);
    }
    public static Test suit(){
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestTwo("test1"));
        return suite;
    }
}
