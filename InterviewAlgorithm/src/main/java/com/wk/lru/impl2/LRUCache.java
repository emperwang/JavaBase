package com.wk.lru.impl2;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K,V> {
    public Map<K,V> map;
    public DubleList<K,V> list;
    private int size;
    public LRUCache(int size){
        this.size = size;
        map = new HashMap<>(size);
        list = new DubleList<>();
    }

    public void add(K key, V val){
        if (list.size() < this.size){
            if (map.containsKey(key)) {
                map.put(key, val);
                list.remove(new Node<>(key,val));
                list.addFirst(new Node<>(key, val));
            }else{
                map.put(key,val);
                list.addFirst(new Node<>(key,val));
            }
        }else{
            if (map.containsKey(key)) {
                Node<K, V> kva = new Node<>(key, val);
                list.remove(kva);
                list.addFirst(kva);
                map.put(key,val);
            }else{
                map.remove(list.tail.key);
                list.remove(list.tail);
                list.addFirst(new Node<>(key, val));
                map.put(key,val);
            }
        }
    }
    // 获取一个节点时, 把节点放到第一个
    public V get(K k){
        if (map.containsKey(k)){
            V v = map.get(k);
            Node<K, V> kvNode = new Node<>(k, v);
            list.remove(kvNode);
            list.addFirst(kvNode);
            return v;
        }else{
            return (V)"-1";
        }
    }

}

class DubleList<K,V>{
    Node<K,V> head,tail;
    private int size = 0;
    public DubleList(){
    }
    public void addFirst(Node<K,V> node){
        if (head == null){
            tail = head = node;
            tail.prev = tail.next = node;
        }else if (head != null) {
            node.next = head;
            node.prev = head.prev;
            head.prev.next = node;
            head.prev = node;
            head = node;
        }
        size++;
    }

    public Node remove(Node<K,V> node){
        if (head == null){
            return null;
        }
        if (head.key == node.key){
            Node t = head;
            head = head.next;
            size--;
            return t;
        }
        for(Node t = head; t != null; t = t.next){
            if (t.key == node.key){
                t.prev.next = t.next;
                t.next.prev = t.prev;
                size--;
                return t;
            }
        }
        return null;
    }

    public void addLast(Node<K,V> node){
        if (tail == null){
            tail = head = node;
        }else{
            node.prev = tail.prev;
            node.next = tail.next;
            tail.prev.next = node;
            tail.next.prev = node;
            tail = node;
        }
        size++;
    }

    public int size(){
        return size;
    }
}

class Node<K,V>{
    K key;
    V val;
    Node prev, next;
    public Node(K k, V v){
        key = k;
        val = v;
    }
}
