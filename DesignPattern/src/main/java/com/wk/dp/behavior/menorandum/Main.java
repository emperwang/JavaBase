package com.wk.dp.behavior.menorandum;

import java.util.Arrays;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 14:12
 * @Description
 */
public class Main {
    public static void main(String[] args) {
        Client client = new Client();
        MemoraMgr mgr = new MemoraMgr();
        client.setMgr(mgr);
        client.record(Arrays.asList("th1","th2","th3","th4"));
        //client.showInfo();
        client.backUp("mon");
        //client.showInfo();
        //client.restore("mon");
        //client.showInfo();

        client.record(Arrays.asList("ww1","ww2","ww3","ww4"));
        client.backUp("fri");

        client.restore("mon");
        client.showInfo();
    }
}
