package com.wk.dp.behavior.menorandum;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 13:55
 * @Description
 */
public class Memora {
    private List<String> states = new ArrayList<>();

    public void writeItem(String item){
        states.add(item);
    }

    public void writeItem(List<String> lists){
        this.states.addAll(lists);
    }

    public List<String> getStates() {
        return states;
    }
}
