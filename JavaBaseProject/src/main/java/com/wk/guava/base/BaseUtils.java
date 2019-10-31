package com.wk.guava.base;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BaseUtils {

    public static void OptionalUse(){
        // 1.创建一个optional
        Optional<Integer> ops = Optional.of(5);

        // 1-2.获取optional的值
        Integer integer = ops.get();
        System.out.println("get valus is :" + integer);

        // 1-3. 把包含的对象转换集合
        Set<Integer> integers = ops.asSet();
        System.out.println("asSets value is :" + integers.toString());

        // 1-4 引用存在不为null 则返回true
        boolean present = ops.isPresent();
        System.out.println("present is :"+present);

        // 1-5 如果引用不存在,则返回10(相当于设置了默认值)
        Integer or = ops.or(10);
        System.out.println("or valus is :"+or);

        // 1-6 如果引用缺失，则返回null
        Integer orNull = ops.orNull();
        System.out.println("ornull is :"+orNull);

        // 2.创建一个引用确实的实例
        Optional<Object> ops2 = Optional.absent();
        // 3.从一个可能为null的对象创建Optional实例
        Optional<Object> nullOpt = Optional.fromNullable(null);
    }

    public static void PrecondsitionUtil(){
        int i = 10;
        String str = "abcdefghijklmnopqrstuvxyz";
        // 检查参数, 并打印具体的message提示信息
        Preconditions.checkArgument(i>0,"Auguments was %s but expected nonnegative",i);

        // 检查value是否为null
        Integer integer = Preconditions.checkNotNull(i);
        System.out.println("checkNotNull : "+integer);
        // 检查对象的状态
        Preconditions.checkState(i>0);

        // 检车index作为索引值对应某个列表，字符串或数组是否有效。
        // index >=0 && index<size
        int i1 = Preconditions.checkElementIndex(1, str.length());
        System.out.println("checkElementIndex :" + i1);

        // 检查index作为位置值对某个列表，字符串或数组是否有效  index>=0 && index <= size
        int i2 = Preconditions.checkPositionIndex(10, str.length());
        System.out.println("checkPositionIndex :"+i2);

        // 检查 [start,end] 表示的位置范围对某个列表,字符串或数组是否有效
        Preconditions.checkPositionIndexes(1,100,str.length());
    }

    public static void Orderutil(){
        List<Integer> lists = new ArrayList<>();
        lists.add(5);
        lists.add(6);
        lists.add(15);
        lists.add(8);
        lists.add(10);
        lists.add(12);
        lists.add(4);

        // 自然排序
        Ordering<Comparable> natural = Ordering.natural();
        System.out.println("before sort: "+lists.toString());
        Collections.sort(lists,natural);
        System.out.println("after sort: "+ lists.toString());
        System.out.println("=================================================");

        System.out.println("list is sorted :"+natural.isOrdered(lists));
        System.out.println("Max num is :" + natural.max(lists));
        System.out.println("min num is :" + natural.min(lists));
        System.out.println("=================================================");
        Collections.sort(lists,natural.reverse());
        System.out.println("reverse order :"+lists.toString());
        System.out.println("=================================================");
        lists.add(null);
        System.out.println("Null added to Sorted list : " + lists.toString());
        Collections.sort(lists,natural.nullsFirst());
        System.out.println("Null first is : "+lists.toString());
        Collections.sort(lists,natural.nullsLast());
        System.out.println("Null last is :"+lists.toString());
        System.out.println("=================================================");

        List<String> names = new ArrayList<>();
        names.add("Ram");
        names.add("Shyam");
        names.add("Mohan");
        names.add("Sohan");
        names.add("Ramesh");
        names.add("Suresh");
        names.add("Naresh");
        names.add("Mahesh");
        names.add(null);
        names.add("Vikas");
        names.add("Deepak");
        System.out.println("string list : "+names.toString());
        Collections.sort(names,natural.nullsFirst().reverse());
        System.out.println("sort names :"+names.toString());
    }

    public static void RangeUtil(){
        // create a range[a,b] = {a <= x <= b}
        Range<Integer> closed = Range.closed(0, 9);
        System.out.println("closed :"+closed.toString());
        PrintRange(closed);
        // tranform
        System.out.println("5 is present :" + closed.contains(5));
        System.out.println("(123) is present : "+closed.containsAll(Ints.asList(1,2,3)));
        System.out.println("Lower Bound : "+ closed.lowerEndpoint());
        System.out.println("Upper Bound : "+ closed.upperEndpoint());
        System.out.println("======================================================");
        // (0,9) => {0 < value < 9}
        Range<Integer> open = Range.open(0, 9);
        PrintRange(open);
        System.out.println("======================================================");
        // [0,9)
        Range<Integer> closedOpen = Range.closedOpen(0, 9);
        System.out.println("closePrint : ");
        PrintRange(closedOpen);
        System.out.println("======================================================");
        //  (9,infinity)
        Range<Integer> greaterThan = Range.greaterThan(9);
        System.out.println("(9,infinity");
        System.out.println("Lower Bound : "+greaterThan.lowerEndpoint());
        System.out.println("Upper Bound : "+ greaterThan.hasUpperBound());
        System.out.println("======================================================");
        Range<Integer> closed35 = Range.closed(3, 5);
        // 是否是子串
        System.out.println("[0,9] encloses [3,5] :" + closed.encloses(closed35));
        System.out.println("======================================================");
        Range<Integer> closed920 = Range.closed(9, 20);
        System.out.println("[0,9] is connected [9,20] : "+ closed.isConnected(closed920));
        System.out.println("======================================================");
        Range<Integer> closed515 = Range.closed(5, 15);
        // 求交集
        PrintRange(closed.intersection(closed515));
        System.out.println("======================================================");
        // span, 是求 合集
        System.out.println("span : ");
        PrintRange(closed.span(closed515));
    }

    public static void PrintRange(Range<Integer> range){
        ContiguousSet<Integer> integers = ContiguousSet.create(range, DiscreteDomain.integers());
        UnmodifiableIterator<Integer> iterator = integers.iterator();
        for (Integer integer : integers) {
            System.out.print(" integer : "+integer);
        }
        System.out.println();
    }

    public static void main(String[] args) {
//        OptionalUse();
//        PrecondsitionUtil();
//        Orderutil();
        RangeUtil();
    }
}
