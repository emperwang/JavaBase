package com.wk.other;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wk
 * @Date: 2021/1/6 18:06
 * @Description
 */
public class Solution_romToInt {
    /*
    罗马数字包含以下七种字符: I， V， X， L，C，D 和 M。
        字符          数值
        I             1
        V             5
        X             10
        L             50
        C             100
        D             500
        M             1000

    例如， 罗马数字 2 写做 II ，即为两个并列的 1。12 写做 XII ，即为 X + II 。 27 写做  XXVII, 即为 XX + V + II 。
    通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做 IIII，而是 IV。数字 1 在数字 5 的左边，
    所表示的数等于大数 5 减小数 1 得到的数值 4 。同样地，数字 9 表示为 IX。这个特殊的规则只适用于以下六种情况：
        I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。
        X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。
        C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900。
    给定一个罗马数字，将其转换成整数。输入确保在 1 到 3999 的范围内。

    示例 1:
    输入: "III"
    输出: 3
     */
    /* 10 10 5 1
        X X V I     26
       10 10 -1 5
        X X I V     24
        可以每次获取两个值,如果前面的值比后面的小,则进行的是减法,否则为加法;如果是最后一个值,则表示加法
     */
    // 空间 O(1)  时间 O(n)
    public int romanToInt(String s){
        if (s == null || s.length() <=0){
            return 0;
        }
        int res = 0;
        int preNum = getValue(s.charAt(0));
        for (int i=1;i<=s.length()-1; i++){
           int num = getValue(s.charAt(i));
            if (preNum < num){
                res -= preNum;
            }else{
                res += preNum;
            }
            preNum = num;
        }
        res += preNum;
        return res;
    }

    private int getValue(char s){
        switch (s){
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
        }
        return -1;
    }
    // 时间: O(n)  空间O(1)
    public int romToInt2(String s){
        if (s == null || s.length() <= 0){
            return 0;
        }
        Map<Character,Integer> map = new HashMap<>();
        map.put('I',1);
        map.put('V',5);
        map.put('X',10);
        map.put('L',50);
        map.put('C',100);
        map.put('D',500);
        map.put('M',1000);
        int preNum = map.get(s.charAt(0));
        int res = 0;
        for (int i=1; i<s.length();i++){
            int num = map.get(s.charAt(i));
            if (preNum < num){
                res -= preNum;
            }else{
                res += preNum;
            }
            preNum = num;
        }
        res+=preNum;
        return res;
    }
    public static void main(String[] args) {
        final Solution_romToInt romToInt = new Solution_romToInt();
        System.out.println(romToInt.getValue('I'));
        System.out.println(romToInt.romanToInt("XXVI"));    // 26
        System.out.println(romToInt.romanToInt("XXIV"));    // 24
        //
        System.out.println(romToInt.romToInt2("XXVI"));    // 26
        System.out.println(romToInt.romToInt2("IV"));    //
    }
}
