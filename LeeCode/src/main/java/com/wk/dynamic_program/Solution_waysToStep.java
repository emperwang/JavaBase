package com.wk.dynamic_program;

/**
 * @author: ekiawna
 * @Date: 2021/3/15 17:54
 * @Description
 */
public class Solution_waysToStep {
    /*
    面试题 08.01. 三步问题
三步问题。有个小孩正在上楼梯，楼梯有n阶台阶，小孩一次可以上1阶、2阶或3阶。实现一种方法，
计算小孩有多少种上楼梯的方式。结果可能很大，你需要对结果模1000000007。
示例1:
 输入：n = 3
 输出：4
 说明: 有四种走法

示例2:
 输入：n = 5
 输出：13
     */
    /*
        f(1) = [{1} ] = 1
        f(2) = [{1,1}, {2}] = 2
        f(3) = [{1,1,1},{1,2},{2,1},{3}] = 4
        f(i) = f(i-1) + f(i-2) + f(i-3)
     */
    public int waysToStep(int n) {
        if (n >= 3){
            int d1 = 1;
            int d2 = 2;
            int d3 = 4;
            int base = 1000000007;
            while (--n >= 3){
                int t1 = d2;
                int t2 = d3;
                d3 = (((d1+d2)%base) + d3)%base;
                d1 = t1;
                d2 = t2;
            }
            return d3;
        }else {
            return n;
        }
    }

    public static void main(String[] args) {
        Solution_waysToStep step = new Solution_waysToStep();
        System.out.println(step.waysToStep(3));
    }
}
