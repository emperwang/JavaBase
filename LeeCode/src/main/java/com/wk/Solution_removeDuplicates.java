package com.wk;

/**
 * @author: wk
 * @Date: 2021/1/7 14:00
 * @Description
 */
public class Solution_removeDuplicates {
    /*
    给定一个排序数组，你需要在 原地 删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
    不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。

    示例 1:
    给定数组 nums = [1,1,2],
    函数应该返回新的长度 2, 并且原数组 nums 的前两个元素被修改为 1, 2。
    你不需要考虑数组中超出新长度后面的元素。
     */
    public int removeDuplicates1(int[] nums) {
        if (nums == null || nums.length<=0){
            return 0;
        }
        int length = nums.length;
        int idx = nums[0];
        for (int i=1;i<length;i++){
            if (idx == nums[i]){
                length -= 1;
                // 重操作
                System.arraycopy(nums,i+1,nums,i,nums.length-i-1);
                i--;
            }else {
                idx = nums[i];
            }
        }
        return length;
    }
    /*
    使用两个指针,一个指针指向数组中不重复的数的末尾,一个指针用于遍历数组
    O(n)
     */
    public int removeDuplicates(int[] nums) {
        if (nums == null || nums.length<=0){
            return 0;
        }
        int i=0;
        for (int j=1;j<nums.length;j++){
           if (nums[j] != nums[i]){
               i++;
           }
           nums[i] = nums[j];
        }
        return i+1;
    }

    public static void main(String[] args) {
        final Solution_removeDuplicates removeDuplicates = new Solution_removeDuplicates();
        //System.out.println(removeDuplicates.removeDuplicates1(new int[]{2,2,3,3,4}));
        System.out.println(removeDuplicates.removeDuplicates(new int[]{2,2,3,3,4}));
    }
}
