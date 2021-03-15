package com.wk.dynamic_program;

/**
 * @author: ekiawna
 * @Date: 2021/3/15 14:44
 * @Description
 */
public class Solution_maxSubArray {
    /*
    53. 最大子序和
给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。

示例 1：
输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
输出：6
解释：连续子数组 [4,-1,2,1] 的和最大，为 6 。

示例 2：
输入：nums = [1]
输出：1

示例 3：
输入：nums = [0]
输出：0

示例 4：
输入：nums = [-1]
输出：-1

示例 5：
输入：nums = [-100000]
输出：-100000
     */
    /*
     使用 f(i) 表示第i个索引位置的最大值,其状态转换方程式为:
     f(i) = Max(f(i-1), nums[i])
     这里使用pre来表示 f(i-1)
     */
    public int maxSubArray(int[] nums) {
        int pre = 0, fi = nums[0];
        for (int i=0; i < nums.length;i++){
            pre = Math.max(pre+nums[i],nums[i]);
            fi = Math.max(pre, fi);
        }
        return fi;
    }

    public static void main(String[] args) {
        int[] nums = {-2,1,-3,4,-1,2,1,-5,4};
        int[] nums1 = {-1};
        Solution_maxSubArray subArray = new Solution_maxSubArray();
        int i = subArray.maxSubArray(nums);
        System.out.println(i);
    }
}
