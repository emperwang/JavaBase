package com.wk;

import org.junit.Test;

/**
 * @author: wk
 * @Date: 2020/12/2 11:03
 * @Description
 */
public class CompareableTest {

    @Test
    public void test1(){
        Integer t1 = 5;
        Integer t2 = 6;
        System.out.printf("cmp = %d\n", t1.compareTo(t2));
    }
}
