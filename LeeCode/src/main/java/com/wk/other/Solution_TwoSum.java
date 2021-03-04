package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/12 13:01
 * @Description
 */
public class Solution_TwoSum {
    /*
    给定一个已按照升序排列 的有序数组，找到两个数使得它们相加之和等于目标数。
    函数应该返回这两个下标值 index1 和 index2，其中 index1 必须小于 index2。

    说明:
        返回的下标值（index1 和 index2）不是从零开始的。
        你可以假设每个输入只对应唯一的答案，而且你不可以重复使用相同的元素。
    示例:
    输入: numbers = [2, 7, 11, 15], target = 9
    输出: [1,2]
    解释: 2 与 7 之和等于目标数 9 。因此 index1 = 1, index2 = 2 。
     */
    /*
     依据题目 有序的升序数组
     那么此处使用双指针法
     */
    public int[] twoSum(int[] numbers, int target) {
        if (numbers == null || numbers.length<=0) return null;
        int pre = 0;
        int post = numbers.length-1;
        if (numbers[pre] > target) return null;
        while (pre < post){
            if ((numbers[pre]+numbers[post]) < target){
                pre++;
            }else if((numbers[pre]+numbers[post]) > target){
                post--;
            }else{
                return new int[]{pre+1,post+1};
            }
        }
        return null;
    }

    public static void main(String[] args) {
        final Solution_TwoSum solutionTwoSum = new Solution_TwoSum();
        final int[] ints = solutionTwoSum.twoSum(new int[]{2, 7, 11, 15}, 9);
        for (int anInt : ints) {
            System.out.print(anInt+" ");
        }
    }
}
