package com.wk;

/**
 * @author: wk
 * @Date: 2021/1/7 10:21
 * @Description
 */
public class Solution_longestCommonPrefix {
    /*
    编写一个函数来查找字符串数组中的最长公共前缀。
    如果不存在公共前缀，返回空字符串 ""。

    示例 1:
    输入: ["flower","flow","flight"]
    输出: "fl"
     */
    /*
      获取数组中的每个最长前缀,即获取数组中每个字符串的最长前缀
      1. 先获取 前2个字符串的 最长前缀
      2. 使用上面获取到的最长前缀, 依次和后面进行比较 获取相同的最长前缀
     */
    // prefix长度为m, 时间 O(n*M)
    public String longestCommonPrefix(String[] strs) {
        String ret = "";
        if (strs == null || strs.length <=0){
            return ret;
        }
        String pre = strs[0];
        String nextpre = strs[1];
        String preFix = longestCommonPrefix(pre,nextpre);
        if (preFix != null && "".equalsIgnoreCase(preFix)){
            return ret;
        }
        for (int i=2; i<strs.length; i++){
            String nextStr = strs[i];
            preFix = longestCommonPrefix(preFix,nextStr);
        }
        return preFix;
    }

    public String longestCommonPrefix(String s1, String s2){
        if (s1 == null || s2==null || s1.length()<=0 || s2.length()<=0){
            return "";
        }
        final int minLen = Math.min(s1.length(), s2.length());
        int idx = 0;
        while (idx < minLen && s1.charAt(idx) == s2.charAt(idx)){
            idx++;
        }
        return s1.substring(0,idx);
    }

    public static void main(String[] args) {
        final Solution_longestCommonPrefix commonPrefix = new Solution_longestCommonPrefix();
        String strs[] = {"flower","flow","flight"};
        final String prefix = commonPrefix.longestCommonPrefix(strs);
        System.out.println(prefix);
//        final String s = commonPrefix.longestCommonPrefix("flower", "flow");
//        System.out.println(s);
    }
}
