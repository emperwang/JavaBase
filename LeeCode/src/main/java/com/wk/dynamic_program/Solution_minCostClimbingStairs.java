package com.wk.dynamic_program;

/**
 * @author: ekiawna
 * @Date: 2021/3/15 17:22
 * @Description
 */
public class Solution_minCostClimbingStairs {
    /*
    746. 使用最小花费爬楼梯
数组的每个下标作为一个阶梯，第 i 个阶梯对应着一个非负数的体力花费值 cost[i]（下标从 0 开始）。
每当你爬上一个阶梯你都要花费对应的体力值，一旦支付了相应的体力值，你就可以选择向上爬一个阶梯或者爬两个阶梯。
请你找出达到楼层顶部的最低花费。在开始时，你可以选择从下标为 0 或 1 的元素作为初始阶梯。

示例 1：
输入：cost = [10, 15, 20]
输出：15
解释：最低花费是从 cost[1] 开始，然后走两步即可到阶梯顶，一共花费 15 。

 示例 2：
输入：cost = [1, 100, 1, 1, 1, 100, 1, 1, 100, 1]
输出：6
解释：最低花费方式是从 cost[0] 开始，逐个经过那些 1 ，跳过 cost[3] ，一共花费 6 。
     */
    /*
      楼顶对应下标n, 问题等价于计算达到下标n的最小花费
      由于可以选择下标0 或 1 作为初始阶梯, 因此有 dp[0] = dp[1] = 0
      当 2<= i <= n时,可以从下标i-1使用cost[i-1]的花费达到下标i, 或者从下标i-2使用cost[i-2]的花费达到下标i,
      状态转移方程式:
      dp[i] = min(dp[i-1]+cost[i-1],  dp[i-2]+cost[i-2])
     */
    public int minCostClimbingStairs(int[] cost) {
        int n = cost.length;
        int[] dp = new int[n + 1];
        dp[0] = dp[1] = 0;

        for (int i = 2; i<= n; i++){
            dp[i] = Math.min(dp[i-1]+cost[i-1], dp[i-2]+cost[i-2]);
        }
        return dp[n];
    }
    /*
    从上面计算可知,其实 dp[i]只和 dp[i-1] dp[i-2] 有关系, 所以可以使用两个变量来记录 dp[i-1] dp[i-2]的值
     */

    public int minCostClimbingStairs2(int[] cost) {
        int pre = 0, cur = 0;
        for (int i = 2; i<= cost.length; i++){
            int next = Math.min(pre+cost[i-2], cur+cost[i-1]);
            pre = cur;
            cur = next;
        }
        return cur;
    }

    public static void main(String[] args) {
        Solution_minCostClimbingStairs stairs = new Solution_minCostClimbingStairs();
        int[] nums = {10, 15 ,20};
        int[] nums1 = {1, 100, 1, 1, 1, 100, 1, 1, 100, 1};
        System.out.printf(stairs.minCostClimbingStairs2(nums1) + "");
    }
}
