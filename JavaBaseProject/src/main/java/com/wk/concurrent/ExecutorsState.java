package com.wk.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorsState {
    private static int ctlOf(int rs, int wc) { return rs | wc; }
    public static void main(String[] args) {
         Integer COUNT_BITS = Integer.SIZE - 3;
         Integer CAPACITY   = (1 << COUNT_BITS) - 1;

        // runState is stored in the high-order bits
         Integer RUNNING    = -1 << COUNT_BITS;     // 00
         Integer SHUTDOWN   =  0 << COUNT_BITS;     //
         Integer STOP       =  1 << COUNT_BITS;
         Integer TIDYING    =  2 << COUNT_BITS;
         Integer TERMINATED =  3 << COUNT_BITS;
        AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

        System.out.println(COUNT_BITS);
        System.out.println(Integer.toBinaryString(CAPACITY));
        System.out.println(Integer.toBinaryString(RUNNING));
        System.out.println(Integer.toBinaryString(SHUTDOWN));
        System.out.println(Integer.toBinaryString(STOP));
        System.out.println(Integer.toBinaryString(TIDYING));
        System.out.println(Integer.toBinaryString(TERMINATED));
        System.out.println(Integer.toBinaryString(ctl.get()));

    }
}
