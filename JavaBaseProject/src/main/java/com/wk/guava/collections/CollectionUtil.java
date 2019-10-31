package com.wk.guava.collections;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CollectionUtil {

    public static void MultiSets(){
        HashMultiset<String> multiset = HashMultiset.create();
        multiset.add("a");
        multiset.add("b");
        multiset.add("c");
        multiset.add("d");
        multiset.add("e");
        multiset.add("f");
        multiset.add("a");
        multiset.add("b");
        multiset.add("c");
        multiset.add("d");
        multiset.add("e");
        multiset.add("f");
        // 元素b出现的次数
        System.out.println("Occurrence of b :"+multiset.count("b"));
        // 总个数，由此可见是允许出现数据重复
        System.out.println("Total size is :" + multiset.size());
        // 获取distinct value
        Set<String> elementSet = multiset.elementSet();
        System.out.print("Set [");
        for (String s : elementSet) {
            System.out.print(" " + s);
        }
        System.out.println("]");
        // 遍历器
        Iterator<String> iterator = multiset.iterator();
        System.out.print("Multiset iterator [");
        while (iterator.hasNext()){
            System.out.print(" " + iterator.next());
        }
        System.out.println(" ]");

        // entrySet 遍历
        Set<Multiset.Entry<String>> entries = multiset.entrySet();
        System.out.print("entries [ ");
        for (Multiset.Entry<String> entry : entries) {
            String element = entry.getElement();
            System.out.print(" "+ element);
        }
        System.out.println(" ] ");

        // remove 两个元素
        // 移除元素个数多于容器中的个数   不会报错
        multiset.remove("b",3);
        System.out.println("Occurence of b : "+multiset.count("b"));
    }

    public static void BiMap(){
        HashBiMap<Integer,String> hashBiMap = HashBiMap.create();
        hashBiMap.put(1,"hash1");
        hashBiMap.put(2,"hash2");
        hashBiMap.put(3,"hash3");
        System.out.println("hashMap : " + hashBiMap.toString());
        System.out.println("MapReverse : "+hashBiMap.inverse());
        System.out.println("reverse get : "+ hashBiMap.inverse().get("hash3"));
    }

    public static void HashBaseTableUt(){
        HashBasedTable<String, String, String> table = HashBasedTable.create();
        table.put("IBM","101","mahesh");
        table.put("IBM","102","Ramesh");
        table.put("IBM","103","Suresh");

        table.put("Microsoft","111","Sohan");
        table.put("Microsoft","112","Mohan");
        table.put("Microsoft","113","Rohan");

        table.put("TCS","121","Ram");
        table.put("TCS","122","Shyam");
        table.put("TCS","123","Susun");
        table.put("TCS","124","Sunlish");
        // get rows
        Map<String, String> ibm = table.row("IBM");
        System.out.print("IBM :[ ");
        for (Map.Entry<String, String> entry : ibm.entrySet()) {
            System.out.println("entry is :" + entry.getKey()+", name is :"+entry.getValue());
        }
        System.out.println(" ] ");
        System.out.println("=============================================");
        System.out.println("contains TCS 121 : "+table.contains("TCS","121"));
        System.out.println("=============================================");
        Set<String> keySet = table.rowKeySet();
        System.out.print(" key set is [ ");
        for (String s : keySet) {
            System.out.print(" " + s);
        }
        System.out.println(" ] ");
        System.out.println("=============================================");
        Map<String, String> column = table.column("102");
        System.out.print("column is :[ ");
        for (Map.Entry<String, String> entry : column.entrySet()) {
            System.out.print(" key is : "+entry.getKey()+",values is : "+entry.getValue());
        }
        System.out.println(" ] ");
    }


    public static void main(String[] args) {
//        MultiSets();
//        BiMap();
        HashBaseTableUt();
    }
}
