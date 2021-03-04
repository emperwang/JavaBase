package com.wk.other;

import java.util.Arrays;

/**
 * @author: wk
 * @Date: 2021/1/18 9:45
 * @Description
 */
public class Solution_countPrimes {
    /*
    统计所有小于非负整数 n 的质数的数量。
    示例 1：
    输入：n = 10
    输出：4
    解释：小于 10 的质数一共有 4 个, 它们是 2, 3, 5, 7 。

    示例 2：
    输入：n = 0
    输出：0

    示例 3：
    输入：n = 1
    输出：0

    质数的定义：在大于 111 的自然数中，除了 111 和它本身以外不再有其他因数的自然数
     */
    /*
    如果 y 是 x 的因数，那么 x/y​ 也必然是 x 的因数，因此我们只要校验 y 或者 x/y 即可。而如果我们每次选择校验两者中的较小数，
    则不难发现较小数一定落在 [2,\sqrt{x}]的区间中，因此我们只需要枚举[2,\sqrt{x}]
    中的所有数即可，这样单次检查的时间复杂度从O(n) 降低至了 O(\sqrt{n})
     */
    public int countPrimes(int n) {
        int count = 0;
        for (int i = 2; i<n;i++){
            if (isPrimes(i)) count++;
        }
        return count;
    }

    public boolean isPrimes(int n){
        for (int i=2; i*i <= n; i++){
            if ((n % i) == 0) return false;
        }
        return true;
    }

    public int countPrimes2(int n) {
        final int[] isPrimes = new int[n];
        Arrays.fill(isPrimes,1);
        int count = 0;
        for (int i = 2; i<n;i++){
            if (isPrimes[i]==1){
                count++;
                for (int j=i*i; j<n;j+=i){
                    isPrimes[j] = 0;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        final Solution_countPrimes solutionCountPrimes = new Solution_countPrimes();
//        System.out.println(solutionCountPrimes.isPrimes(2));
//        System.out.println(solutionCountPrimes.isPrimes(3));
//        System.out.println(solutionCountPrimes.isPrimes(4));
//        System.out.println(solutionCountPrimes.isPrimes(5));
        System.out.println(solutionCountPrimes.countPrimes(10));
        System.out.println(solutionCountPrimes.countPrimes2(10));
    }
}
