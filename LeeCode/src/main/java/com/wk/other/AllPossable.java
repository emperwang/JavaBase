package com.wk.other;

/**
 * @author: wk
 * @Date: 2020/12/4 13:44
 * @Description
 */
public class AllPossable {

    /*
        查找一个字符串中字符所有排列
        如: abc
        1. 先固定a, 查找后面bc的全排列,abc acd, 之后置换ab的位置
        2. 固定b位置bac, 查找后面 ac的全排列,bac bca, 之后置换ac的位置
        3.固定c位置cba,查找后面ba的全排列,cba,cab
     */
    public static void allPossableChar(char[] ch, int idx, int len){
        if (idx == len){
            for (int i=0; i < len; i++){
                System.out.printf("%s ", ch[i]);
            }
            System.out.println();
            return;
        }
        for (int i=idx; i<len; i++){
            swap(ch,i, idx);
            allPossableChar(ch,idx+1,len);
            swap(ch,i, idx);
        }
    }

    public static void swap(char[] ch,int first, int sec){
        char tmp = ch[first];
        ch[first] = ch[sec];
        ch[sec] = tmp;
    }

    public static void main(String[] args) {
        String str = "abc";
        //System.out.printf("%s\n", str.toCharArray().length);
        allPossableChar(str.toCharArray(),0,str.length());
    }
}
