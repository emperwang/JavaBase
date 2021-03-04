package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/12 14:48
 * @Description
 */
public class Solution_titleToNumber {
    /*
    给定一个Excel表格中的列名称，返回其相应的列序号。
        例如，
            A -> 1
            B -> 2
            C -> 3
            ...
            Z -> 26
            AA -> 27
            AB -> 28
            ...

        示例 1:
        输入: "A"
        输出: 1

        示例 2:
        输入: "AB"
        输出: 28

        示例 3:
        输入: "ZY"
        输出: 701
     */
    public int titleToNumber(String s) {
        if (s == null || s.length()<=0) return 0;
        final String s1 = new StringBuilder(s).reverse().toString();
        int len = s.length()-1;
        int sum = 0;
        for (int i=0;i<=len;i++){
            sum += (translate(s1.charAt(i))*(int)Math.pow(26,i));
        }
        return sum;
    }

    public int translate(char c){
        int ret = -1;
        switch (c){
            case 'A':
                ret = 1;
                break;
            case 'B':
                ret = 2;
                break;
            case 'C':
                ret = 3;
                break;
            case 'D':
                ret = 4;
                break;
            case 'E':
                ret = 5;
                break;
            case 'F':
                ret = 6;
                break;
            case 'G':
                ret = 7;
                break;
            case 'H':
                ret = 8;
                break;
            case 'I':
                ret = 9;
                break;
            case 'J':
                ret = 10;
                break;
            case 'K':
                ret = 11;
                break;
            case 'L':
                ret = 12;
                break;
            case 'M':
                ret = 13;
                break;
            case 'N':
                ret = 14;
                break;
            case 'O':
                ret = 15;
                break;
            case 'P':
                ret = 16;
                break;
            case 'Q':
                ret = 17;
                break;
            case 'R':
                ret = 18;
                break;
            case 'S':
                ret = 19;
                break;
            case 'T':
                ret = 20;
                break;
            case 'U':
                ret = 21;
                break;
            case 'V':
                ret = 22;
                break;
            case 'W':
                ret = 23;
                break;
            case 'X':
                ret = 24;
                break;
            case 'Y':
                ret = 25;
                break;
            case 'Z':
                ret = 26;
                break;
        }
        return ret;
    }

    public static void main(String[] args) {
        final Solution_titleToNumber solutionTitleToNumber = new Solution_titleToNumber();
        System.out.println(solutionTitleToNumber.titleToNumber("AB"));
        System.out.println(solutionTitleToNumber.titleToNumber("ZY"));
        System.out.println(Math.pow(3,2));
        System.out.println(Math.pow(26,0));
    }
}
