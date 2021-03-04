package com.wk.other;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author: wk
 * @Date: 2021/1/18 10:35
 * @Description
 */
public class Solution_containsDuplicate {
    /*
    给定一个整数数组，判断是否存在重复元素。
    如果存在一值在数组中出现至少两次，函数返回 true 。如果数组中每个元素都不相同，则返回 false 。

    示例 1:
    输入: [1,2,3,1]
    输出: true

    示例 2:
    输入: [1,2,3,4]
    输出: false
     */
    public boolean containsDuplicate(int[] nums) {
        final HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            set.add(nums[i]);
        }
        return set.size()==nums.length?false:true;
    }

    public boolean containsDuplicate2(int[] nums) {
        final long count = Arrays.stream(nums).distinct().count();
        System.out.println(count);
        return count==nums.length?false:true;
    }
    public static void main(String[] args) {
        final Solution_containsDuplicate containsDuplicate = new Solution_containsDuplicate();
        System.out.println(containsDuplicate.containsDuplicate2(new int[]{1,2,3,1}));
    }
}
