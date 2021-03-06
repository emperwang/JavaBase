package com.wk.asm.demo1;

import javassist.bytecode.Opcode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ASMGettingStarted {

    public static ClassWriter createClassWriter(String className){
        // new ClassWriter(0) 参数为0,表示方法栈长度和本地变量表由用户自己计算; COMPUTE_MAXS表示最大
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        // 1.8   public  父类为Object 没有接口
        // Opcodes.V1_8版本号
        // Opcodes.ACC_PUBLIC 表示 public 类
        // classname 表示全限定类名
        // 第一个null 表示 泛型
        // java/lang/Object 表示父类
        // 最后一个null 表示接口
        classWriter.visit(Opcodes.V1_8,Opcodes.ACC_PUBLIC,className,null,
                "java/lang/Object",null);
        // 构造函数
        MethodVisitor constructor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V",
                null, null);
        constructor.visitVarInsn(Opcodes.ALOAD,0);
        // 调用父类构造函数
        constructor.visitMethodInsn(Opcode.INVOKESPECIAL,
                "java/lang/Object","<init>","()V",false);
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1,1);
        constructor.visitEnd();
        return classWriter;
    }

    public static byte[] createVoidMethod(String className,String message){
        // 把className中的.修改为/
        ClassWriter writer = createClassWriter(className.replace(".", "/"));
        // 创建一个方法   ()V 表示没有返回值
        MethodVisitor runMethod = writer.visitMethod(Opcodes.ACC_PUBLIC, "run", "()V", null, null);
        // 获取 java.io.PrintStream 对象
        runMethod.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;");
        // 将 int float String型常量从常量池推送至栈顶（此处将message字符串从常量池中推送至栈顶）
        runMethod.visitLdcInsn(message);
        // 执行 println方法
        // 最后一个参数 false, 表示是否是接口
        runMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println",
                "(Ljava/lang/String;)V",false);
        runMethod.visitInsn(Opcodes.RETURN);
        runMethod.visitMaxs(1,1);
        runMethod.visitEnd();

        return writer.toByteArray();
    }

    /**   最终构建的类
     * package com.wk.asm;
     import java.io.PrintStream;
     public class test
     {
     public void run()
     {
     System.out.println("This is my First ASM test");
     }
     }
     * @param args
     */
    /*  反汇编
    public class com.wk.asm.test {
      public com.wk.asm.test();
        descriptor: ()V
        Code:
           0: aload_0
           1: invokespecial #8                  // Method java/lang/Object."<init>":
    ()V
           4: return

      public void run();
        descriptor: ()V
        Code:
           0: getstatic     #15                 // Field java/lang/System.out:Ljava/
    io/PrintStream;
           3: ldc           #17                 // String This is my First ASM test
           5: invokevirtual #23                 // Method java/io/PrintStream.printl
    n:(Ljava/lang/String;)V
           8: return
    }
     */
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String dir = "H:\\FTPTest\\com\\wk\\asm\\";
        String destFile = "H:\\FTPTest\\com\\wk\\asm\\test.class";
        String className = "com.wk.asm.test";
        byte[] classData = createVoidMethod(className, "This is my First ASM test");
        File file = new File(dir);
        if (!file.exists()){
            file.mkdirs();
        }
        FileOutputStream outputStream = new FileOutputStream(destFile);
        outputStream.write(classData);
        outputStream.flush();
        outputStream.close();
        CustomLoader customLoader = new CustomLoader(className, classData);
        Class<?> aClass = customLoader.loadClass(className);
        //aClass.getMethods()[0].invoke(aClass.newInstance());
        Method run = aClass.getDeclaredMethod("run");
        run.invoke(aClass.newInstance());
    }
}























