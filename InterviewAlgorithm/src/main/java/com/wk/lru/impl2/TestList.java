package com.wk.lru.impl2;

// lRU 算法
public class TestList {
    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        cache.add(1,"a");
        cache.add(2,"b");
        cache.add(3,"c");
        System.out.println("key 1 = " + cache.get(1));
        cache.add(4,"d");
        System.out.println("key 1 = " + cache.get(1));
    }

    public void testDublist(){
        DubleList<Integer, String> dl = new DubleList<>();
        dl.addFirst(new Node<>(1,"2"));
        dl.addFirst(new Node<>(2,"3"));
        dl.addFirst(new Node<>(3,"4"));

        System.out.println("size = "+dl.size());

        dl.remove(new Node<>(1,"2"));
        System.out.println("size = " + dl.size());
    }
}
