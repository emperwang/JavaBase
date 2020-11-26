package com.wk.map;

import org.junit.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author: wk
 * @Date: 2020/11/26 15:27
 * @Description
 */
public class TreeMapTest {

    /*
    测试常用的API
     */
    public void test1(){
        final Random random = new Random();
        final TreeMap map = new TreeMap();
        // 添加操作
        map.put("one", random.nextInt(10));
        map.put("two", random.nextInt(10));
        map.put("three", random.nextInt(10));
        System.out.printf("%s \n", map);
        // 通过iterator 遍历 key-value
        final Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            System.out.printf("next : %s - %s\n", entry.getKey(), entry.getValue());
        }
        // 键值对个数
        System.out.printf("size: %s \n", map.size());
        // 是否包含键
        System.out.printf("contains key two: %s \n", map.containsKey("two"));
        System.out.printf("contains key five: %s \n", map.containsKey("five"));
        // 是否包含value
        System.out.printf("contains value 0: %s \n", map.containsValue(1));
        // 删除key
        map.remove("three");
        System.out.printf("map: %s\n", map);
        // 清空
        map.clear();
        // 判断是否为空
        System.out.printf("isEmpty: %s\n", map.isEmpty());
    }

    public void test2(){
        final TreeMap map = new TreeMap();
        // 添加操作
        map.put("a", 101);
        map.put("b", 102);
        map.put("c", 103);
        map.put("d", 104);
        map.put("e", 105);

        // headMap
        System.out.printf("headMap(c): \n\t %s\n", map.headMap("c"));
        // headMap(key, inclusive)
        System.out.printf("headMap(c,true)\n\t %s\n", map.headMap("c", true));
        System.out.printf("headMap(c,false)\n\t %s\n", map.headMap("c", false));
        // tailMap(fromKey)
        System.out.printf("tailMap(c) \n\t %s\n", map.tailMap("c"));
        // tailMap(fromKey, inclusive)
        System.out.printf("tailMap(c,true) \n\t %s\n", map.tailMap("c", true));
        System.out.printf("tailMap(c,false) \n\t %s\n", map.tailMap("c", false));

        // subMap(fromKey, toKey)
        System.out.printf("subMap(a,c) \t\n %s\n", map.subMap("a","c"));
        // subMap(fromKey, inclusiveFromKey, toKey, inclusiveToKey)
        System.out.printf("subMap(a,true,c,false) \t\n %s\n", map.subMap("a",true,"c", false));
        System.out.printf("subMap(a,true,c,true) \t\n %s\n", map.subMap("a",true,"c", true));
        System.out.printf("subMap(a,false,c,true) \t\n %s\n", map.subMap("a",false,"c", true));
        System.out.printf("subMap(a,false,c,false) \t\n %s\n", map.subMap("a",false,"c", false));

        // navigableKeySet
        System.out.printf("map.navigableKeySet: \n\t %s\n", map.navigableKeySet());
        // descendingKeySet
        System.out.printf("descendingKeySet : \n\t %s\n", map.descendingKeySet());
    }
    /*
        导航函数
     */
    public void test3(){
        final TreeMap map = new TreeMap();
        // 添加操作
        map.put("aaa", 101);
        map.put("bbb", 102);
        map.put("eee", 105);
        map.put("ccc", 103);
        map.put("ddd", 104);
        System.out.printf("map : \n\t %s \n", map);
        // 获取第一个key  第一个entry
        System.out.printf("First key:%s,\t First entry: %s\n", map.firstKey(), map.firstEntry());
        // 获取最后一个key,最后一个entry
        System.out.printf("Last key: %s,\t Last entry: %s\n", map.lastKey(), map.lastEntry());
        // 获取小于/等于 bbb的最大键值对
        System.out.printf("key floor before bbb: \n\t %s\n", map.floorEntry("ccc"));
        // 获取小于 bbb 的最大键值对
        System.out.printf("key lower ccc: \n\t %s \n", map.lowerEntry("ccc"));
        // 获取 大于/等于 bbb的最小键值对
        System.out.printf("key ceiling after ccc: \n\t %s \n", map.ceilingEntry("ccc"));
        // 获取大于bbb的最小键值对
        System.out.printf("key higer after ccc: \t\n %s \n", map.higherEntry("ccc"));
    }

    @Test
    public void testMain(){
//        test1();
//        test2();
        test3();
    }
}
