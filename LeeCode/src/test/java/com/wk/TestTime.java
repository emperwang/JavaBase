package com.wk;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: wk
 * @Date: 2020/12/28 11:08
 * @Description
 */
public class TestTime {

    @Test
    public void testCalender(){
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //calendar.add(Calendar.MONTH,-5);
        System.out.println(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        //calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.add(Calendar.MONTH,1);
        System.out.println(calendar.getTime());
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.MONTH)+1);
    }

    @Test
    public void test2(){
        final Date time1 = new Date();
        System.out.println(time1.after(null));
    }

    @Test
    public void testQueue(){
        final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.add(4);
        queue.add(5);
        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println(queue.remove());
        System.out.println(Long.parseLong("60b05e798",16));
    }

    @Test
    public void testSort(){
        final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return -1;
            }
        });
        System.out.println(list);

        List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
        Collections.sort(list2, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return 1;
            }
        });
        System.out.println(list2);
    }
}
