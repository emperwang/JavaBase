package com.wk.other;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wk
 * @Date: 2021/1/18 10:22
 * @Description
 */
public class Solution_isIsomorphic {
    /*
    给定两个字符串 s 和 t，判断它们是否是同构的。
    如果 s 中的字符可以按某种映射关系替换得到 t ，那么这两个字符串是同构的。
    每个出现的字符都应当映射到另一个字符，同时不改变字符的顺序。不同字符不能映射到同一个字符上，相同字符只能映射到同一个字符上，
    字符可以映射到自己本身。

    示例 1:
    输入：s = "egg", t = "add"
    输出：true

    示例 2：
    输入：s = "foo", t = "bar"
    输出：false
     */
    /*
     制作两个链表的映射
     */
    public boolean isIsomorphic(String s, String t) {
        Map<Character,Character> s2t = new HashMap<>();
        Map<Character,Character> t2s = new HashMap<>();
        for (int i=0; i < s.length(); i++){
            char s1 = s.charAt(i), t1 = t.charAt(i);
            if ((s2t.containsKey(s1) && s2t.get(s1) != t1) || (t2s.containsKey(t1) && t2s.get(t1) != s1)){
                return false;
            }
            s2t.put(s1,t1);
            t2s.put(t1,s1);
        }
        return true;
    }

    public boolean isIsomorphic2(String s, String t) {
        final char[] s1 = s.toCharArray();
        final char[] t1 = t.toCharArray();
        for (int i=0; i <s1.length; i++){
            if (s.indexOf(s1[i]) != t.indexOf(t1[i])){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

    }
}
