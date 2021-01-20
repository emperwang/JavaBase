package com.wk;

/**
 * @author: wk
 * @Date: 2021/1/20 13:07
 * @Description
 */
public class Solution_isPowerOfTwo {
    /*
    给定一个整数，编写一个函数来判断它是否是 2 的幂次方。
        示例 1:
        输入: 1
        输出: true
        解释: 2^0 = 1

        示例 2:
        输入: 16
        输出: true
        解释: 2^4 = 16

        示例 3:
        输入: 218
        输出: false
     */
    /*
    若 n=2^x  且 x 为自然数（即 n 为 2 的幂），则一定满足以下条件：
    恒有 n & (n - 1) == 0，这是因为：
       1.  n 二进制最高位为 1，其余所有位为 0；
            n−1 二进制最高位为 0，其余所有位为 1；
       2. 一定满足 n > 0。
     */
    public boolean isPowerOfTwo(int n) {
        return (n>0) && (n&(n-1))==0;
    }

    public static void main(String[] args) {
        final Solution_isPowerOfTwo solutionIsPowerOfTwo = new Solution_isPowerOfTwo();
        System.out.println(solutionIsPowerOfTwo.isPowerOfTwo(1));
        System.out.println(solutionIsPowerOfTwo.isPowerOfTwo(2));
        System.out.println(solutionIsPowerOfTwo.isPowerOfTwo(4));
        System.out.println(solutionIsPowerOfTwo.isPowerOfTwo(10));
    }
}
