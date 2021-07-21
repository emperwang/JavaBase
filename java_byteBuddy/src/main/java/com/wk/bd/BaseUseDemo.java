package com.wk.bd;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author: Sparks
 * @Date: 2021/7/20 14:57
 * @Description
 */
public class BaseUseDemo {

    public static void outputClass(byte[] bytes, int num){
        String basePath = BaseUseDemo.class.getResource("/").getPath();
        String filePath = new StringBuilder(basePath).append("ByteBuddyDemo_").append(num).append(".class").toString();
        try (FileOutputStream outputStream = new FileOutputStream(new File(filePath))){
            System.out.println("file path: " + filePath);
            outputStream.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void baseUse() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        // 1. 开始创建
        DynamicType.Unloaded<Object> unloaded = new ByteBuddy()
                // 定义继承的父类
                .subclass(Object.class)
                // 定义命令控件
                .name("com.bd")
                .make();
        // 输出字节码
        outputClass(unloaded.getBytes(),1);

        // 2. 开始创建第二个类
        DynamicType.Unloaded<Object> unloaded1 = new ByteBuddy()
                // 定义父类
                .subclass(Object.class)
                // 命名空间
                .name("com.bd.demo2")
                // 定义方法
                .defineMethod("main", void.class, Modifier.PUBLIC + Modifier.STATIC)
                // 定义方法参数
                .withParameter(String[].class, "args")
                // 定一个局部变量
                .intercept(FixedValue.value("hellow world!"))
                // 创建
                .make();
        // 输出字节码
        outputClass(unloaded1.getBytes(),2);

        // 3. 开始创建第三个类，  委托函数的使用
        DynamicType.Unloaded<Object> uploaded3 = new ByteBuddy()
                // 定义父类
                .subclass(Object.class)
                // 定义命名空间
                .name("com.db.demo3")
                // 定义方法
                .defineMethod("main", void.class, Modifier.PUBLIC + Modifier.STATIC)
                // 定义方法参数
                .withParameter(String[].class, "args")
                // 定义委托的类, 委托HiMain的main方法
                .intercept(MethodDelegation.to(HiMain.class))
                .make();
        // 输出字节码
        outputClass(uploaded3.getBytes(), 3);
        // 加载类
        Class<?> loaded = uploaded3.load(BaseUseDemo.class.getClassLoader()).getLoaded();
        Method main = loaded.getMethod("main", String[].class);
        main.invoke(loaded.newInstance(), (Object)new String[]{});
    }

    public static void use() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        // 动态类
        DynamicType.Unloaded<BizMethod> methodUnloaded = new ByteBuddy()
                // 指定父类
                .subclass(BizMethod.class)
                // 要拦截的方法
                .method(ElementMatchers.named("queryUserInfo"))
                // 方法
                .intercept(MethodDelegation.to(MonitorDemo.class))
                .make();
        outputClass(methodUnloaded.getBytes(), 4);
        // 加载类
        Class<? extends BizMethod> loaded = methodUnloaded.load(BaseUseDemo.class.getClassLoader()).getLoaded();
        // 调用方法, 测试检测结果
        BizMethod bizMethod = new BizMethod();
        System.out.println(bizMethod.queryUserInfo("10001", "asdinfo"));
        // 反射调用才有效果
        Method queryUserInfo = loaded.getMethod("queryUserInfo", String.class, String.class);
        queryUserInfo.invoke(loaded.newInstance(),"1002","full stack");
    }

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        baseUse();
        use();
    }
}
