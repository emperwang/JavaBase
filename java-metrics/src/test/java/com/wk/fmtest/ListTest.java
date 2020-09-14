package com.wk.fmtest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListTest {
    @Test
    public void listString(){
        List<String> list = new ArrayList<>();
        list.add("abc");
        list.add("edf");
        String str = list.toString();
        System.out.println("str = " + str);

        System.out.println(str.replaceAll("\\[","{").replaceAll("]","}"));
    }
}
