package com.wk;

import org.junit.Test;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 9:55 2020/1/3
 * @modifier:
 */
public class IntervalCode {

    /**
     *  code point
     *  code unit
     *  一个完整的字符是一个code point;
     *  一个code point可以对应1到2个code unit;一个code unit是16位
     */
    @Test
    public void test1(){
        String cc = "\uD834\uDD1E";
        String bb = "\uD834\uDD1E";
        System.out.println(bb);
        System.out.println(bb.length());
        System.out.println(cc.length());
        System.out.println(cc.codePointCount(0,cc.length()));
    }

    @Test
    public void test2(){
        String str1 = "/ftproot/GD/HX/ERICSSON/GZ_OMC2/FM/20190725/FM-OMC-1A-V1.0.0-20190725170530-001.txt";
        String res = "/ftproot/GD/HX/ERICSSON/GZ_OMC2/FM/20190725/FM-OMC-1A-V1.0.0-20190725170530-001.txt";

        System.out.println(str1.equals(res));

    }
}
