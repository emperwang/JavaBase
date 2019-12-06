package com.wk.collections;

import java.util.*;

public class CollectionsDemo {
    private static List<Integer> lists = new ArrayList<>();
    private static List<String> list2 = new ArrayList<>();
    static {
        lists.add(1);
        lists.add(10);
        lists.add(8);
        lists.add(5);
        lists.add(2);

        list2.add("abc");
        list2.add("qwe");
        list2.add("asd");
        list2.add("zxc");
        list2.add("poi");
        list2.add("lok");
    }

    private static void natureSort(){
        Collections.sort(lists);
        System.out.println(lists.toString());
        System.out.println("================================================");
        Collections.sort(list2);
        System.out.println(list2.toString());
    }

    private static void customCompare(){
        boolean disjoint = Collections.disjoint(list2, lists);
        System.out.println("disjoint: "+disjoint);

        // 只是把现有的顺序反序
        Collections.reverse(lists);
        System.out.println("reverse: "+lists.toString());
        // 指定一个排序方式，进行排序
        Collections.sort(lists, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });
        System.out.println("lists:"+lists.toString());
    }

    private static void getMax(){
        Integer max = Collections.max(lists);
        String max1 = Collections.max(list2);
        System.out.println("max :" + max+", max1:"+max1);
    }

    private static void getMin(){
        Integer min = Collections.min(lists);
        String min1 = Collections.min(list2);
        System.out.println("min: "+min+", min1 :"+min1);
    }

    private static void apiDemo(){
        List<Integer> li = Collections.checkedList(lists, Integer.class);
        System.out.println("li:"+li.toString());
        // 二叉查找法
        int i = Collections.binarySearch(lists, 10);
        System.out.println("i = "+i);
        // 获取某个元素在list中出现的次数
        int frequency = Collections.frequency(lists, 1);
        System.out.println("frequency: "+frequency);

        //Collections.fill(lists,3);
        //System.out.println("fill :"+lists.toString());
        // 打乱list
        Collections.shuffle(lists);
        System.out.println("shuffle : "+lists);

        // copy
        List copyDest = new ArrayList();
        Collections.addAll(copyDest,new Object[lists.size()]);
        Collections.copy(copyDest,lists);
        System.out.println("copyDest:"+copyDest.toString());

        // 返回由指定对象的n副本  组成的list,也就是容器中的对象的内容一样
        List<String> qwe = Collections.nCopies(5, "qwe");
        System.out.println("size:"+qwe.size()+",content:"+qwe.toString());

    }

    public static void main(String[] args) {
        //natureSort();
        //getMax();
        //getMin();
        //customCompare();
        apiDemo();
    }
}
