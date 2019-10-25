package com.wk.asm.aop3;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class LogTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try{
            ClassReader cr = new ClassReader(className);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            TimeCountAdapter timeCountAdapter = new TimeCountAdapter(cw);
            cr.accept(timeCountAdapter,ClassReader.EXPAND_FRAMES);

            return cw.toByteArray();
        }catch (Exception e){

        }
        return null;
    }
}
