package com.wk.paramVal;

import org.junit.Test;

import java.util.*;

public class ParamVal {

    /** 验证参数传递方式:
     *  值传递: String 加其他基本类型是值传递
     *  引用传递: 数组  list  map
     */
    // 参数传递中: 值传递  引用传递  验证
    @Test
    public void paramPass(){
        System.out.println("======================String=================================");
        String a = "123";
        System.out.println("before : " + a);
        testStr(a);
        System.out.println("after :"+a);
        System.out.println("======================String   end=================================");

        System.out.println("=========================Array================================");
        String[] b = {"aa","bb"};
        testArray(b);
        System.out.println("after change: "+ Arrays.asList(b));
        System.out.println("=========================Array end===============================");

        System.out.println("=========================List================================");
        List<String> lists = new ArrayList<>();
        lists.add("first");
        lists.add("seconds");
        testList(lists);
        System.out.println("after change  :" + lists.toString());
        System.out.println("=========================List end===============================");


        System.out.println("=========================Map================================");
        Map<String,String> maps = new HashMap<>();
        maps.put("one","aa");
        maps.put("two","bb");
        testMap(maps);

        System.out.println("=========================Map end===============================");

    }

    public void testMap(Map<String,String> maps){
        System.out.println("testMap rev param: "+ maps.toString());
        maps.put("testMap","changeVal");
        System.out.println("testMap change : " + maps.toString());
    }

    public void testList(List lists){
        List tmp = lists;
        System.out.println("testList rev param : " + lists.toString());
        tmp.add("testList");
        System.out.println("testList tmp change :" + tmp.toString());
    }

    public void testArray(String[] arr){
        System.out.println("testArray rev param :"+ Arrays.asList(arr));
        arr[0] = "testArray";
        System.out.println("testArray change: " + Arrays.asList(arr));
    }

    public void testStr(String b){
        System.out.println("recv b :" + b);
        b = "10000";
        System.out.println("testStr change : "+b);
    }

}
