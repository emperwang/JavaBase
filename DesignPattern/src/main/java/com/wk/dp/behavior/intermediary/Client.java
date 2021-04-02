package com.wk.dp.behavior.intermediary;

import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:58
 * @Description
 */
public class Client {
    private Mediary mediary;

    public void findMediary(Mediary mediary){
        this.mediary = mediary;
    }

    public List<House> findHouse(int price){
        if (mediary == null){
            System.out.println("please find mediary first");
            return null;
        }
        List<House> houses = mediary.find(price);
        return houses;
    }
}
