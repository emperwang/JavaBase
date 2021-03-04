package com.wk.other;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HongBao {
    Random random = new Random();

    private int getRandom (int max, int min){
        System.out.println("max = " + max +" min =" + min);
        return random.nextInt((max-min)>0?(max-min):(min-max)) + min;
    }

    private int createNum(int total,int sendMoney,int sendNum, int people,int avg,int max){
        int nMin = Math.max((total-sendMoney- (people-sendNum)*max), avg);
        int nMax = Math.min((total-sendMoney- (people-sendNum)*avg), max);
        return getRandom(nMax, nMin);
    }

    public List<Integer> divideMoney(int people, int total){
        int avg = (int)(total / people *0.2);
        int max = (int)(total / people *1.9);
        int sendNum = 1;
        int sendMoney = 0;
        List<Integer> result = new ArrayList<>();
        for (; sendNum < people ; sendNum++){
            int num = createNum(total, sendMoney, sendNum, people, avg, max);
            sendMoney+=num;
            result.add(num);
        }
        result.add(total - sendMoney);
        System.out.println(result.stream().mapToInt(x -> x).sum());
        return result;
    }

    public static void main(String[] args) {
        HongBao hongBao = new HongBao();
        List<Integer> integers = hongBao.divideMoney(10, 100);
        System.out.println(integers.toString());
    }
}
