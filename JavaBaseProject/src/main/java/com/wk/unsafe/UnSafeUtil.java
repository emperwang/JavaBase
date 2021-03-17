package com.wk.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;

/**
 * @author: ekiawna
 * @Date: 2021/3/17 16:18
 * @Description
 */
public class UnSafeUtil {

    public static Unsafe getUnSafe() throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        return unsafe;
    }
    // 计算一个object的内存占用大小
    public static long sizeOf(Object o ) throws NoSuchFieldException, IllegalAccessException {
        Unsafe unSafe = getUnSafe();
        HashSet<Field> fields = new HashSet<>();
        Class<?> aClass = o.getClass();
        while (aClass != Object.class){
            for (Field field : aClass.getDeclaredFields()) {
                if ((field.getModifiers() & Modifier.STATIC) == 0){
                    fields.add(field);
                }
            }
            // 父类属性 也要计算
            aClass = aClass.getSuperclass();
        }
        /*
         计算每个字段的偏移量,因为第一个字段的偏移量即在对象头的基础上偏移的
         所以只需要比较当前偏移量最大的字段即表示这是对该对象最后一个字段的位置
         */
        long maxSize = 0;
        for (Field field : fields) {
            long offset = unSafe.objectFieldOffset(field);
            if (offset > maxSize){
                maxSize = offset;
            }
        }
        /*
        上面计算的是对象的最后要给字段的偏移量起始位置,java中对象最大长度是8个字节(long)
        这里计算方式时 将  当前偏移量/8+8 字节的padding
         */
        return ((maxSize/8)+1)*8;
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Unsafe unSafe = UnSafeUtil.getUnSafe();
        System.out.println(unSafe);
    }
}
