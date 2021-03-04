package com.wk.other;

import java.util.LinkedList;

/**
 * @author: wk
 * @Date: 2021/1/7 11:23
 * @Description
 */
public class Solution_isValid {
    /*
    给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。
    有效字符串需满足：
    左括号必须用相同类型的右括号闭合。
    左括号必须以正确的顺序闭合。
    注意空字符串可被认为是有效字符串。

    示例 1:
    输入: "()"
    输出: true
    输入: "(]"
    输出: false
    输入: "([)]"
    输出: false
    输入: "{[]}"
    输出: true
     */
    /*
        1. 如果数据长度为奇数,那么括号肯定不成对,则直接返回false
        2. 遍历s中的括号,如果遇到一个左括号,则放入栈中
        3. 遍历到了右括号,则和栈顶相比较,如果相同,则把栈顶元素删除,如果不同则返回false
        4. 最后如果栈中没有元素,则说明有效
     */
    // 时间O(n)
    public boolean isValid(String s) {
        if (s == null){
            return false;
        }
        if ("".equalsIgnoreCase(s)){
            return true;
        }
        if (s.length() % 2 == 1){
            return false;
        }
        final LinkedList<Character> list = new LinkedList<>();
        for (int i = 0;i<s.length();i++){
            if (isLeft(s.charAt(i))){
                list.addFirst(s.charAt(i));
            }else{
                if (list.isEmpty()) return false;
                final Character first = list.getFirst();
                if (isPair(first,s.charAt(i))){
                    list.removeFirst();
                }else{
                    return false;
                }
            }
        }
        return list.isEmpty();
    }

    public boolean isPair(Character key,Character toCompare){
        switch (key){
            case '(':
                if (toCompare == ')') {
                    return true;
                }
                break;
            case '{':
                if (toCompare == '}') {
                    return true;
                }
                break;
            case '[':
                if (toCompare == ']') {
                    return true;
                }
                break;
        }
        return false;
    }

    public boolean isLeft(Character c){
        switch (c){
            case '(':
                return true;
            case '{':
                return true;
            case '[':
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        final Solution_isValid isValid = new Solution_isValid();
        System.out.println(isValid.isValid("){"));
        System.out.println(isValid.isValid("([}}])"));
    }
}
