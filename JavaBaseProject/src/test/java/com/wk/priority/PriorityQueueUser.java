package com.wk.priority;

import org.junit.Test;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * @author: wk
 * @Date: 2020/12/8 11:31
 * @Description
 */
public class PriorityQueueUser {
    @Test
    public void test(){
        final PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.addAll(Arrays.asList(1,3,5,6,7,8,9));
        queue.add(4);
    }
}
