package com.wk.PrintUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 10:10 2020/1/10
 * @modifier:
 */
@Slf4j
public class TestBit {

    public static void main(String[] args) {
//        IntSize();
        testPoolBit();
    }

    public static void testPoolBit(){
        // 获取要移动的位数, 高3位表示状态,低29位表示数量
        int COUNT_BITS = Integer.SIZE - 3;
        int CAPACITY   = (1 << COUNT_BITS) - 1;
        log.info("COUNT_BITS :{},CAPACITY:{} ",COUNT_BITS,CAPACITY);
        // 测试移位操作
        log.info("<<1 :{}", 1<<1);
        log.info("<<2 :{}", 1<<2);
        log.info("<<3 :{}", 1<<3);
        log.info("<<4 :{}", 1<<4);
        log.info("<<5 :{}", 1<<5);
        log.info("-------------------------");
        log.info("<<1 :{}", -1<<1);
        log.info("<<2 :{}", -1<<2);
        log.info("<<3 :{}", -1<<3);
        log.info("<<4 :{}", -1<<4);
        log.info("<<5 :{}", -1<<5);
        // runState is stored in the high-order bits
        // 表示具体状态的值
        /**
         * RUNNING:-536870912,SHUTDOWN:0,STOP:536870912,TIDYING:1073741824,TERMINATED:1610612736
         *
         * -1<<29  RUNNING
         1010 0000 0000 0000 0000 0000 0000 0000    :
         1101 1111 1111 1111 1111 1111 1111 1111    : 反码
         1110 0000 0000 0000 0000 0000 0000 0000    : 补码, 最终表示方法

         0<<29  SHUTDOWN
         0000 0000 0000 0000 0000 0000 0000 0000

         1<<29  STOP
         0010 0000 0000 0000 0000 0000 0000 0000

         2<<29  tidying(整理)
         0100 0000 0000 0000 0000 0000 0000 0000

         3<<29  terminated
         1100 0000 000 0000 0000 0000 0000 0000
         */
        int RUNNING    = -1 << COUNT_BITS;
        int SHUTDOWN   =  0 << COUNT_BITS;
        int STOP       =  1 << COUNT_BITS;
        int TIDYING    =  2 << COUNT_BITS;
        int TERMINATED =  3 << COUNT_BITS;
        log.info("RUNNING:{},SHUTDOWN:{},STOP:{},TIDYING:{},TERMINATED:{} ",RUNNING,SHUTDOWN,STOP,TIDYING,TERMINATED);

        int ctlof = ctlOf(RUNNING, 1);
        log.info("ctlof :{}", ctlof);

        AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
        int c = ctl.get();
        int rs = runStateOf(c);
        log.info("runState:{}, running:{}", rs, rs==RUNNING);

        int count = workerCountOf(c);
        log.info("workerCount:{}", count);
    }

    private static int runStateOf(int c)     {
        int COUNT_BITS = Integer.SIZE - 3;
        int CAPACITY   = (1 << COUNT_BITS) - 1;
        return c & ~CAPACITY;
    }
    private static int workerCountOf(int c)  {
        int COUNT_BITS = Integer.SIZE - 3;
        int CAPACITY   = (1 << COUNT_BITS) - 1;
        return c & CAPACITY;
    }
    private static int ctlOf(int rs, int wc) {
        int COUNT_BITS = Integer.SIZE - 3;
        int CAPACITY   = (1 << COUNT_BITS) - 1;
        return rs | wc;
    }

    public static void IntSize(){
        Integer intSize = Integer.SIZE;
        Integer ctl = intSize - 3;
        System.out.println("intSize :" + intSize +" , ctl :" + ctl);
        System.out.println(intSize.byteValue());
        octToBin(intSize);
        octToBin(ctl);
    }

    public static String octToBin(int size){
        String a = "";
        while (size != 0){
            a = size%2 + a;
            size = size/2;
        }
        System.out.println(a);
        return a;
    }
}
