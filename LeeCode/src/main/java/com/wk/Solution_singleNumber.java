package com.wk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * @author: wk
 * @Date: 2021/1/11 17:33
 * @Description
 */
public class Solution_singleNumber {
    /*
    给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
    说明：
    你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？
    示例 1:
    输入: [2,2,1]
    输出: 1

    示例 2:
    输入: [4,1,2,1,2]
    输出: 4
 */
    public int singleNumber(int[] nums) {
        final HashMap<Integer, Integer> map = new HashMap<>(nums.length);
        for (int num : nums) {
            if (map.containsKey(num)){
                //map.put(num,map.get(num)+1);
                map.remove(num);
            }else {
                map.put(num,1);
            }
        }
        final Set<Integer> res = map.keySet();
        int s = -1;
        for (Integer re : res) {
            s = re;
        }
        return s;
    }
    /*
     1. 数组中只有一个不是重复的,并求和
     2. 使用一个set集合添加所有出现的元素,求和的2倍
     3. 两数相减  就得出那个出现一次的数据
     */
    public int singleNumber1(int[] nums){
        final HashSet<Integer> sets = new HashSet<>();
        int sum = 0;
        for (int num : nums) {
            sets.add(num);
            sum += num;
        }
        final int sum1 = sets.stream().mapToInt(Integer::intValue).sum()*2;
        return sum1-sum;
    }
    /*
    上面算法都是用了 O(n)的空间复杂度, O(n)的时间复杂度
    下面使用异或来使用  固定的空间复杂度来解决
    1. 一个数和自身 异或 得到结果0
    2. 一个数和0异或, 得到自身

     */
    public int singleNumberXor(int[] nums){
        int res = 0;
        for (int num : nums) {
            res ^= num;
        }
        return res;
    }

    public static void main(String[] args) {
        final Solution_singleNumber solutionSingleNumber = new Solution_singleNumber();
        System.out.println(solutionSingleNumber.singleNumber(new int[]{4,1,2,1,2}));
        System.out.println(solutionSingleNumber.singleNumber1(new int[]{4,1,2,1,2}));
        System.out.println(solutionSingleNumber.singleNumberXor(new int[]{4,1,2,1,2}));
    }
}

