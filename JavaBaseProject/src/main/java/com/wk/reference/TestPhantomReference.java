package com.wk.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * @author: ekiawna
 * @Date: 2021/3/17 15:42
 * @Description
 */
public class TestPhantomReference {
    public static volatile boolean isRun = true;

    public static void main(String[] args) throws InterruptedException {
        String str = new String("123");
        System.out.println(str.getClass() + " @ " + str.hashCode());
        ReferenceQueue<String> referenceQueue = new ReferenceQueue<>();
        new Thread(() -> {
            while (isRun){
                Reference<? extends String> obj = referenceQueue.poll();
                if (obj != null){
                    try {
                        Field referent = Reference.class.getDeclaredField("referent");
                        referent.setAccessible(true);
                        Object result = referent.get(obj);
                        System.out.println("gc will collect: "
                                            + result.getClass()+"@"
                                            + result.hashCode()+"\t"
                                            +result);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        PhantomReference<String> phantomReference = new PhantomReference<>(str, referenceQueue);
        str = null;
        TimeUnit.SECONDS.sleep(2);
        System.gc();
        TimeUnit.SECONDS.sleep(2);
        isRun = false;
    }
}
