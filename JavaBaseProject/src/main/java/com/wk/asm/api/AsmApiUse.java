package com.wk.asm.api;

import jdk.internal.org.objectweb.asm.Type;

public class AsmApiUse {
    /**
     *  Type主要用于 java和jvm中的表示的转换
     */
    public static void TypeUse() throws NoSuchMethodException {
        // getType 是获取对jvm表示方法  , java -> jvm Type
        Type classType = Type.getType(String.class); // Ljava/lang/String;
        Type stringType = Type.getType(TeachDemo.class.getName()); // com.wk.asm.api.TeachDemo
        Type construcType = Type.getType(TeachDemo.class.getConstructor()); // ()V
        Type methodType = Type.getType(TeachDemo.class.getDeclaredMethod("print")); // ()V
        Type retMethodType = Type.getType(TeachDemo.class.getDeclaredMethod("print2",int.class,int.class));  // (II)I
        System.out.println("classType = "+classType +",  stringType="+stringType +", constructorType="
                +construcType +" , methodType = "+methodType+",retMethodType="+retMethodType);
        System.out.println("*********************************************************************");
        // jvm Type -> java
        Type javaStringType = Type.getType("Ljava/lang/String;"); //  Ljava/lang/String;
        Type voidType = Type.getType("()V");  // ()V
        Type javaClassTYpe = Type.getType("com.wk.asm.api.TeachDemo"); // com.wk.asm.api.TeachDemo
        Type retIntType = Type.getType("(II)I");// (II)I
        System.out.println("javaStringType = "+javaStringType +",  voidType="+voidType +", javaClassTYpe="
                +javaClassTYpe +" , retIntType = "+retIntType);
        System.out.println("*********************************************************************");

        String constructorDescriptor = Type.getConstructorDescriptor(TeachDemo.class.getConstructor());// ()V
        String descriptor = Type.getDescriptor(TeachDemo.class); // Lcom/wk/asm/api/TeachDemo;
        String internalName = Type.getInternalName(TeachDemo.class); // com/wk/asm/api/TeachDemo
        Type returnType = Type.getReturnType(TeachDemo.class.getDeclaredMethod("print2", int.class, int.class));// I
        String retTypeStr = retIntType.getReturnType().toString(); // I
        String descriptor1 = Type.getArgumentTypes("(II)I")[0].getDescriptor(); // I
        System.out.println("constructorDescriptor = "+constructorDescriptor +",  descriptor="+descriptor +", internalName="
                +internalName +" , returnType = "+returnType+" ,retTypeStr="+retTypeStr+",descriptor1="+descriptor1);

    }


    public static void main(String[] args) throws NoSuchMethodException {
        TypeUse();
    }
}
