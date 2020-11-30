package com.wk.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author: wk
 * @Date: 2020/11/30 11:37
 * @Description
 */
public class NumSort {

    /*
        冒泡排序法
        重复从右侧开始对比临近的数字,根据结果来进行是否swap
     */
    public void BubbleSort(int[] nums){
        if (nums == null || nums.length <= 0){
            System.out.printf("Invalid array.");
            return;
        }
        /*
        (n-1) (n-2) .... 1 0
        ((n-1)+0)*n / 2 = (n^2-n) /2 ~= O(n^2)
         */
        for (int len = nums.length-1; len >= 0; len--){
            for (int i = len-1; i >= 0; i--){
                if (nums[len] < nums[i]){
                    int tmp = nums[i];
                    nums[i] = nums[len];
                    nums[len] = tmp;
                }
            }
        }
        Arrays.stream(nums).forEach(it -> System.out.println(it));
    }

    /*
    插入排序法
    将数据分为左边和右边,默认左边的数据是已经排好序的数字,右边是未排序的数字,
    从右边持续获取未排序的数字 并找到其在左边的待插入的位置,进行插入
     */
    public void InsertSort(int[] nums){
        if (nums == null || nums.length <= 0){
            System.out.printf("Invalid array.");
            return;
        }
        int left=0;
        int[] dest = {nums[left]};
        for(int rig=left+1; rig<nums.length;rig++){
            for(int idx=0;idx < dest.length; idx++ ){
                if (nums[rig] <= dest[idx]){
                    int[] tmp = new int[dest.length+1];
                    System.arraycopy(dest,0,tmp,0,idx);
                    System.arraycopy(dest,idx,tmp,idx+1,dest.length-idx);
                    tmp[idx] = nums[rig];
                    dest = tmp;
                    break;
                }else if (idx == dest.length-1){
                    int[] tmp = new int[dest.length + 1];
                    System.arraycopy(dest, 0, tmp, 0, dest.length);
                    tmp[dest.length] = nums[rig];
                    dest = tmp;
                    break;
                }
            }
        }
        Arrays.stream(dest).forEach(it -> System.out.println(it));
    }
    /*
        选择排序法
        重复从待排序的数据中寻找最小值,将其与最左边的数字进行交换
     */
    public void ChooseSort(int[] nums){
        if (nums == null || nums.length <= 0){
            System.out.printf("Invalid array.");
            return;
        }
        // 1: n-1  2: n-2 3: n-3     ((n-1)+0)*n / 2 = (n^2-n)/2
        for (int left = 0; left< nums.length; left++) {
            // find min
            int min = left;
            for (int rig = left+1; rig < nums.length; rig++) {
                if (nums[rig] <  nums[min]){
                    min = rig;
                }
            }
            if (nums[min] < nums[left]) {
                int tmp = nums[min];
                nums[min] = nums[left];
                nums[left] = tmp;
            }
        }
        Arrays.stream(nums).forEach(it -> System.out.println(it));
    }
    /*
    堆排序
    最后的非叶子节点 数组.length / 2-1
    下标为i的节点的父节点下标为: (i-1)/2
    下标为i的节点的左孩子节点下标为: 2*i + 1
    下标为i的节点的右孩子节点下标为: 2*i + 2
     大头堆
     */
    public void HeapSort(int[] nums){
        if (nums == null || nums.length <= 0){
            System.out.printf("Invalid array.");
            return;
        }
        // 建堆
        for (int i = nums.length/2-1;i >= 0; i--){
            headify(nums,i,nums.length);
        }
        // 排序
        // 把最后的叶子节点,即数组的最后一个值和堆的root节点交换
        for (int l = nums.length-1; l>=0;l--){
            int tmp = nums[l];
            nums[l] = nums[0];
            nums[0] = tmp;
            headify(nums,0,l);
        }
    }

    /**
     * @param arr 待排序的列表
     * @param i   父节点(简单说是 子树的父节点)
     * @param n    列表长度
     */
    private void headify(int[] arr, int i, int n){
        int tmp = arr[i];
        for (int k = i * 2+1; k<n; k = 2*i+1){
            // k = i*2+1 即 k是i的左孩子节点的索引
            // 如果右节点存在(i*2+2即右节点索引),且左节点小于右节点值,那把k指向右节点索引
            if ((k+1)<n && arr[k] < arr[k+1]){
                k++;
            }
            if (arr[i] < arr[k]){
                arr[i] = arr[k];
                arr[k] = tmp;
                i = k;  // 这里修改i的值,即把k指向被修改的子树的父节点
            }else{
                break;
            }
        }
    }

    /*
    快速排序
    从待排序的数组中随机选择一个值 作为中值,然后把数分为:
    [小于中值的数] 中值 [大于中值的数], 然后再对[]中的数按照此方法进行排序,直到顺序确定
     */
    public void QuickSort(int[] nums,int low, int high){
        if (nums == null || nums.length <= 0){
            return;
        }
        if (low > high){
            return;
        }
        int i=low, j = high, rad = nums[low],t;
        while (i < j){
            // 先看右边
            while (nums[j] >= rad && j > i){
                j--;
            }
            // 再看左边
            while (nums[i] <= rad && i < j){
                i++;
            }
            // 找到则交换位置
            if (i < j){
                t = nums[i];
                nums[i] = nums[j];
                nums[j] = t;
            }
        }
        nums[low] = nums[j];
        nums[j] = rad;
        // 左边
        QuickSort(nums,low,j-1);
        // 右边
        QuickSort(nums,j+1,high);
        //Arrays.stream(nums).forEach(it -> System.out.println(it));
    }

    /*
    归并排序法
    平均拆分带排序的数组,直到长度为1,然后把最后的不能拆分的数组按照大小进行合并
     */
    public void MergerSort(int[] nums){
        if (nums == null || nums.length <= 0){
            System.out.printf("Invalid array.");
            return;
        }
        sort(nums,0,nums.length-1);
        Arrays.stream(nums).forEach(i -> System.out.println(i));
    }
    public void sort(int[] nums, int low, int high){
        int mid = (low + high) /2 ;
        System.out.printf("compute val low=%d, mid = %d, high=%d\n", low, mid, high);
        if (low < high){
            // left
            System.out.printf("left low=%d, mid = %d, high=%d\n", low, mid, high);
            sort(nums,low,mid);
            // right
            sort(nums,mid+1, high);
            merage(nums,low, mid, high);
        }
    }

    public void merage(int[] a, int low, int mid,int high){
        int i=low, j=mid+1, k=0;
        int[] tmp = new int[high-low+1];
        System.out.printf("merge low=%d, mid = %d, high=%d\n", low, mid, high);
        while (i <= mid && j <= high){
            if (a[i] <= a[j]){
                tmp[k++] = a[i++];
            }else{
                tmp[k++] = a[j++];
            }
        }
        while (i <= mid){
            tmp[k++] = a[i++];
        }
        while (j<=high){
            tmp[k++] = a[j++];
        }
        //覆盖原数组
        for (int i1 = 0; i1 < tmp.length; i1++) {
            a[i1+low] = tmp[i1];
        }
    }

    public static void main(String[] args) {
        int[] nums = {9,4,5,6,3,1,6,10,12};
        final NumSort numSort = new NumSort();
        //numSort.BubbleSort(nums);
//        numSort.ChooseSort(nums);
//        numSort.InsertSort(nums);
//        numSort.QuickSort(nums,0,nums.length-1);
//        Arrays.stream(nums).forEach(it -> System.out.println(it));
        numSort.MergerSort(nums);
//        numSort.HeapSort(nums);
//        Arrays.stream(nums).forEach(it -> System.out.println(it));
    }
}
