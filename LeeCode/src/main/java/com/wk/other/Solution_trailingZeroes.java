package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/12 15:01
 * @Description
 */
public class Solution_trailingZeroes {
    /*
    给定一个整数 n，返回 n! 结果尾数中零的数量。
    示例 1:
    输入: 3
    输出: 0
    解释: 3! = 6, 尾数中没有零。

    示例 2:
    输入: 5
    输出: 1
    解释: 5! = 120, 尾数中有 1 个零.

    说明: 你算法的时间复杂度应为 O(log n) 。

    看题解: 找其中的因子5的个数
     */
    public int trailingZeroes(int n) {
        int count = 0;
        int factor = 5;
        while (n > 0){
            n /= factor;
            count += n;
        }
        return count;
    }

    public int trailingZeroes2(int n) {
        int count = 0;
        int factor = 5;
        while (n >= factor){
            count += (n/factor);
            factor *= 5;
        }
        return count;
    }

    public static void main(String[] args) {
        final Solution_trailingZeroes solutionTrailingZeroes = new Solution_trailingZeroes();
        System.out.println(solutionTrailingZeroes.trailingZeroes(5));
        System.out.println(solutionTrailingZeroes.trailingZeroes2(5));
    }
}
