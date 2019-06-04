package com.wk.java.util.stream.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * foreach方式demo
 */
public class StreamOperationDemo {
    public static void main(String[] args) {
        //fucntionForEach();
        funtionCollectionAndToArray();
    }

    private static void funtionCollectionAndToArray(){
        Stream<String> stream = Stream.of("a", "b", "c", "d");
        // 1.转换为数组
        Object[] objects = stream.toArray();
        System.out.println(objects);
        // 2. 转换为list
        Stream<String> stream2 = Stream.of("a", "b", "c", "d");
        List<String> lists = stream2.collect(Collectors.toList());
        System.out.println(lists);
        // 3. 转换为ArrayList
        Stream<String> stream3 = Stream.of("a", "b", "c", "d");
        ArrayList<String> lists2 = stream3.collect(Collectors.toCollection(ArrayList::new));
        System.out.println(lists2);
        // 4. 转换为String
        Stream<String> stream4 = Stream.of("a", "b", "c", "d");
        String s = stream4.collect(Collectors.joining()).toString();
        System.out.println(s);
    }

    /**
     *  创建stream并使用foreach遍历
     */
    private static void fucntionForEach() {
        IntStream.of(1,2,3,4,5).forEach(System.out::print);
        System.out.println();
        IntStream.of(1,2,3,4,5,6,7,8).forEach(a->System.out.print(a));
        System.out.println();
        IntStream.range(1,4).forEach(a->System.out.print(a));
        System.out.println();
        IntStream.rangeClosed(1,8).forEach(a->System.out.print(a));
    }
}
