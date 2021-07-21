package com.wk.test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Sparks
 * @Date: 2021/7/20 16:44
 * @Description
 */
public class MapTest {

    @Test
    public void testMap(){
        Map<Integer,Object> maps =new HashMap<>();
        maps.put(1,"hello world");
        maps.put(2,"hello world2");

        Object o = maps.computeIfAbsent(3, id -> {
            System.out.println("parameter: " + id);
            return id + " test result";
        });

        System.out.println(o);
    }
}
