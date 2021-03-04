package com.wk.other;

import java.util.Arrays;

/**
 * @author: wk
 * @Date: 2021/1/20 13:13
 * @Description
 */
public class Solution_isAnagram {
    /*
    给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
    示例 1:
    输入: s = "anagram", t = "nagaram"
    输出: true

    示例 2:
    输入: s = "rat", t = "car"
    输出: false
    说明:
        你可以假设字符串只包含小写字母。
    进阶:
    如果输入字符串包含 unicode 字符怎么办？你能否调整你的解法来应对这种情况？
     */
    // 对两个字符串进行排序,然后进行比较
    public boolean isAnagram(String s, String t) {
        if (s == null || t ==null || s.length()<= 0|| t.length() <= 0) return false;
        if (s.length() != t.length()){
            return false;
        }
        final char[] chars = s.toCharArray();
        final char[] chars1 = t.toCharArray();
        Arrays.sort(chars);
        Arrays.sort(chars1);
        return chars.equals(chars1);
    }
    /*
    使用26英文字母表 来映射两个字符串中字符出现的位置
    s中出现的则加1, t中出现的则减1,最后如果全部为0,则相同
     */
    public boolean isAnagram2(String s, String t) {
        if (s == null || t ==null || s.length()<= 0|| t.length() <= 0) return false;
        if ("".equals(s) && "".equals(t)) return true;
        if (s.length() != t.length()){
            return false;
        }
        final int[] ints = new int[26];
        for (int i=0;i<s.length();i++){
            ints[s.charAt(i)-97]++;
            ints[t.charAt(i)-97]--;
        }
        for (int j=0;j<ints.length;j++){
            if (ints[j] != 0) return false;
        }
        return true;
    }
    public static void main(String[] args) {

    }
}
