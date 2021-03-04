package com.wk.other;

/**
 * 把一个字符串中空格替换为 %20
 */
public class ReplaceSpaceInArray {

    String url = "abc def efg";

    /**
     * 1. 遍历一遍得到所有的空格数
     * 2. 创建一个新的数组 长度为原来数组的长度 + 2*空格数
     * 3. 使用两个指针分别指源数组 和新数组
     * 4. 从尾部开始遍历源数组,把指向的非空格复制到新数组, 指向空格后 把其替换为 %20 并复制到新数组
     */
    public void replaceSpace(String url){
        if (url == null || url.length() <= 0){
            return;
        }
        char[] strs = url.toCharArray();
        // 1. 遍历一遍得到空格数量
        int spct = 0;
        for (char str : strs) {
            if (str == ' '){
                spct++;
            }
        }
        System.out.printf("chars length: %d, space count = %d\n", strs.length,spct);
        // 2. 得到新的长度并创建一个新数组
        int newLength = strs.length + 2 * spct;
        char[] cnewchar = new char[newLength];
        int oriIdx = strs.length-1,newIdx = newLength-1;
        while (oriIdx >= 0 && newIdx >= 0){
            if (strs[oriIdx] != ' '){
                cnewchar[newIdx--] = strs[oriIdx--];
            }else{
                oriIdx--;
                cnewchar[newIdx--] ='0';
                cnewchar[newIdx--] ='2';
                cnewchar[newIdx--] ='%';
            }
        }
        for (char c : cnewchar) {
            System.out.printf("%s",c);
        }
    }

    /**
     * 把两个有序的数组 按照大小顺序拼接在一起,拼接后的数组仍然有序
     * 如: arr1 = {2,5,7,9} arr2 = {1,3,4,6,7}
     * 拼接后: arr = {1,2,3,4,5,6,7,9}
     */
    public int[]  addArray(int[] arr1, int[] arr2){
        if ((arr1==null || arr1.length <= 0)){
            return arr2;
        }
        if (arr2==null || arr2.length <= 0){
            return arr1;
        }
        /*
        *思路: 1.创建一个新数组 长度可以容纳两个数组
        *      2.从尾部开始遍历两个数组的值,并把大的值 赋值到 新数组的尾部
         */
        int idx1 = arr1.length-1;
        int idx2 = arr2.length-1;
        int newLen = arr1.length + arr2.length;
        int[] newArr = new int[newLen];
        newLen--;
        while (idx1 >= 0 && idx2 >= 0){
            while (idx1 >= 0 && idx2 >= 0 && arr1[idx1] > arr2[idx2]){
                newArr[newLen--] = arr1[idx1--];
            }
            while(idx1 >= 0 && idx2 >= 0 && arr1[idx1] < arr2[idx2]){
                newArr[newLen--] = arr2[idx2--];
            }
            while (idx1 >= 0 && idx2 >= 0 && arr1[idx1] == arr2[idx2]){
                newArr[newLen--] = arr2[idx2--];
                newArr[newLen--] = arr1[idx1--];
            }
        }
        while (idx1 >= 0){
            newArr[newLen--] = arr1[idx1--];
        }
        while (idx2 >= 0){
            newArr[newLen--] = arr2[idx2--];
        }
        return newArr;
    }

    public static void main(String[] args) {
        ReplaceSpaceInArray array = new ReplaceSpaceInArray();
        array.replaceSpace(array.url);
        System.out.println();
        int[] arr1 = {2,5,7,9}, arr2 = {1,3,4,6,7};
        int[] arrn = array.addArray(arr1, arr2);
        for (int i : arrn) {
            System.out.printf("%d --\t", i);
        }
    }
}
