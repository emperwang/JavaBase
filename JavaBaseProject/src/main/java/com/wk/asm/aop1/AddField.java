package com.wk.asm.aop1;


import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

