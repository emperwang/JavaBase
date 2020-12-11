package com.wk.test;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.StringTokenizer;

/**
 * @author: wk
 * @Date: 2020/12/10 17:04
 * @Description
 */
public class TestOne extends TestCase {
    public TestOne(String method){
        super(method);
    }


    public void pendingtest1(){
        System.out.println("test one test1");
        assertTrue(3>2);
        //fail("fail message");
        //assertEquals(5,10);
        // message:  输出的信息
        // expected: 预期值
        // actual:  实际的值
        // delta:  比较浮点数时 指定误差值,误差内都认为是相等的
        assertEquals("test one  compare float and specific delate",0.3, 1.0/3.0,0.1);
    }

    public void test2(){
        System.out.println("test one test2");
        assertEquals("test one test2 for suit",5,5);
    }

    public void testStringToken(){
        String str = "this is test for token";
        final StringTokenizer tokenizer = new StringTokenizer(str);
        while (tokenizer.hasMoreTokens()){
            System.out.printf("token: %s \t", tokenizer.nextToken());
        }
    }

    public static Test suit(){
        final TestSuite suite = new TestSuite();
        //suite.addTest(new TestOne("test1"));
        suite.addTest(new TestOne("test2"));
        /*
         对测试的包装,设置了 setup和tearDown函数
         */
        final TestSetup wrapper = new TestSetup(suite) {
            @Override
            protected void setUp() throws Exception {
                super.setUp();
            }

            @Override
            protected void tearDown() throws Exception {
                super.tearDown();
            }
        };
        return wrapper;
    }

    public static void oneTimeSetUp(){

    }

    public static void oneTimeTearDown(){

    }
}
