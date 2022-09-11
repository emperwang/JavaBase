package com.wk;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author: Sparks
 * @Date: 2021/3/15 23:30
 * @Description
 */
public class MathSum {

    public int sums(){
        Scanner scanner = new Scanner(System.in);
        int nums = scanner.nextInt();
        HashSet<Integer> set = new HashSet<>();
        for (int i =0; i<nums; i++){
            set.add(scanner.nextInt());
        }
        int n = scanner.nextInt();
        if ((2*n) > set.size()){
            return -1;
        }
        List<Integer> collect = set.stream().sorted().collect(Collectors.toList());
        int sum = 0;
        for (int i = 0; i<n; i++){
            sum += collect.get(i);
            sum += collect.get(collect.size()-i-1);
        }
        return sum;
    }

    public static void main(String[] args) {
        MathSum sum = new MathSum();
        int res = sum.sums();
        System.out.println(res);
    }
}
