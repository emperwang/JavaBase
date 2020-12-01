package com.wk.search;

/**
 * @author: wk
 * @Date: 2020/12/1 10:12
 * @Description
 */
public class SearchNum {

    public int BinarySearchNormal(int[] nums, int toFind){
        if (nums == null || nums.length <= 0){
            return -1;
        }
        int low = 0;
        int high = nums.length-1;
        int mid = (low + high) /2;
        while (low <= high){
            if (nums[mid] < toFind){
                low = mid+1;
            }else if (nums[mid] == toFind){
                return mid;
            } else if (nums[mid] > toFind){
                high = mid - 1;
            }
            mid = (low + high) /2;
        }
        return -1;
    }
    public int BinarySearch(int[] nums, int toFind,int low, int high){
        if (nums == null || nums.length <= 0 || low > high){
            return -1;
        }
        int mid = (low+high)/2;
        if (nums[mid] == toFind){
            return mid;
        }else if(nums[mid] > toFind){
            return BinarySearch(nums,toFind,low,mid-1);
        }else if(nums[mid] < toFind){
            return BinarySearch(nums,toFind,mid+1, high);
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] nums = {1,2,3,4,5,6,7,8,9,10,11,12,15};
        final SearchNum searchNum = new SearchNum();
        final int i = searchNum.BinarySearchNormal(nums, 20);
        System.out.printf("i = %d\n", i);
        final int i1 = searchNum.BinarySearchNormal(nums, 12);
        System.out.printf("i = %d, val=%d\n",i1, nums[i1]);
        System.out.println("=========================================");
        final int i2 = searchNum.BinarySearch(nums, 20, 0, nums.length - 1);
        System.out.printf("i2=%d\n", i2);
        final int i3 = searchNum.BinarySearch(nums, 15, 0, nums.length - 1);
        System.out.printf("i3=%d,val=%d\n",i3, nums[i3]);
    }
}
