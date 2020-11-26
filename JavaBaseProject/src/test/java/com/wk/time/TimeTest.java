package com.wk.time;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author: wk
 * @Date: 2020/11/26 10:04
 * @Description
 */
public class TimeTest {

    public void tests(){
        {
            Arrays.asList(1,3,5,7,9).stream().forEach(t->{
                System.out.println(t);
            });
        }
        {
            System.out.println("--------------------------------------------------------------");
            Arrays.asList("A","B","C","D").stream().forEach(t -> {
                System.out.println(t);
            });
        }
        {
            System.out.println("--------------------------------------------------------------");
            Arrays.asList("Q","W","E","R").stream().forEach(t -> {
                System.out.println(t);
            });
        }
        {
            System.out.println("--------------------------------------------------------------");
            Arrays.asList("Z","X","C","V").stream().forEach(t -> {
                System.out.println(t);
            });
        }
    }

    @Test
    public void test1(){
        tests();
//        System.out.println(Long.MAX_VALUE >>1);
//        System.out.println(TimeUnit.SECONDS.toNanos(-5));
    }
}
