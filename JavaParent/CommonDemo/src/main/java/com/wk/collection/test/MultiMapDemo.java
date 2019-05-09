package com.wk.collection.test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * multimap 就是一对多的一个映射关系,类似于Map<String,List<Object>, 但是这两个是不一样的
 */
public class MultiMapDemo {
    class Student{
        String name;
        int age;
    }

    private static final String CLASS_NAME_1 = "one_class";
    private static final String CLASS_NAME_2 = "two_class";
    Multimap<String,Student> multimap = ArrayListMultimap.create();

    public void testSudent(){
        for(int i=0;i<5;i++){
            Student student = new Student();
            student.name="Tome"+i;
            student.age = 6;
            multimap.put(CLASS_NAME_1,student);

        }

        for(int i=0;i<6;i++){
            Student student = new Student();
            student.name = "Jary"+i;
            student.age = 7;
            multimap.put(CLASS_NAME_2,student);
        }
        //遍历一年级的学生
        for(Student stu:multimap.get(CLASS_NAME_1)){
            System.out.println("一年级学生:name-:"+stu.name+",age-:"+stu.age);
        }

        //判断键值是否存在
        if (multimap.containsKey(CLASS_NAME_1)){
            System.out.println("键值包含:"+CLASS_NAME_1);
        }
        //键--单个值映射的个数(也就是映射的总数)
        System.out.println(multimap.size());  //11
        //不同键的个数
        System.out.println(multimap.keys().size()); //11
    }

    public static void main(String[] args) {
        MultiMapDemo mapDemo = new MultiMapDemo();
        mapDemo.testSudent();
    }
}
