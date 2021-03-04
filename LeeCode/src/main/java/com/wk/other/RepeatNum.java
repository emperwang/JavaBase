package com.wk.other;

/**
 * @author: wk
 * @Date: 2020/10/19 10:39
 * @Description
 */


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
* 在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，
 * 也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。
 *
 * 示例 1：
 *
 * 输入：
 * [2, 3, 1, 0, 2, 5, 3]
 * 输出：2 或 3
*/
public class RepeatNum {
    static int[] nums = {2, 3, 1, 0, 2, 5, 3};
    /*
    查找数组中任意重复数字,则遍历一把数组,key为数组值,value为出现次数,当第一次出现重复时,返回
    时间复杂度: O(n)
    空间复杂度: O(n)  创建了一个 maps集合
     */
    public static void findRepeatNUm(){
        Map<Integer, Integer> maps = new HashMap<>();
        for (int num : nums) {
            if (!maps.containsKey(num)){
                maps.put(num,1);
            }else{
                System.out.println("first repeat: " + num);
                maps.put(num,maps.get(num)+1);
                break;
            }
        }
    }
    /*
    使用set集合来存储数组中的值,当添加失败后,表示已经存在
    时间复杂度: O(n)
    空间复杂度: O(n)
     */
    public static void findRepeatNum2(){
        Set<Integer> sets = new HashSet<>();
        for (int num : nums) {
            if (!sets.add(num)){
                System.out.println("first repeat set: " + num);
                break;
            }
        }
    }

    public static void main(String[] args) {
        findRepeatNUm();
        findRepeatNum2();
    }
}
