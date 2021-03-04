package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/8 11:42
 * @Description
 */
public class Solution_countAndSay {
    /*
    1<= n <=30
    给定一个正整数 n ，输出外观数列的第 n 项。
    「外观数列」是一个整数序列，从数字 1 开始，序列中的每一项都是对前一项的描述。
    你可以将其视作是由递归公式定义的数字字符串序列：
    countAndSay(1) = "1"
    countAndSay(n) 是对 countAndSay(n-1) 的描述，然后转换成另一个数字字符串。

    前五项如下：
    1.     1
    2.     11
    3.     21
    4.     1211
    5.     111221
    第一项是数字 1
    描述前一项，这个数是 1 即 “ 一 个 1 ”，记作 "11"
    描述前一项，这个数是 11 即 “ 二 个 1 ” ，记作 "21"
    描述前一项，这个数是 21 即 “ 一 个 2 + 一 个 1 ” ，记作 "1211"
    描述前一项，这个数是 1211 即 “ 一 个 1 + 一 个 2 + 二 个 1 ” ，记作 "111221"

    示例 1：
    输入：n = 1
    输出："1"
    解释：这是一个基本样例。

    示例 2：
    输入：n = 4
    输出："1211"
    解释：
    countAndSay(1) = "1"
    countAndSay(2) = 读 "1" = 一 个 1 = "11"
    countAndSay(3) = 读 "11" = 二 个 1 = "21"
    countAndSay(4) = 读 "21" = 一 个 2 + 一 个 1 = "12" + "11" = "1211"
     */
    public String countAndSay(int n) {
        if (n<1 || n>30) return "";
        // 递归终止条件
        if (n == 1) {
            return "1";
        }
        final StringBuilder builder = new StringBuilder();
        String res = countAndSay(n-1);
        int i=0;
        final int length = res.length();
        while (i<length){
            int count = 1;
            // 每次对比的是两个,故对比长度比总长度小1,大于字符串长度
            while (i<length-1  && res.charAt(i) == res.charAt(i+1)){
                i++;
                count++;
            }
            builder.append(count).append(res.charAt(i));
            i++;
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        final Solution_countAndSay solutionCountAndSay = new Solution_countAndSay();
        System.out.println(solutionCountAndSay.countAndSay(2));
        System.out.println(solutionCountAndSay.countAndSay(3));
        System.out.println(solutionCountAndSay.countAndSay(4));
    }
}
