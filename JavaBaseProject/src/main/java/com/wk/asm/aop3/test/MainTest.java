package com.wk.asm.aop3.test;

import com.wk.asm.aop3.TimeCountAdapter;
import com.wk.asm.demo1.CustomLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainTest {
    private static final String file_suffix = ".class";
    public static void main(String[] args) throws IOException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {
        String baseDir = "H:/FTPTest";
        String name = StudentInfo.class.getName();
        String simpleName = StudentInfo.class.getSimpleName();
        String packagePath = StudentInfo.class.getPackage().getName().replace(".","/");
        baseDir = baseDir + "/" +packagePath;
        File file = new File(baseDir);
        if (!file.exists()){
            file.mkdirs();
        }
        String destFile = baseDir + "/" + simpleName+file_suffix;
        ClassReader reader = new ClassReader(name);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        TimeCountAdapter countAdapter = new TimeCountAdapter(writer);
        reader.accept(countAdapter, ClassReader.EXPAND_FRAMES);

        byte[] classBytes = writer.toByteArray();
        FileOutputStream outputStream = new FileOutputStream(destFile);
        outputStream.write(classBytes);
        outputStream.flush();
        outputStream.close();

        CustomLoader customLoader = new CustomLoader(name, classBytes);
        Class<?> aClass = customLoader.defineClass();
        Object instance = aClass.newInstance();
        Method printStuInfo = aClass.getDeclaredMethod("printStuInfo");
        printStuInfo.invoke(instance);

    }
}
