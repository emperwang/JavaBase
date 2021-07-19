package com.wk;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Sparks
 * @Date: 2021/7/12 18:46
 * @Description
 */
public class MapTest {

    @Test
    public void test1(){
        Map<String, List<String>> maps = new ConcurrentHashMap<>();
        String key = "123";
        maps.computeIfAbsent(key,value -> new ArrayList<>()).add("456");
        maps.computeIfAbsent(key, val -> new ArrayList<>()).add("6789");
        List<String> strings = maps.get(key);
        strings.stream().forEach(System.out::println);
    }
}
