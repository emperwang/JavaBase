package com.wk.dp.behavior.menorandum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ekiawna
 * @Date: 2021/4/2 13:56
 * @Description
 */
public class Client {
    private List<String> todoList;
    private MemoraMgr mgr;
    public Client(){
        this.todoList = new ArrayList<>();
    }

    public void setMgr(MemoraMgr mgr) {
        this.mgr = mgr;
    }

    public void forget(){
        this.todoList.clear();
    }

    public Client record(String thing){
        this.todoList.add(thing);
        return this;
    }

    public Client record(List<String> things){
        this.todoList.addAll(things);
        return this;
    }

    public void backUp(String name){
        if (mgr == null){
            System.out.println("please set memora mgr first");
            return;
        }
        Memora memora = new Memora();
        memora.writeItem(todoList);
        this.todoList.clear();
        mgr.saveState(name, memora);
    }

    public void restore(String name){
        Memora state = mgr.getState(name);
        this.todoList = state.getStates();
    }

    public void showInfo(){
        StringBuilder builder = new StringBuilder();
        builder.append("todoList: ").append("\n");
        todoList.forEach(ttt -> {
            builder.append(ttt).append("\n");
        });
        System.out.println(builder.toString());
    }
}
