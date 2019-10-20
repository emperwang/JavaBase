package com.wk.javassist;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JavassistBuildClass {

    /**
     * 代码创建一个user类
     */
    public void createClass() throws CannotCompileException, NotFoundException, IOException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        // create class
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass("com.wk.javassist.User");
        // add field
        CtField integerAge = CtField.make("private Integer age;", ctClass);
        ctClass.addField(integerAge);
        // add field
        CtField name = new CtField(pool.getCtClass("java.lang.String"), "name", ctClass);
        ctClass.addField(name);

        // add setter and getter
        ctClass.addMethod(new CtNewMethod().setter("setAge",integerAge));
        ctClass.addMethod(new CtNewMethod().getter("getAge",integerAge));
        ctClass.addMethod(new CtNewMethod().setter("setName",name));
        ctClass.addMethod(new CtNewMethod().getter("getName",name));

        // custom method
        CtMethod printInfo = CtMethod.make("public void printInfo(){ System.out.println(\"age = \"+this.age + \", name=\"+this.name);}",
                ctClass);
        ctClass.addMethod(printInfo);

        CtMethod printInfo2 = CtMethod.make("public int sum1(int a,int b){return a+b; }",
                ctClass);
        ctClass.addMethod(printInfo2);

        /**
         * public int sum(int a,int b){return a+b; }
         * 此处的  $1+$2表示参数 a+参数b, $0表示this
         */
        CtMethod sum = CtNewMethod.make(CtClass.intType, "sum", new CtClass[]{CtClass.intType, CtClass.intType},
                null, "{return $1+$2;}", ctClass);
        ctClass.addMethod(sum);

        CtMethod sum2 = CtNewMethod.make(Modifier.PUBLIC, CtClass.intType, "sum2", new CtClass[]{CtClass.intType, CtClass.intType},
                null, "{return $1+$2;}", ctClass);
        ctClass.addMethod(sum2);

        // add constructor
        /*CtConstructor noArgConstructor = new CtConstructor(null, ctClass);
        ctClass.addConstructor(noArgConstructor);*/
        /*CtConstructor noargConstruct = new CtNewConstructor().make("public User(){}", ctClass);
        ctClass.addConstructor(noargConstruct);

        CtConstructor constructor = new CtNewConstructor().make("public User(int age,String name){this.age = age;this.name = name;}", ctClass);
        ctClass.addConstructor(constructor);*/

        ctClass.writeFile("F:\\FTPTest");
        System.out.println("创建成功");

        // 反射调用
        Class useClass = ctClass.toClass();
        Object instance = useClass.newInstance();
        Method sum21 = useClass.getDeclaredMethod("sum2",int.class,int.class);
        Object res = sum21.invoke(instance, 1, 2);
        System.out.println(res.toString());
    }

    public static void main(String[] args) throws IOException, CannotCompileException, NotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JavassistBuildClass aClass = new JavassistBuildClass();
        aClass.createClass();
    }
}
