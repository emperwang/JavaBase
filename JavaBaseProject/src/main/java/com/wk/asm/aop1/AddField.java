package com.wk.asm.aop1;


import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/*
class 文件结构
ClassFile {
    u4 magic;
    u2 minor_version;
    u2 major_version;
    u2 constant_pool_count;
    cp_info constant_pool[constant_pool_count-1];
    u2 access_flags;
    u2 this_class;
    u2 super_class;
    u2 interfaces_count;
    u2 interfaces[interfaces_count];
    u2 fields_count;
    field_info fields[fields_count];
    u2 methods_count;
    method_info methods[methods_count];
    u2 attributes_count;
    attribute_info attributes[attributes_count];
}
 */
public class AddField {
    private Class<?> clazz;
    private ClassReader cr;
    private ClassWriter cw;
    private ClassVisitor cv;
    private File classFile;
    private final static String Class_Suffix = ".class";

    public AddField(Class<?> clazz){
        this.clazz = clazz;
    }

    public void addPulicField(String fieldName,String fieldDesc) throws IOException {
        if (cr == null){
            cr = new ClassReader(clazz.getCanonicalName());
        }
        cw = new ClassWriter(cr,ClassWriter.COMPUTE_MAXS);
        cv = new AddFieldAdapter(cw, Opcodes.ACC_PUBLIC,fieldName,fieldDesc);
    }
    /*
       ClassReader用于读取 class文件,其具体的读取方法会委托与内部的一个ClassVisitor
       而从这里 cr.accept(cv,ClassReader.SKIP_DEBUG)可以看到,在这里委托的具体访问的类为AddFieldAdapter
       而AddFieldAdapter是ClassVisitor的子类,其中ClassVisitor是一个抽象类,其读取方法也会委托到内部的一个类
       new AddFieldAdapter(cw, Opcodes.ACC_PUBLIC,fieldName,fieldDesc)由此可知,ClassVisitor内部委托的读取类应该是ClassWriter
       而AddFieldAdapter重写了visitField 和 visitEnd 方法, 也就是说当执行visitField 和 visitEnd方法时,会具体执行AddFieldAdapter
       的方法,不会执行ClassWriter的方法; 当时访问class的其他方法时,就会委托到 ClassWriter
     */
    public void writeByteCode() throws IOException {
        cr.accept(cv,ClassReader.SKIP_DEBUG);
        byte[] bytes = cw.toByteArray();
        FileOutputStream outputStream = new FileOutputStream(getFile());
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    public File getFile(){
        if (classFile == null){
            StringBuilder builder = new StringBuilder();
            builder.append(clazz.getResource("/"))
                    .append(clazz.getCanonicalName().replace(".","/"))
                    .append(Class_Suffix);
            String filePath = builder.toString().substring(6);
            System.out.println(filePath);
            classFile = new File(filePath);
            return classFile;
        }
        return classFile;
    }

    public static void main(String[] args) throws IOException {
        AddField add = new AddField(Student.class);
        add.addPulicField("address","Ljava/lang/String;");
        add.addPulicField("tel","I");
        add.addPulicField("addr","Ljava/lang/String;");
        //add.getFile();
        add.writeByteCode();
    }
}

