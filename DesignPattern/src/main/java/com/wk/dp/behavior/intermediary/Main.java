package com.wk.dp.behavior.intermediary;

import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 11:37
 * @Description
 */
public class Main {
    public static void main(String[] args) {
        LianjiaMediary lianjiaMediary = new LianjiaMediary();
        RujiaMediary rujiaMediary = new RujiaMediary();
        lianjiaMediary.register(new House().setAddress("beijing").setPrice(2000).setSize(20));
        lianjiaMediary.register(new House().setAddress("beijing").setPrice(4000).setSize(40));
        lianjiaMediary.register(new House().setAddress("beijing").setPrice(4000).setSize(30));

        Client client = new Client();
        client.findMediary(lianjiaMediary);
        List<House> house = client.findHouse(4000);
        System.out.println(house);
    }
}
