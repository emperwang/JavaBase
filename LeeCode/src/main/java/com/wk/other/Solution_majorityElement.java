package com.wk.other;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wk
 * @Date: 2021/1/12 14:02
 * @Description
 */
public class Solution_majorityElement {
    /*
    给定一个大小为 n 的数组，找到其中的多数元素。多数元素是指在数组中出现次数 大于 ⌊ n/2 ⌋ 的元素。
    你可以假设数组是非空的，并且给定的数组总是存在多数元素。

    示例 1：
    输入：[3,2,3]
    输出：3
    进阶：
    尝试设计时间复杂度为 O(n)、空间复杂度为 O(1) 的算法解决此问题。
     */
    // 时间O(2n)  空间: O(n/2-1)
    public int majorityElement(int[] nums) {
        int mid = nums.length/2;
        final HashMap<Integer, Integer> map = new HashMap<>(nums.length);
        for (int num : nums) {
            if (map.containsKey(num)){
                map.put(num,map.get(num)+1);
            }else{
                map.put(num,1);
            }
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue()>mid){
                return entry.getKey();
            }
        }
        return -1;
    }
    // 空间复杂度为 O(1)  时间O(n)
    public int majorityElement2(int[] nums) {
        int count = 0;
        int candidate = nums[0];
        for (int num:nums){
            if (count == 0){
                candidate = num;
            }
            count += (num==candidate?1:-1);
        }
        return candidate;
    }

    public static void main(String[] args) {
        final Solution_majorityElement solutionMajorityElement = new Solution_majorityElement();
        System.out.println(solutionMajorityElement.majorityElement2(new int[]{3,2,3,1}));
        System.out.println(solutionMajorityElement.majorityElement(new int[]{3,2,3,}));
    }
}
