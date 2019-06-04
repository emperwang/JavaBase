package com.wk.stream;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 创建stream的集中方式
 * 1. Collection.stream()
 * 2. Collection.parallelStream()
 * 3. Arrays.stream(T array)
 * 4. Stream.of()
 * 5. java.io.BufferedReader.lines()
 * 6. java.util.stream.IntStream.range()
 * 7. Randoms.ints()
 * 8. BitSet.stream
 * 9. Pattern.splitAsStream(java.lang.CharSequence)
 * 10.JarFile.stream()
 */
public class CreateStreamDemo {
    public static void main(String[] args) {
        // 1. Stream.of 也就是调用Arrays.stream实现的
        Stream<String> stream = Stream.of("a", "b", "c", "d");
        // 2. 通过数据创建
        String[] strings = {"a", "b", "c", "D"};
        Stream<String> stream1 = Stream.of(strings);
        Stream<String> stream2 = Arrays.stream(strings);
        // 3. 通过list容器创建(list和set都可创建,map不能)
        List<String> list = Arrays.asList(strings);
        Stream<String> stream3 = list.stream();
    }
}
