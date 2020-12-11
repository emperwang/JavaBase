package com.wk.priority;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * @author: wk
 * @Date: 2020/12/8 11:31
 * @Description
 */
public class PriorityQueueUser extends TestCase {

    public PriorityQueueUser(String method){
        super(method);
    }

    public void test(){
        final PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.addAll(Arrays.asList(1,3,5,6,7,8,9));
        queue.add(4);
    }

    public void test2(){
        assertTrue(3>2);
        //fail("fail message");
        //assertEquals(5,10);
        // message:  输出的信息
        // expected: 预期值
        // actual:  实际的值
        // delta:  比较浮点数时 指定误差值,误差内都认为是相等的
        assertEquals("compare float and specific delate",0.3, 1.0/3.0,0.1);
    }

    public void test3(){
        assertEquals("test3 for suit",5,5);
    }

    public static Test suite(){
        final TestSuite suite = new TestSuite();
        suite.addTest(new PriorityQueueUser("test2"));
        suite.addTest(new PriorityQueueUser("test3"));
        return suite;
    }
}
