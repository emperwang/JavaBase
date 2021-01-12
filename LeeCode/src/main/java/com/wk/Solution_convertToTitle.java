package com.wk;

/**
 * @author: wk
 * @Date: 2021/1/12 13:15
 * @Description
 */
public class Solution_convertToTitle {
    /*
    给定一个正整数，返回它在 Excel 表中相对应的列名称。
    例如，
        1 -> A
        2 -> B
        3 -> C
        ...
        26 -> Z
        27 -> AA
        28 -> AB
        ...
    示例 1:
    输入: 1
    输出: "A"

    示例 2:
    输入: 28
    输出: "AB"
     */
    // 十进制转换为 26进制
    public String convertToTitle(int n) {
        final StringBuilder builder = new StringBuilder();
        while (n > 0){
            int mod = n%26;
            builder.append(translate(mod==0?26:mod));
            n = mod==0?(n/26-1):n/26;
        }
        return builder.reverse().toString();
    }
    // 10进制转换为2进制
    public String octToBinary(int n){
        final StringBuilder builder = new StringBuilder();
        while (n>0){
            int mod = n%2;
            builder.append(mod==0?0:1);
            n = n/2;
        }
        return builder.reverse().toString();
    }

    public String translate(int code){
        String s = "";
        switch (code){
            case 0:
                s = "A";
                break;
            case 1:
                s = "A";
                break;
            case 2:
                s = "B";
                break;
            case 3:
                s = "C";
                break;
            case 4:
                s = "D";
                break;
            case 5:
                s = "E";
                break;
            case 6:
                s = "F";
                break;
            case 7:
                s = "G";
                break;
            case 8:
                s = "H";
                break;
            case 9:
                s = "I";
                break;
            case 10:
                s = "J";
                break;
            case 11:
                s = "K";
                break;
            case 12:
                s = "L";
                break;
            case 13:
                s = "M";
                break;
            case 14:
                s = "N";
                break;
            case 15:
                s = "O";
                break;
            case 16:
                s = "P";
                break;
            case 17:
                s = "Q";
                break;
            case 18:
                s = "R";
                break;
            case 19:
                s = "S";
                break;
            case 20:
                s = "T";
                break;
            case 21:
                s = "U";
                break;
            case 22:
                s = "V";
                break;
            case 23:
                s = "W";
                break;
            case 24:
                s = "X";
                break;
            case 25:
                s = "Y";
                break;
            case 26:
                s = "Z";
                break;
        }
        return s;
    }

    public static void main(String[] args) {
        final Solution_convertToTitle solutionConvertToTitle = new Solution_convertToTitle();
//        System.out.println(solutionConvertToTitle.convertToTitle(26));
//        System.out.println(solutionConvertToTitle.convertToTitle(27));
//        System.out.println(solutionConvertToTitle.convertToTitle(28));
//        System.out.println(solutionConvertToTitle.convertToTitle(29));
        System.out.println(solutionConvertToTitle.convertToTitle(52));
        System.out.println(solutionConvertToTitle.octToBinary(5));
        System.out.println(solutionConvertToTitle.octToBinary(2));
    }
}
