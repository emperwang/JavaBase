package com.wk;

import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: Sparks
 * @Date: 2021/7/21 14:44
 * @Description
 */
public class sanmuTest {

    @Test
    public void test1() {
        String str = 1 > 0?"greater 0": "less then 0";
        System.out.println(str);

        String format = "<if test=\"%s != null and %s != ''\">%s</if>";
        String res = String.format(format,"name");
        System.out.println(res);
    }


    @Test
    public void test2(){
        String collect = Arrays.asList("", "name2", "name3").stream().map(i -> i.toUpperCase())
                .filter(Objects::nonNull).collect(Collectors.joining("\n"));
        System.out.println(collect);
    }
}
