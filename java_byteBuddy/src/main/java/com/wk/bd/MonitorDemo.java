package com.wk.bd;

import com.google.gson.Gson;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author: Sparks
 * @Date: 2021/7/20 15:42
 * @Description
 */
public class MonitorDemo {

    /**
     * @RuntimeType: 定义运行时的 目标方法
     *
     * @SuperCall   调用父类版本的方法
     * @AllArguments  绑定所有参数的数组
     * @Argument    绑定单个参数, 0表示第一个参数
     * @This        当前被拦截的, 动态生成的那个对象
     * @Super       当前被拦截的,动态生成的那个对象的父类对象
     * @Origin      具备多种用法, 如下:
     *                  - Method: 被调用的原始方法
     *                  - Constructor: 被调用的原始构造器
     *                  - Class 当前动态创建的类
     *                  - MethodHandle MethodType String 动态类的toString的返回值
     * @DefaultCall     调用默认方法而非super的方法
     * @Super           注入父类型对象,可以使接口,从而可以调用它的任何方法
     * @Empty         注入参数的类型的 默认值
     * @StudValue       注入一个存根值. 对于返回引用,void的方法, 注入null, 对于返回原始类型的方法,注入0
     * @FieldValue      注入被拦截对象的一个字段的值
     * @Morph           类似于 @SuperCall, 但是允许指定调用参数
     *
     */
    @RuntimeType
    public static Object intercept(@SuperCall Callable<?> callable,
                                   @AllArguments Object[] args,
                                   @Argument(0) Object uid,
                                   @This Object thisObj,
                                   @Super Object parentObj,
                                   @Origin Method method){

        long start = System.currentTimeMillis();
        Object call = null;
        try {
            // 调用原始方法
             call = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Gson gson = new Gson();
            System.out.println("=============================monitor demo========================");
            System.out.println("@AllArguments获取所有参数:" + gson.toJson(args));
            System.out.println("@Argument(0) 获取第一个参数结果:" + uid);
            System.out.println("@This 当前对象 spanId:" + ((BizMethod)thisObj).spandId());
            System.out.println("@Super 父类对象结果: " + parentObj.hashCode());
            System.out.println("@Origin 方法名称: " + method.getName());
            System.out.println("@Origin 入参个数: " + method.getParameterCount());
            System.out.println("@Origin 入参类型: " + method.getParameterTypes()[0].getTypeName());
            System.out.println("@Origin 出参类型: " + method.getReturnType().getTypeName());
            System.out.println("方法耗时: " + (System.currentTimeMillis() - start) + "ms");
            System.out.println("=============================monitor demo========================");
        }
        return call;
    }
}
