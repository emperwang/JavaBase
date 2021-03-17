package com.wk.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author: ekiawna
 * @Date: 2021/3/17 17:27
 * @Description
 */
public class TestNewInstance {

    static class User{
        private String name;
        private int age;
        private static String address = "beijing";

        public User(){
            name = "xiaoming";
        }
        public String getName(){
            return name;
        }

        public String getAddress() {
            return address;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
    /*
    allocateInstance 创建对象时: 跳过对象的实例化阶段(通过构造函数), 忽略构造函数的安全检查(反射newInstance)
    当你需要某类的实例,但该类没有public的构造函数
     */
    public void testNewInstance() throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Unsafe unSafe = UnSafeUtil.getUnSafe();
        User user = new User();
        System.out.println(user.getName());
        //
        User user1 = User.class.newInstance();
        System.out.println(user1.getName());
        // allocateInstance 只是给对象分配了内存,它并不会初始化对象中的属性
        User user2 = (User) unSafe.allocateInstance(User.class);
        System.out.println(user2.getName());
    }

    public void testObject() throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Unsafe unSafe = UnSafeUtil.getUnSafe();
        // 通过allocateInstance 创建对象,为其分配内存地址, 不会加载构造函数
        User user = (User) unSafe.allocateInstance(User.class);
        System.out.println(user);

        // class && field
        Class<? extends User> userClass = user.getClass();
        Field name = userClass.getDeclaredField("name");
        Field age = userClass.getDeclaredField("age");
        Field address = userClass.getDeclaredField("address");

        // 获取实例域name 和 age在对象内存中的偏移量 并设置值
        System.out.println(unSafe.objectFieldOffset(name));
        unSafe.putObject(user,unSafe.objectFieldOffset(name),"honghong");

        System.out.println(unSafe.objectFieldOffset(age));
        unSafe.putInt(user, unSafe.objectFieldOffset(age),10);
        System.out.println(user);

        // 获取定义address字段的类
        Object staticFieldBase = unSafe.staticFieldBase(address);
        System.out.println("staticFieldbase: " + staticFieldBase);
        // 获取static 变量address的偏移量
        long staticFieldOffset = unSafe.staticFieldOffset(address);
        // 获取 static 变量address的值
        System.out.println(unSafe.getObject(staticFieldBase, staticFieldOffset));
        // 设置static 变量address的值
        unSafe.putObject(staticFieldBase, staticFieldOffset, "tianjin");
        System.out.println(user + "  " + user.getAddress());
    }

    public void testFieldOffset() throws NoSuchFieldException, IllegalAccessException {
        Unsafe unSafe = UnSafeUtil.getUnSafe();
        for (Field field : User.class.getDeclaredFields()) {
            // 当获取static field时  报错
            System.out.println(field.getName()+" - " + field.getType()+" : " + unSafe.objectFieldOffset(field));
        }
    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        TestNewInstance newInstance = new TestNewInstance();
        //newInstance.testNewInstance();

        //newInstance.testObject();
        //newInstance.testFieldOffset();
        long size = UnSafeUtil.sizeOf(new User());
        System.out.println(size);
    }
}
