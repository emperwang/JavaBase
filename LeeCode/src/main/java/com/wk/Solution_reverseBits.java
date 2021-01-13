package com.wk;

/**
 * @author: wk
 * @Date: 2021/1/13 15:52
 * @Description
 */
public class Solution_reverseBits {
    /*
    颠倒给定的 32 位无符号整数的二进制位。
    示例 1：
    输入: 00000010100101000001111010011100
    输出: 00111001011110000010100101000000
    解释: 输入的二进制串 00000010100101000001111010011100 表示无符号整数 43261596，
         因此返回 964176192，其二进制表示形式为 00111001011110000010100101000000。
     */
    /*
      第i位的二进制位 翻转后的位置为: 32-i(索引从1开始)
                                    31-i (索引从0开始)
     */
    public long reverseBits(long n){
        System.out.println(Long.toBinaryString(n));
        long ret = 0;
        long power = 31;
        while (n > 0){
            ret += ((n & 1)<<power);
            n >>= 1;
            power -= 1;
        }
        System.out.println(Long.toBinaryString(ret));
        return ret;
    }

    public int reverseBits1(int n){

        return 0;
    }
    public byte reverseByte(byte bts){

      //  return (bts* 0x0202020202 & 0x010884422010) % 1023;
        return 0;
    }

    public int reverseBits2(int n){

        return 0;
    }

    public static void main(String[] args) {
        final Solution_reverseBits solutionReverseBits = new Solution_reverseBits();
        ///System.out.println(solutionReverseBits.reverseBits(10));
        //System.out.println(solutionReverseBits.reverseBits(Integer.MAX_VALUE));
        //System.out.println(Integer.MAX_VALUE);
        System.out.println(solutionReverseBits.reverseBits(37777777775l));
    }                                                      //2147483647
}
