package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/11 17:32
 * @Description
 */
public class Solution_isPalindrome {
    /*
    给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。
    说明：本题中，我们将空字符串定义为有效的回文串。
名词解析:
    “回文串”是一个正读和反读都一样的字符串

    示例 1:
    输入: "A man, a plan, a canal: Panama"
    输出: true

    示例 2:
    输入: "race a car"
    输出: false
     */
    public boolean isPalindrome(String s){
        if (s == null ||s.length() <=0) return true;
        int start = 0;
        int end = s.length()-1;
        while (end>start){
            while (start<s.length() &&!Character.isLetterOrDigit(s.charAt(start))) start++;
            while (end >=0 &&!Character.isLetterOrDigit(s.charAt(end))) end--;
            if (start >= s.length() ||end<0) return false;
            char pre = s.charAt(start++);
            char post = s.charAt(end--);
            // 全部转换为小写进行比较
            if (!(pre >= 97 && pre <= 122)){
                pre = (char) (pre + 32);
            }
            if (!(post >= 97 && post <= 122)){
                post = (char) (post + 32);
            }
            if (pre != post) return false;
        }
        return true;
    }
    // 针对都是符号的字符串 处理不足  ",."
    public boolean isPalindrome2(String s){
        if (s == null ||s.length() <=0) return true;
        int start = 0;
        int end = s.length()-1;
        while (end>start){
            while (start<end && !Character.isLetterOrDigit(s.charAt(start))) {
                start++;
            }
            while (end >=0 && !Character.isLetterOrDigit(s.charAt(end))) {
                end--;
            }
            if (start <end) {
                char pre = s.charAt(start++);
                char post = s.charAt(end--);
                // 全部转换为小写进行比较
                if (Character.toLowerCase(pre) != Character.toLowerCase(post))
                    return false;
            }
        }
        return true;
    }

    public boolean isPalindrome22(String s){
        if (s == null ||s.length() <=0) return true;
        // 先进行一遍过滤,去除那些非 字符数字的字符
        final StringBuilder builder = new StringBuilder();
        for (int i=0;i<s.length();i++){
            if (Character.isLetterOrDigit(s.charAt(i))){
                builder.append(Character.toLowerCase(s.charAt(i)));
            }
        }
        String tmp = builder.toString();
        int start = 0;
        int end = tmp.length()-1;
        while (end>start){
            if (tmp.charAt(start) != tmp.charAt(end)) return false;
            end--;
            start++;
        }
        return true;
    }

    public boolean isPalindrome3(String s){
        if (s == null ||s.length() <=0) return true;
        final StringBuilder builder = new StringBuilder();
        for (int i= 0; i<s.length();i++){
            if (Character.isLetterOrDigit(s.charAt(i))){
                builder.append(s.charAt(i));
            }
        }
        final StringBuilder reverse = new StringBuilder(builder.toString()).reverse();
//        System.out.println(builder.toString());
//        System.out.println(reverse.toString());
        return builder.toString().equalsIgnoreCase(reverse.toString());
    }


    public static void main(String[] args) {
        String s = "A man, a plan, a canal: Panama";
        String s1 = "race a car";
        String s3=",.";
        String s4=".,";
        final Solution_isPalindrome solutionIsPalindrome = new Solution_isPalindrome();
        //System.out.println(Character.isLetterOrDigit(','));
        System.out.println(solutionIsPalindrome.isPalindrome2(s3));
        //System.out.println(solutionIsPalindrome.isPalindrome22(s4));
        //System.out.println(solutionIsPalindrome.isPalindrome3(s4));
    }
}
