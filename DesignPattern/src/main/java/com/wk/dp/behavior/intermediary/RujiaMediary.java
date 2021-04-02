package com.wk.dp.behavior.intermediary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 10:58
 * @Description
 */
public class RujiaMediary implements Mediary{
    List<House> houses ;

    public RujiaMediary(){
        houses = new ArrayList<>();
    }
    @Override
    public void register(House house){
        houses.add(house);
    }
    @Override
    public void deregister(House house){
        houses.remove(house);
    }
    @Override
    public List<House> find(int price){
        List<House> res = new ArrayList<>();
        houses.forEach(house -> {
            if (house.getPrice() <= price){
                res.add(house);
            }
        });
        return res;
    }
}
