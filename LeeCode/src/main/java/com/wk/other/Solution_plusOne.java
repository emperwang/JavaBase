package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/8 13:05
 * @Description
 */
public class Solution_plusOne {
    /*
    给定一个由 整数 组成的 非空 数组所表示的非负整数，在该数的基础上加一。
    最高位数字存放在数组的首位， 数组中每个元素只存储单个数字。
    你可以假设除了整数 0 之外，这个整数不会以零开头。

    示例 1：
    输入：digits = [1,2,3]
    输出：[1,2,4]
    解释：输入数组表示数字 123。

    1 <= digits.length <= 100
    0 <= digits[i] <= 9

     */
    public int[] plusOne(int[] digits) {
        if (digits == null || digits.length <=0){
            return digits;
        }
        //从后往前推,最后一位加1,如果需要进位,则处理前一位
        int len = digits.length;
        for (int i = len-1;i>=0;i--){
            int plast = digits[i]+1;
            if (plast>=10){
                digits[i] = plast %10;
                if (i==0){
                    final int[] ints = new int[digits.length+1];
                    //System.arraycopy(digits,0,ints,1,digits.length-1);
                    ints[0] = 1;
                    return ints;
                }
            }else{
                digits[i] = plast;
                return digits;
            }
        }
        return null;
    }

    public int[] plusOne1(int[] digits) {
        if (digits == null || digits.length<=0){
            return digits;
        }
        for (int i=digits.length-1; i>=0;i--){
            digits[i]++;
            digits[i] = digits[i] % 10;
            if (digits[i] != 0) return digits;
        }
        digits = new int[digits.length+1];
        digits[0]=1;
        return digits;
    }

    public void printArray(int[] ints){
        for (int anInt : ints) {
            System.out.print(anInt);
            System.out.print("  ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        final Solution_plusOne solutionPlusOne = new Solution_plusOne();
        solutionPlusOne.printArray(solutionPlusOne.plusOne1(new int[]{1, 2, 3}));
        solutionPlusOne.printArray(solutionPlusOne.plusOne1(new int[]{9,9}));
    }
}
