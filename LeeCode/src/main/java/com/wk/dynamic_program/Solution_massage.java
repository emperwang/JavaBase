package com.wk.dynamic_program;

/**
 * @author: ekiawna
 * @Date: 2021/3/15 14:40
 * @Description
 */
public class Solution_massage {
    /*
    面试题 17.16. 按摩师
一个有名的按摩师会收到源源不断的预约请求，每个预约都可以选择接或不接。在每次预约服务之间要有休息时间，
因此她不能接受相邻的预约。给定一个预约请求序列，替按摩师找到最优的预约集合（总预约时间最长），返回总的分钟数。
注意：本题相对原题稍作改动

示例 1：
输入： [1,2,3,1]
输出： 4
解释： 选择 1 号预约和 3 号预约，总时长 = 1 + 3 = 4。

示例 2：
输入： [2,7,9,3,1]
输出： 12
解释： 选择 1 号预约、 3 号预约和 5 号预约，总时长 = 2 + 9 + 1 = 12。

示例 3：
输入： [2,1,4,5,3,1,1,3]
输出： 12
解释： 选择 1 号预约、 3 号预约、 5 号预约和 8 号预约，总时长 = 2 + 4 + 3 + 3 = 12。
     */
    /*
    dp0 表示不预约的时间
    dp1 表示预约的时间
    dp0 = Max(dp[i-1][0], dp[i-1][1])
    dp1 = dp[i-1][0] + numsi
     */
    public int massage(int[] nums) {
        int n = nums.length;
        if ( n == 0)return 0;
        int dp0 = 0, dp1=nums[0];
        for (int i =1; i< n; i++){
            int tdp0 = Math.max(dp0, dp1);  // 计算 dp[i][0]
            int tdp1 = dp0 + nums[i];   // 计算dp[i][1]
            dp0 = tdp0;     // 用dp[i][0] 更新dp0
            dp1 = tdp1;     // 用dp[i][1] 更新dp1
        }
        return Math.max(dp0, dp1);
    }
}
