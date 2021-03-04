package com.wk.other;

/**
 * @author: wk
 * @Date: 2021/1/7 14:36
 * @Description
 */
public class Solution_strStr {
    /*
    实现 strStr() 函数。
    给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。
    如果不存在，则返回  -1。

    示例 1:
    输入: haystack = "hello", needle = "ll"
    输出: 2
     */
    /*
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa (贼长)
    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab (最后一个不等)
    导致运行时时间特别长
     */
    public int strStr(String haystack, String needle) {
        if (haystack == null ||  haystack.length()<0 ){
            return -1;
        }
        if (needle == null || needle.length() < 0){
            return -1;
        }
        if (haystack.length() > 0 && "".equals(needle)){
            return 0;
        }
        if ("".equals(haystack) && needle.length() > 0){
            return -1;
        }
        if ("".equals(haystack) && "".equals(needle)){
            return 0;
        }
        if (needle.length() > haystack.length()){
            return -1;
        }
        int L = haystack.length();
        int N = needle.length();
        /*
         总共需要遍历 L-N 次
         nidx表示遍历到 needle的位置,i表示遍历haystack的位置
         current表示最大的相同的字符串的长度
         最坏时间: O((L-N)*N)
         */
        for (int i=0; i<(L-N+1); i++){
            if (needle.charAt(0) == haystack.charAt(i)){
                int nidx = 0,current=0;
                while (nidx < N && i<L && needle.charAt(nidx++) == haystack.charAt(i++)){
                    current++;
                }
                if (current == N){
                    return i-current;
                }
                i = i-nidx;
            }
        }
//        for (int i=0; i<haystack.length();i++){
//            if (needle.charAt(0) == haystack.charAt(i)){
//                int j = 0,k=i;
//                while ( j<=(needle.length()-1) && k < haystack.length()){
//                    if (needle.charAt(j++) != haystack.charAt(k++)){
//                        break;
//                    }
//                    if (j > (needle.length()-1)){
//                        return i;
//                    }
//                }
//            }
//        }

        return -1;
    }

    // 窗口滑动,每次都比较needle.length个子字符串, 然后需要比较 haystack.length - needle.length 次
    // O((N-L)L)
    public int strStr1(String haystack, String needle) {
        if (haystack == null ||  haystack.length()<0 ){
            return -1;
        }
        if (needle == null || needle.length() < 0){
            return -1;
        }
        if (haystack.length() > 0 && "".equals(needle)){
            return 0;
        }
        if ("".equals(haystack) && needle.length() > 0){
            return -1;
        }
        if ("".equals(haystack) && "".equals(needle)){
            return 0;
        }
        if (needle.length() > haystack.length()){
            return -1;
        }
        int L = needle.length();
        int N = haystack.length();
        for (int st = 0; st < N-L+1; st++){
            if (haystack.substring(st,st+L).equals(needle)){
                return st;
            }
        }
        return -1;
    }


    public static void main(String[] args) {
        final Solution_strStr solutionStrStr = new Solution_strStr();
        final int idx2 = solutionStrStr.strStr("mississippi", "pi");
        System.out.println(idx2);
        final int idx1 = solutionStrStr.strStr("mississippi", "issip");
        System.out.println(idx1);
        final int idx0 = solutionStrStr.strStr1("mississippi", "issip");
        System.out.println(idx0);
//        final int idx = solutionStrStr.strStr("hello", "ll");
//        System.out.println(idx);


//        final int idx1 = solutionStrStr.strStr("hello", "he");
//        System.out.println(idx1);
//
//        final int idx2 = solutionStrStr.strStr("", "");
//        System.out.println(idx2);
//
//        final int idx3 = solutionStrStr.strStr("a", "");
//        System.out.println(idx3);
    }
}
