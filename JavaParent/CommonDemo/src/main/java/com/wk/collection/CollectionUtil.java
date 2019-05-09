package com.wk.collection;

import com.google.common.collect.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CollectionUtil {

    /**
     * 创建一个arrayList
     * @param <T>
     * @return
     */
    public static <T>ArrayList<T> newArrayList(){
        return new ArrayList<>();
    }

    /**
     *  创建一个特定容量的ArrayList
     * @param capacity
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> newArrayList(int capacity){
        return  new ArrayList<>(capacity);
    }

    /**
     * 创建一个HashMap
     */
    public static <K,V> HashMap<K,V> newHashMap(){
        return new HashMap<>();
    }

    /**
     * 创建一个指定容量的HashMap
     * @param capacity
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> HashMap<K,V>  newHashMap(int capacity){
        return new HashMap<>(capacity);
    }

    /**
     * 创建一个LinkedHashMap
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> LinkedHashMap<K,V> newLinkedHashMap(){
        return new LinkedHashMap<>();
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(){
        return new LinkedHashSet<>();
    }

    /**
     * 创建一个多线程可以访问的并发的ConcurrentHashMap
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> ConcurrentHashMap<K,V> newConcurrentHashMap(){
        return new ConcurrentHashMap<>();
    }

    public static <K,V> ConcurrentHashMap<K,V> newConcurrentHashMap(int capacity){
        return new ConcurrentHashMap<>(capacity);
    }

    public static <E>ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(){
        return  new ConcurrentLinkedQueue<>();
    }

    /**
     *  创建一个指定长度的空list
     * @param length
     * @return
     */
    public static List<String> createEmptyList(int length){
        List<String> list = new ArrayList<>(length);
        while(length-- >0){
            list.add(StringUtils.EMPTY);
        }
        return list;
    }

    /**
     *  把一个list扩展为特定的长度,内容补为空串
     * @param values
     * @param length
     */
    public static void fill(List<String> values,int length){
        int size = values.size();
        if(size >= length){
            return;
        }
        values.addAll(createEmptyList(length - size));
    }

    public static <E> ArrayDeque<E> newArrayDeque(){
        return new ArrayDeque<>();
    }

    /**
     * 创建包含指定元素的ArrayDeque
     * @param elements
     * @param <E>
     * @return
     */
    public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> elements){
        if(elements instanceof Collection){
            return new ArrayDeque<>((Collection<E>)elements);
        }
        ArrayDeque<E> deque = new ArrayDeque<>();
        Iterables.addAll(deque,elements);
        return deque;
    }

    public static <K,V>ArrayListMultimap<K,V> newArrayListMultimap(){
        return ArrayListMultimap.create();
    }

    public static <K,V> HashMultimap<K,V> newHashMultimap(){
        return HashMultimap.create();
    }

    public static <K,V> LinkedHashMultimap<K,V> newLinkedHashMultimap(){
        return LinkedHashMultimap.create();
    }

    public static <K extends Comparable,V extends Comparable>TreeMultimap<K,V> newTreeMultiMap(){
        return TreeMultimap.create();
    }
}
