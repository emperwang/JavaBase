package com.wk;

/**
 * @author: Sparks
 * @Date: 2021/3/15 23:03
 * @Description
 */
public class FindStr {

    public int findStr(String t, String p){
        if (t == null || t.length() <= 0 || p == null || p.length()<=0) return -1;
        char[] tch = t.toCharArray();
        char[] pch = p.toCharArray();
        for (int i=0; i< tch.length; i++){
            if (tch[i] == pch[0]){
                int tmp = i,j =0;
                while (j < pch.length){
                    if (tch[tmp] == pch[j]){
                        tmp++;
                        j++;
                    }else {
                        break;
                    }
                }
                if (j == pch.length) return i+1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String t = "AVERDXIVYERDIAN";
        String p = "RDXI";
        FindStr str = new FindStr();
        int str1 = str.findStr(t, p);
        if (-1 == str1){
            System.out.println("No");
        }else{
            System.out.println(str1);
        }
    }
}
