package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/11 14:42
 * @Description
 */
public class Solution_maxProfitII {
    /*
    给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
    设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
    注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。

    示例 1:
    输入: [7,1,5,3,6,4]
    输出: 7
    解释: 在第 2 天（股票价格 = 1）的时候买入，在第 3 天（股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5-1 = 4 。
         随后，在第 4 天（股票价格 = 3）的时候买入，在第 5 天（股票价格 = 6）的时候卖出, 这笔交易所能获得利润 = 6-3 = 3 。
     */
    /*
    相当于是每次从数组中取两个连续的数, 把其大于0的差值相加
     */
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length <= 0) return 0;
        int max = 0;
        for (int pre=0,post=1;post<prices.length;){
            int preVal = prices[pre];
            int posVal = prices[post];
            if (preVal < posVal){
                max += posVal-preVal;
            }
            pre += 1;
            post+=1;
        }
        return max;
    }
    /*
        动态规划:
        考虑到[不能同时参与多笔交易],因此每天交易结束后只可能存在手里有一只股票或者没有股票的状态.
        定义状态 dp[i][0] 表示第i天交易完后手里没有股票的最大利润,
                 dp[i][1] 表示第i天交易完后手机持有一只股票的最大利用(i从0开始)
         考虑dp[i][0]的转移方程,如果这一天交易完成后手里没有股票,那么可能的转移状态为前一天已经没有股票,
         即dp[i-1][0],或者前一天结束的时候持有一只股票,即dp[i-i][1],这时候我们要将其卖出，并
         获得prices[i]的收益. 因此为了收益最大化,我们列出如下的转移方程:
         dp[i][0]=max{dp[i-1][0], dp[i-1][1]+prices[i]}

         再来考虑dp[i][1],按照同样的方式考虑转移状态,那么可能的转移状态为前一天已经持有一只股票,
         即dp[i-1][1], 或者前一天结束时还没有股票,即 dp[i-1][0], 这时候我们要将其买入,并减少prices[i]
         的收益. 可以列出如下转移方式:
         d[i][1]=max{dp[i-1][1], dp[i-1][0]-prices[i]}

           对于初始状态,根据状态定义可以知道第0天交易结束的时候
           dp[0][0] = 0, dp[0][1]=-prices[0]
           因此,从前往依次计算状态可知,由于全部交易结束后,持有股票的收益一定低于不持有股票的收益,
           因此这时候dp[n-1][0]的收益必然是大于dp[n-1][1]的
     */
    public int maxProfitII(int[] prices){
        if (prices == null || prices.length <= 0) return 0;
        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int i=1;i<n;i++){
            dp[i][0] = Math.max(dp[i-1][0],dp[i-1][1]+prices[i]);
            dp[i][1] = Math.max(dp[i-1][1],dp[i-1][0]-prices[i]);
        }
        return dp[n-1][0];
    }
    /*
    可以看到上面的状态转移方程中,每一天的状态只与前一天的状态有关,
    而与更早的状态都无关,因此可以不必存储那些无关的状态,只需要将dp[i-1][0] 和dp[i-1][1]
    存放在两个变量中,通过他们计算出dp[i][0]和dp[i][1]并存回对应的变量,便于i+1天的状态转移
     */
    public int maxProfitIII(int[] prices){
        int n = prices.length;
        int dp0 = 0;
        int dp1 = -prices[0];
        for (int i=0; i<n; i++){
            int newDp0 = Math.max(dp0,dp1+prices[i]);
            int newDp1 = Math.max(dp1,dp0-prices[i]);
            dp0 = newDp0;
            dp1 = newDp1;
        }
        return dp0;
    }

    public static void main(String[] args) {
        final Solution_maxProfitII solutionMaxProfitII = new Solution_maxProfitII();
        System.out.println(solutionMaxProfitII.maxProfit(new int[]{7,1,5,3,6,4}));
        System.out.println(solutionMaxProfitII.maxProfitII(new int[]{7,1,5,3,6,4}));
        System.out.println(solutionMaxProfitII.maxProfitIII(new int[]{7,1,5,3,6,4}));
    }
}
