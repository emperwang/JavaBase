package com.wk.reflect.caller;

import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

/**
 * @author: wk
 * @Date: 2020/11/23 14:09
 * @Description
 */
public class Invoked {
    /*
    此注释是由特权用户加载才能使用,可以设置-Xbootclasspath参数 让加载rt.jar的加载器加载此类,就可以使用了
    -Xbootclasspath:bootclasspath ：让jvm从指定的路径中加载bootclass，用来替换jdk的rt.jar。一般不会用到。
         -Xbootclasspath/a: path ： 被指定的文件追加到默认的bootstrap路径中。
         -Xbootclasspath/p: path ： 让jvm优先于默认的bootstrap去加载path中指定的class。
     */
    // 本次设置 -Xbootclasspath/a:C:\code-workspace\source\JavaBase\JavaBaseProject\target\classes
    @CallerSensitive
    public void print(){
        final Class<?> callerClass = Reflection.getCallerClass();
        System.out.println("this is Invoked print and the call is: " + callerClass.getName());
    }
}
