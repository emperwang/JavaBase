package com.wk;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PassTest {

    @Test
    public void testAdd(){
        int a = 5;
        // 测试传递
        add(a);
        System.out.println("a = " + a);
        // 测试引用传递
        List<String> list = new ArrayList<>();
        add(list);
        System.out.println("list :" + list.toString());
    }
    // 值传递
    public void add(int v){
        v += 10;
        System.out.println("v = " + v);
    }

    public void add(List<String> l){
        l.add("add value");
        System.out.println("add list value :" + l.toString());
    }
}
