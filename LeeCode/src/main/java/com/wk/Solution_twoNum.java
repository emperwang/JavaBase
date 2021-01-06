package com.wk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: wk
 * @Date: 2021/1/6 14:33
 * @Description
 */
/*
给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 的那 两个 整数，并返回它们的数组下标。
你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
你可以按任意顺序返回答案。

示例 1：
输入：nums = [2,7,11,15], target = 9
输出：[0,1]
解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。

 */
public class Solution_twoNum {
    public static int[] nums = {2,7,11,15};
    public static int target = 9;
    public int[] twoSum(int[] nums, int target) {
        if (nums == null || nums.length<=0)return null;
        // 暴力破解  O(n^2)
        for (int i=0; i<nums.length;i++){
            for (int j=0; j<nums.length;j++){
                if (i == j) continue;
                if (nums[i] + nums[j] == target){
                    return new int[] {i,j};
                }
            }
        }
        return null;
    }

    public int[] twoSum2(int[] nums, int target) {
        if (nums == null || nums.length<=0)return null;
        /*
        把数组中的数据 放入到 hashmap中,这样查找 为O(1), 遍历nums.length个数
         */
        // key为nums的值, value为 index
        Map<Integer,Integer> map = new HashMap<>();
        for (int i =0;i<nums.length;i++){
            if (map.containsKey(target-nums[i])){
                return new int[] {map.get(target-nums[i]),i};
            }else{
                map.putIfAbsent(nums[i],i);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        final Solution_twoNum num = new Solution_twoNum();
        System.out.println(Arrays.toString(num.twoSum(nums,target)));

        System.out.println(Arrays.toString(num.twoSum2(nums,target)));
    }
}
