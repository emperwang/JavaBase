package com.wk;

import java.util.Scanner;

/**
 * @author: Sparks
 * @Date: 2021/3/15 23:47
 * @Description
 */
public class ByteCal {
    /*
    输入:
    3
    010
    110
    输出:
    1

    010 | 110 = 110
    100 | 110 = 110
    010 | 110 = 110
    001 | 110 = 111
     */
    public void swapCompare(){
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        String str1 = scanner.next();
        String str2 = scanner.next();
        int ch = Integer.parseInt(str1, 2);
        int not = Integer.parseInt(str2, 2);
        int res = 0;
        while (num > 0){
            int tmp = 0x1<<(num-1);
            if (((ch & tmp)==0) && ((not & tmp) != 0)){
                res++;
            }
            if (((ch & tmp)!=0) && ((not & tmp) == 0)){
                res++;
            }
            //System.out.println(ch & (0x1 << num-1));
            num--;
        }
        System.out.println(res);
    }

    public static void main(String[] args) {
        ByteCal cal = new ByteCal();
        cal.swapCompare();
    }
}
