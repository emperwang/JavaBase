package com.wk;

/**
 * @author: wk
 * @Date: 2021/1/8 13:44
 * @Description
 */
public class Solution_sqrt {
    /*
    实现 int sqrt(int x) 函数。
    计算并返回 x 的平方根，其中 x 是非负整数。
    由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。

    示例 1:
    输入: 4
    输出: 2

    示例 2:
    输入: 8
    输出: 2
    说明: 8 的平方根是 2.82842...,
     由于返回类型是整数，小数部分将被舍去。
     aqrt(x) = x^1/2 = e^lnx*1/2
     */
    public int mySqrt(int x) {
        if (x < 0) return -1;
        // exp() 方法用于返回自然数底数e的参数次方
        int res = (int)Math.exp(0.5 * Math.log(x));
        return res;
    }

    public static void main(String[] args) {
        final Solution_sqrt solutionSqrt = new Solution_sqrt();
        System.out.println(solutionSqrt.mySqrt(4));
        System.out.println(solutionSqrt.mySqrt(9));
    }
}
