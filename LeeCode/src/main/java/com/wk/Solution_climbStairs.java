package com.wk;

/**
 * @author: wk
 * @Date: 2021/1/8 16:43
 * @Description
 */
public class Solution_climbStairs {
    /*
    假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
    每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
    注意：给定 n 是一个正整数。

    示例 1：
    输入： 2
    输出： 2
    解释： 有两种方法可以爬到楼顶。
    1.  1 阶 + 1 阶
    2.  2 阶
     */
    public int climbStairs(int n) {
        int p = 0,q=0,r=1;
        for (int i=0;i<n;i++){
            p = q;
            q = r;
            r = p+q;
        }
        return r;
    }

    public int climbStairs1(int n){
        final int[] ints = new int[n + 1];
        ints[0] = 1;
        ints[1] = 1;
        for (int i=2; i<=n;i++){
            ints[i] = ints[i-1] + ints[i-2];
        }
        return ints[n];
    }
    // 递归
    public int climbStairs2(int n){
        if (n == 0 || n == 1){
            return 1;
        }
        return climbStairs2(n-1)+climbStairs2(n-2);
    }

    // 菲波那切数列
    // 台阶   0   1   2   3   4   5   6
    // 数量   1   1   2   3   5   8   13
    public static void main(String[] args) {
        final Solution_climbStairs solutionClimbStairs = new Solution_climbStairs();
        System.out.println(solutionClimbStairs.climbStairs(45));
        System.out.println(solutionClimbStairs.climbStairs2(45));
    }
}
