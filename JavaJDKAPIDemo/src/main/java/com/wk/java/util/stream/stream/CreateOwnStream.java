package com.wk.java.util.stream.stream;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CreateOwnStream {
    public static void main(String[] args) {
        //GenerateRandomStream();
        //generateStream();
        //generateStream2();
        //functionGroupingBy();
        functionPartitioningBy();
    }

    private static void functionPartitioningBy() {
        Map<Boolean, List<Person>> collect = Stream.generate(new PersonSupplier())
                .limit(100)
                .collect(Collectors.partitioningBy(p -> p.getAge() < 18));
        System.out.println("Children number:" + collect.get(true).size());
        System.out.println("Adult number:"+collect.get(false).size());
    }

    private static void functionGroupingBy() {
        Map<Integer, List<Person>> collect = Stream.generate(new PersonSupplier())
                .limit(100)
                .collect(Collectors.groupingBy(Person::getAge));
        Set<Map.Entry<Integer, List<Person>>> entries = collect.entrySet();
        for (Map.Entry<Integer, List<Person>> entry : entries) {
            Integer key = entry.getKey();
            List<Person> value = entry.getValue();
            System.out.println("key:"+key+", values:"+value);
        }
    }

    private static void generateStream2() {
        Stream.iterate(0,n -> n+3).limit(10).forEach(System.out::println);
    }

    private static void generateStream() {
        Stream.generate(new PersonSupplier())
                .limit(10)
                .forEach(p -> System.out.println(p.getName()+","+p.getAge()));
    }

    protected static class PersonSupplier implements Supplier<Person>{
        private int index = 0;
        private Random random = new Random();
        @Override
        public Person get() {
            return new Person(index++,"StormTestUser"+index,random.nextInt(100));
        }
    }

    public static class Person{
        private int no;
        private String name;
        private int age;

        public Person(int no, String name, int age) {
            this.no = no;
            this.name = name;
            this.age = age;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "no=" + no +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    private static void GenerateRandomStream() {
        Random seed = new Random();
        Supplier<Integer> random = seed::nextInt;
        Stream.generate(random).limit(10).forEach(System.out::println);
        System.out.println();
        System.out.println("--------------");
        // Another way
        IntStream.generate(() -> (int)(System.nanoTime() % 100))
                .limit(20).forEach(System.out::println);
    }
}
