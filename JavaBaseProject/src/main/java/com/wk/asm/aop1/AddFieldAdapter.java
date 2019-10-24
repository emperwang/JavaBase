package com.wk.asm.aop1;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class AddFieldAdapter extends ClassVisitor{
    private int accessModifier;
    private String name;
    private String desc;
    private boolean isFieldPresent = false;

    public AddFieldAdapter(ClassVisitor cv,int accessModifier,String name,
                           String desc){
        super(Opcodes.ASM5,cv);
        this.accessModifier = accessModifier;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        if (name.equals(this.name)){
            isFieldPresent = true;
        }
        return cv.visitField(access,name,desc,signature,value);
    }

    @Override
    public void visitEnd() {
        if (!isFieldPresent){
            // 写入属性
            FieldVisitor fieldVisitor = cv.visitField(accessModifier, name, desc, null, null);
            if (fieldVisitor != null){
                fieldVisitor.visitEnd();
            }
        }
        cv.visitEnd();
    }
}
