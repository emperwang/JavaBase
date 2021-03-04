package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/11 13:56
 * @Description
 */
public class Solution_maxProfit {
    /*
    给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
    如果你最多只允许完成一笔交易（即买入和卖出一支股票一次），设计一个算法来计算你所能获取的最大利润。
    注意：你不能在买入股票前卖出股票。

    示例 1:
    输入: [7,1,5,3,6,4]
    输出: 5
    解释: 在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5 。
         注意利润不能是 7-1 = 6, 因为卖出价格需要大于买入价格；同时，你不能在买入前卖出股票。
     */
    /*
      1. 两个指针A,B;  A 从开始元素遍历,B从A后一个位置开始
      2. 查找B-A 差值最大的元素, 返回B 位置的索引
     */
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length <= 0) return 0;
        int max = 0;
        for (int a= 0; a<prices.length-1; a++){
            for (int b=a+1; b<prices.length;b++){
               max = Math.max(prices[b] - prices[a],max);
            }
        }
        return max;
    }

    public int maxProfit2(int[] prices){
        if (prices == null || prices.length <= 0) return 0;
        int miniProfit = Integer.MAX_VALUE;
        int max = 0;
        for (int i=0;i<prices.length;i++){
            if (prices[i] < miniProfit){
                miniProfit = prices[i];
            }else if((prices[i] - miniProfit) > max){
                max = prices[i] - miniProfit;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        final Solution_maxProfit solutionMaxProfit = new Solution_maxProfit();
        System.out.println(solutionMaxProfit.maxProfit2(new int[]{7,1,5,3,6,4}));
        System.out.println(solutionMaxProfit.maxProfit2(new int[]{1,2}));
        System.out.println(solutionMaxProfit.maxProfit2(new int[]{7,6,5,4,3,2,1}));
    }
}
