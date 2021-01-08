package com.wk;

import java.util.Arrays;

/**
 * @author: wk
 * @Date: 2021/1/8 17:06
 * @Description
 */
public class Solution_merge {
    /*
    给你两个有序整数数组 nums1 和 nums2，请你将 nums2 合并到 nums1 中，使 nums1 成为一个有序数组。
    说明：
        初始化 nums1 和 nums2 的元素数量分别为 m 和 n 。
        你可以假设 nums1 有足够的空间（空间大小大于或等于 m + n）来保存 nums2 中的元素。

    示例：
    输入：
    nums1 = [1,2,3,0,0,0], m = 3
    nums2 = [2,5,6],       n = 3
    输出：[1,2,2,3,5,6]

    提示：
        -10^9 <= nums1[i], nums2[i] <= 10^9
        nums1.length == m + n
        nums2.length == n
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        final int[] res = new int[m];
        System.arraycopy(nums1,0,res,0,m);
        int i=0,j=0,k=0;
        while (i< res.length && j < nums2.length){
            if (res[i] >= nums2[j]){
                nums1[k++] = nums2[j++];
            }else{
                nums1[k++] = res[i++];
            }
        }
        while (i<res.length){
            nums1[k++] = res[i++];
        }
        while (j<nums2.length){
            nums1[k++] = nums2[j++];
        }
        printArray(nums1);
    }
    // 双指针,从后往前比
    public void merge1(int[] nums1, int m, int[] nums2, int n) {
        int p1 = m-1;  //nums1 最后一个有效元素
        int p2 = n-1;   // nums2 最后一个有效元素
        int p3 = nums1.length-1;  // 指向 nums1的最后位置

        while (p2 >= 0 && p1>=0){
            if (nums2[p2] >= nums1[p1]){
                nums1[p3--] = nums2[p2--];
            }else{
                nums1[p3--] = nums1[p1--];
            }
        }
        if (p2 >= 0){
            System.arraycopy(nums2,0,nums1,0,p3+1);
        }
        if (p1 >= 0){
            System.arraycopy(nums1,0,nums1,0,p3+1);
        }
        printArray(nums1);
    }

    public void merge2(int[] nums1, int m, int[] nums2, int n) {
        System.arraycopy(nums2,0,nums1,m,n);
        Arrays.sort(nums1);
        printArray(nums1);
    }

    public void printArray(int[] it){
        for (int i : it) {
            System.out.print(i +"  ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        final Solution_merge solutionMerge = new Solution_merge();
        final int[] ints = {2,0};
        final int[] ints1 = {1};
        solutionMerge.merge(ints,ints.length-1,ints1,ints1.length);

    }
}
