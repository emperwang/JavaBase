package com.wk.lru.impl1;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<S, T> {
    private int cap;
    private LinkedHashMap<S,T> lrucache;
    public LruCache(int cap){
        this.cap = cap;
         lrucache = new LinkedHashMap<S,T>(20, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<S,T> eldest) {
                return size() > cap;
            }
        };
    }

    public T getVal(S key){
       return lrucache.getOrDefault(key, (T)"-1");
    }

    public void setVal(S key, T val){
        lrucache.put(key, val);
    }

    public LinkedHashMap<S, T> getLrucache() {
        return lrucache;
    }
}

