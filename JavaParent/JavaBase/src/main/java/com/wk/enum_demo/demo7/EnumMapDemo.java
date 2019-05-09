package com.wk.enum_demo.demo7;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

enum Color{
    GREEN,RED,BLUE,YELLOW;
}

public class EnumMapDemo {
    public static void main(String[] args) {
        //统计每种颜色的数量
        EnumMap<Color, Integer> enumMap = new EnumMap<>(Color.class);

        List<Color> list = new ArrayList<>();
        list.add(Color.BLUE);
        list.add(Color.GREEN);
        list.add(Color.RED);

        for (Color str : list){
            Integer count = enumMap.get(str);
            if(count != null){
                enumMap.put(str,count+1);
            }else{
                enumMap.put(str,1);
            }
        }
        System.out.println(enumMap.toString());
    }
}
