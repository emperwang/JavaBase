package com.wk.asm.demo2;

import com.wk.asm.demo1.CustomLoader;
import jdk.internal.org.objectweb.asm.Type;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 生成的类
 * package com.agent.my3;
     public class Tester
     {
     public Integer getIntVal()
     {
     return 10;
     }
     }
 */
public class ASMGenerateStarted {
    // 动态创建一个类, 有一个无参构造函数
    public static ClassWriter createClassWriter(String className){
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        writer.visit(Opcodes.V1_8,Opcodes.ACC_PUBLIC,className,null,
                Type.getInternalName(Object.class),null);
        // 初始化 一个无参的构造函数
        MethodVisitor constructor = writer.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null,
                null);
        // 构造方法的第一步, aload 0 ; 记载隐含的this对象
        constructor.visitVarInsn(Opcodes.ALOAD,0);
        // 执行父类的init初始化
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL,Type.getInternalName(Object.class),
                "<init>","()V",false);
        //  当前方法返回void
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1,1);
        return writer;
    }

    public static byte[] createReturnMethod(String className,int reurnValue) throws NoSuchMethodException {
        ClassWriter writer = createClassWriter(className.replace(".","/"));
        // ()Ljava/lang/Integer;  表示函数没有参数,返回值为Integer,注意后面的分号,必须存在
        MethodVisitor getIntValMethod = writer.visitMethod(Opcodes.ACC_PUBLIC, "getIntVal", "()Ljava/lang/Integer;", null,
                null);
        // 将单字节常量值(-128~127)推送至栈顶(如果不是-128~127之间的数组，则不能使用bipush指令)
        getIntValMethod.visitIntInsn(Opcodes.BIPUSH,reurnValue);
        // 调用Integer的静态方法 valueOf把 10 转换为 Integer对象
        String methodDesc = Type.getMethodDescriptor(Integer.class.getMethod("valueOf", int.class));
        getIntValMethod.visitMethodInsn(Opcodes.INVOKESTATIC,Type.getInternalName(Integer.class),
                "valueOf",methodDesc,false);
        // 从当前方法返回对象引用
        getIntValMethod.visitInsn(Opcodes.ARETURN);
        getIntValMethod.visitMaxs(1,1);
        getIntValMethod.visitEnd();
        // 返回创建好的类的 字节码
        return writer.toByteArray();
    }

    public static void main(String[] args) throws NoSuchMethodException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        String dir = "H:\\FTPTest\\com\\wk\\asm\\";
        String destFile = "H:\\FTPTest\\com\\wk\\asm\\test2.class";
        String className = "com.wk.asm.test2";
        System.out.println("className final : "+className.replace(".","/"));
        System.out.println("className final : "+className.replaceAll(".","/"));
        File file = new File(dir);
        if (!file.exists()){
            file.mkdirs();
        }
        FileOutputStream outputStream = new FileOutputStream(destFile);

        byte[] classData = createReturnMethod(className, 20);
        outputStream.write(classData);
        outputStream.flush();
        outputStream.close();
        Class<?> aClass = new CustomLoader(className, classData).loadClass(className);
        Method getIntVal = aClass.getDeclaredMethod("getIntVal");
        Object invoke = getIntVal.invoke(aClass.newInstance());
        System.out.println("result is : "+invoke);
    }
}

























