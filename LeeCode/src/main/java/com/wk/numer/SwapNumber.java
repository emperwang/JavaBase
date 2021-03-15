package com.wk.numer;

/**
 * @author: Sparks
 * @Date: 2021/3/13 17:21
 * @Description
 */
public class SwapNumber {
    /*
     不借助第三方变量  来交换两个变量的值
     */

    public void swapPlus(int a, int b){
        a += b;
        b = a - b;
        a = a - b;
    }

    public void swapMulti(int a, int b){
        a = a * b;
        b = a / b;
        a = a/ b;
    }

    public void swapByte(int a, int b){
        a = a ^ b;
        b = a ^ b;
        a = a^b;
    }

    public static void main(String[] args) {

    }
}
