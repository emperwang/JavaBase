package com.wk.dp.behavior.menorandum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 13:55
 * @Description
 */
public class MemoraMgr {
    private Map<String, Memora> backStates;

    public MemoraMgr(){
        this.backStates = new HashMap<>();
    }

    public void saveState(String name, Memora memora){
        backStates.put(name, memora);
    }

    public Memora getState(String name){
        return backStates.get(name);
    }
}
