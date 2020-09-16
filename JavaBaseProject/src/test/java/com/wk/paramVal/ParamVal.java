package com.wk.paramVal;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class ParamVal {

    /**
     * 验证解析uri地址：
     * 当uri地址为 spark://name_2:7077,也就是name中有_下划线时,会导致解析错误
     */
    @Test
    public void testUri() throws URISyntaxException {
        String str1="spark://name_2:7077";
        String str2="spark://name-2:7077";
        String str="spark://name2:7077";
        URI uri = new URI(str2);
        System.out.println(uri.getScheme());
        System.out.println(uri.getHost());
        System.out.println(uri.getPort());
    }

    @Test
    public void testsanmu(){
        String vv = 1==2?"yes":"no";
        System.out.println(vv);
    }

    /** 验证参数传递方式:
     *  值传递: String 加其他基本类型是值传递
     *  引用传递: 数组  list  map
     */
    // 参数传递中: 值传递  引用传递  验证
    @Test
    public void paramPass(){
        System.out.println("======================String=================================");
        String a = "123";
        testStr(a);
        System.out.println("String after :"+a);
        System.out.println("======================String   end=================================");

        System.out.println("======================Integer=================================");
        Integer iii = 10;
        testInt(iii);
        System.out.println("Integer after :"+iii);
        System.out.println("======================Integer   end=================================");

        System.out.println("======================Boolean=================================");
        Boolean bbb = true;
        testBoolean(bbb);
        System.out.println("Boolean after :"+bbb);
        System.out.println("======================Boolean   end=================================");


        System.out.println("=========================Array================================");
        String[] b = {"aa","bb"};
        testArray(b);
        System.out.println("Array after change: "+ Arrays.asList(b));
        System.out.println("=========================Array end===============================");

        System.out.println("=========================List================================");
        List<String> lists = new ArrayList<>();
        lists.add("first");
        lists.add("seconds");
        testList(lists);
        System.out.println("List after change  :" + lists.toString());
        System.out.println("=========================List end===============================");


        System.out.println("=========================Map================================");
        Map<String,String> maps = new HashMap<>();
        maps.put("one","aa");
        maps.put("two","bb");
        testMap(maps);
        System.out.println("Map after change: "+ maps.toString());
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

    public void testInt(Integer ii){
        System.out.println("rev int :" + ii);
        ii = 0;
        System.out.println("testInt change :" + ii);
    }

    public void testBoolean(Boolean bb){
        System.out.println("rev boolean : "+ bb);
        bb = false;
        System.out.println("testBoolean change :" + bb);
    }

}
