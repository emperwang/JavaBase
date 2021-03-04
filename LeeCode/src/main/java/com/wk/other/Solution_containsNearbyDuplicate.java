package com.wk.other;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: wk
 * @Date: 2021/1/18 10:41
 * @Description
 */
public class Solution_containsNearbyDuplicate {
    /*
    给定一个整数数组和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，使得 nums [i] = nums [j]，
    并且 i 和 j 的差的 绝对值 至多为 k。
    示例 1:
    输入: nums = [1,2,3,1], k = 3
    输出: true

    示例 2:
    输入: nums = [1,0,1,1], k = 1
    输出: true
     */
    /*
    线性查找
    即 使用查找窗口为k, 在这个窗口中 查找是否有相等的
     */
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        if (nums == null || nums.length <= 0) return false;
        // 此时间复杂度: O(n) * O(min(n,k))
        for (int i=0; i<nums.length;i++){
            // 可见在这里对窗口中的数据 进行比对时  耗费比较长的时间
            // 优化方案 也主要是优化这里
            // 如果这里的查找 使用二叉树,则
            // 时间复杂度: O(n) * O(log(min(n,k)))
            for (int j=Math.max(i-k,0);j<i;j++){
                if (nums[j] == nums[i]) return true;
            }
        }
        return true;
    }
    public boolean containsNearbyDuplicate2(int[] nums, int k) {
        if (nums == null || nums.length <= 0) return false;
        // 维持此容器的中的数据数量为 k
        Set<Integer> sets = new HashSet<>();
        // O(n)
        for (int i=0;i<nums.length; i++){
            if (sets.contains(nums[i])) return true;
            sets.add(nums[i]);
            if (sets.size() > k){
                sets.remove(nums[i-k]);
            }
        }
        return false;
    }
    public static void main(String[] args) {
        final Solution_containsNearbyDuplicate solutionContainsNearbyDuplicate = new Solution_containsNearbyDuplicate();
        System.out.println(solutionContainsNearbyDuplicate.containsNearbyDuplicate2(new int[]{1,2,3,1},3));
        System.out.println(solutionContainsNearbyDuplicate.containsNearbyDuplicate2(new int[]{1,0,1,1},1));
    }
}
