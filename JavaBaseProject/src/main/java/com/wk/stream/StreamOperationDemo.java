package com.wk.stream;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * foreach方式demo
 */
public class StreamOperationDemo {
    public static void main(String[] args) {
        //fucntionForEach();
        //funtionCollectionAndToArray();
        //functionMap();
        //functionFlatMap();
        //functionFilter();
        //functionPeek();
        functionFindFirst();
        //FunctionOptional();
        //functionReduce();
        //functionLimitSkip();
        //fucntionSorted();
        //fucntionMax();
        //functionDistinct();
        functionMatch();

    }

    private static void functionMatch() {
        List<Person> list = new ArrayList<Person>();
        for (int i = 10; i <= 30; i++){
            Person person = new Person(i, "name" + i);
            list.add(person);
        }
        boolean isAllAdult = list.stream()
                .allMatch(p -> p.getNo() > 18);
        System.out.println("All are adult? "+isAllAdult);

        boolean isThereAnyChild = list.stream()
                .anyMatch(p -> p.getNo() < 12);
        System.out.println("Any children? "+isThereAnyChild);
    }

    // 找出文件中不重复的单词，转小写，排序输出
    private static void functionDistinct() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("E:\\test.txt"));
            List<String> collect = reader.lines()
                    .flatMap(line -> Stream.of(line.split(" ")))
                    .filter(word -> word.length() > 0)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            System.out.println(collect);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 找出文件中最长字符串的长度
    private static void fucntionMax() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("E:\\test.txt"));
            int longest = reader.lines()
                    .mapToInt(String::length)
                    .max()
                    .getAsInt();
            reader.close();
            System.out.println(longest);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fucntionSorted() {
        List<Person> list = new ArrayList<Person>();
        for (int i = 0; i <= 1000; i++){
            Person person = new Person(i, "name" + i);
            list.add(person);
        }
        List<Person> collect = list.stream().sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).limit(2)
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     *  此程序中的limit并没有减少sort函数执行时的数量
     *  可以把liimit或skip放到前面执行，然后再执行sort ，可以有效的进行优化
     */
    private static void functionLimitSkip() {
        List<Person> list = new ArrayList<Person>();
        for (int i = 0; i <= 1000; i++){
            Person person = new Person(i, "name" + i);
            list.add(person);
        }
        List<String> strings = list.stream().map(Person::getName).limit(10).skip(3).collect(Collectors.toList());
        System.out.println(strings);
    }

    public static class Person{
        public int no;
        private String name;

        public Person(int no, String name) {
            this.no = no;
            this.name = name;
        }

        public int getNo() {
            return no;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "no=" + no +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    private static void functionReduce() {
        // 字符串拼接
        String reduceValue = Stream.of("A", "B", "C", "D").reduce("", String::concat);
        System.out.println("reduceValue:"+reduceValue);
        // 获取最小值
        Double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
        System.out.println("minValue:"+minValue);
        // 求和
        Integer sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
        System.out.println("sumValue:"+sumValue);
        // 求和
        sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
        System.out.println("sumValue:"+sumValue);
        // 字符过滤
        String concat = Stream.of("a", "B", "c", "D", "e", "F")
                .filter(x -> x.compareTo("Z") > 0)
                .reduce("", String::concat);
        System.out.println("concat:"+concat);
    }

    private static void FunctionOptional() {
        String strA="abcd",strB=null;
        print(strA);
        print(strB);
        print(String.valueOf(getLength(strA)));
        print(String.valueOf(getLength(strB)));
    }

    // Optional的函数示例
    private static int getLength(String val){
        Integer integer = Optional.ofNullable(val).map(String::length).orElse(-1);
        return integer;
    }
    // Optional的函数示例
    private static void print(String val){
        Optional.ofNullable(val).ifPresent(System.out::println);
    }
    // findFirst 函数
    private static void functionFindFirst() {

    }

    /**
     *  peek 把每个元素返回成一个stream
     */
    private static void functionPeek() {
        Stream.of("one","two","three","four")
                .filter(e->e.length()>3)
                .peek(e->System.out.println("Filtered value:"+e))
                .map(String::toUpperCase)
                .peek(e->System.out.println("Mapped value:"+e))
                .collect(Collectors.toList());
    }

    private static void functionFilter() {
        Integer[] nums = {1,2,3,4,5,6};
        Integer[] integers = Stream.of(nums).filter(n -> n % 2 == 0).toArray(Integer[]::new);
        Stream.of(integers).forEach(a->System.out.print(a));
    }

    private static void functionFlatMap() {
        Stream<List<Integer>> streams = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        );

        Stream<Integer> stream = streams.flatMap(a -> a.stream());
        stream.forEach(a->System.out.print(a));

    }

    /**
     *  把字符转换为大写
     */
    private static void functionMap() {
        Stream<String> stream = Stream.of("a", "b", "c", "d");
        List<String> list = stream.map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(list);
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
