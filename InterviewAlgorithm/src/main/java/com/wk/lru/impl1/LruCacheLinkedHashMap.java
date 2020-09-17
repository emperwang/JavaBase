package com.wk.lru.impl1;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCacheLinkedHashMap {
    public static void main(String[] args) {
        LruCache<Integer, String> cache = new LruCache<>(3);
        cache.setVal(1,"aaa");
        cache.setVal(2,"bbb");
        cache.setVal(3,"ccc");
        System.out.println("init----------- ");
        printInfo(cache);
        cache.setVal(4,"ddd");
        System.out.println("evict --- ");
        printInfo(cache);
        System.out.println("get val : "  + cache.getVal(4));
        printInfo(cache);


    }

    public static void printInfo(LruCache<Integer, String> cache){
        cache.getLrucache().forEach((key,val) -> {
            System.out.print("key = " + key+", val = " + val);
        });
        LinkedHashMap<Integer, String> lrucache = cache.getLrucache();
        lrucache.entrySet().forEach(entry -> {
            System.out.print("key = " + entry.getKey() +", values = " + entry.getValue());
            System.out.println();
        });
    }
}
