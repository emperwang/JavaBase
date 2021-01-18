package com.wk;

/**
 * @author: wk
 * @Date: 2021/1/14 13:42
 * @Description
 */
public class Solution_findTwoMin {
    /*
        给定一个数组,找出其中最小的两个值
        如: [1,4,5,7,2,4]
        返回:[1,0,2,4]    // 最小的两个值为1和2,下标分别为0,4
     */
    public int[] find2min(int[] nums){
       if (nums == null || nums.length < 2) return null;
       int m1=Integer.MAX_VALUE,m2=Integer.MAX_VALUE,idx1=-1,idx2=-1;  // 最下值 和 下标
        for (int i=0; i < nums.length;i++) {
            if (nums[i] < m1){
                m2 = m2;
                m1 = nums[i];
                idx2=idx1;
                idx1 = i;
            }else if (nums[i] < m2){
                m2 = nums[i];
                idx2 = i;
            }
        }
        return new int[]{m1,idx1,m2,idx2};
    }

    public void printArray(int[] arr){
        for (int i : arr) {
            System.out.print(i+" ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        final Solution_findTwoMin solutionFindTwoMin = new Solution_findTwoMin();
        solutionFindTwoMin.printArray(solutionFindTwoMin.find2min(new int[]{1,4,5,7,2,4}));
    }
}
