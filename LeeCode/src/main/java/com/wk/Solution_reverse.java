package com.wk;

/**
 * @author: wk
 * @Date: 2021/1/6 17:45
 * @Description
 */
public class Solution_reverse {

    /*
    给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。
示例 1:
    输入: 123
    输出: 321
     */
    /*

    为了便于解释，我们假设 rev 是正数。
    如果 temp=rev*10+pop导致溢出,那么一定有 rev >= INTMAX/10
    如果 rev>INTMAX/10,那么 temp=rev*10+pop一定会溢出
    如果 rev==INTMAX/10,那么只要pop>7 , temp=rev*10+pop 一定会溢出
    最大值: 2147483647, 那么最后当pop>7时, 会溢出
    最小值: -2147483648, 那么当最后pop<-8 时,会溢出
     */
    // 溢出返回0
    public int reverse(int x) {
        int rev = 0;
        while (x != 0){
            int pop = x % 10;
            x/=10;
            // 这是两个溢出的情况
            if (rev > Integer.MAX_VALUE/10 || (rev == Integer.MAX_VALUE/10 && pop>7)) return 0;
            if (rev <Integer.MIN_VALUE/10 || (rev == Integer.MIN_VALUE/10 && pop < -8)) return 0;
            rev = rev*10+pop;
        }
        return rev;
    }

    public static void main(String[] args) {
        final Solution_reverse reverse = new Solution_reverse();
        System.out.println(reverse.reverse(123));
//        int tp = 1234;
//        System.out.println(tp % 10);
//        tp = tp/10;
//        System.out.println(tp % 10);
//        tp = tp/10;
//        System.out.println(tp % 10);
//        tp = tp/10;
//        System.out.println(tp % 10);
//
//        int tmax= Integer.MAX_VALUE;
//        tp = tmax/10;
//        System.out.println(Integer.MAX_VALUE);
//        System.out.println(Integer.MIN_VALUE);
//        System.out.println(tp);
//        System.out.println(tp+7);
    }
}
