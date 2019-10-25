package com.wk.asm.aop3;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class TimeCountAdapter extends ClassVisitor implements Opcodes {
    private String owner;
    private boolean isInterface;
    public static String fieldName = "UDASMCN";
    private int Access = Opcodes.ACC_PUBLIC +Opcodes.ACC_STATIC+Opcodes.ACC_FINAL;
    private boolean isPresent = false;

    private String methodName;

    public TimeCountAdapter( ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        super.visit(version,Access,name,signature,superName,interfaces);
        owner = name;
        isInterface = (access & ACC_INTERFACE) != 0;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
            if (name.equals(fieldName)){
                isPresent = true;
            }
            return super.visitField(access,name,desc,signature,value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (!isPresent && mv != null && !name.equals("<init>")
                && !name.equals("<cinint>")){
            methodName = name;
            AddTimerMethodAdaptor adaptor = new AddTimerMethodAdaptor(mv);
            adaptor.aa = new AnalyzerAdapter(owner,access,name,desc,adaptor);
            adaptor.lvs = new LocalVariablesSorter(access,desc,adaptor.aa);

            return adaptor.lvs;
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        if (!isInterface){
            FieldVisitor fieldVisitor = cv.visitField(Access, fieldName, "Ljava/lang/String;",
                    null, owner);
            if (fieldVisitor !=null){
                fieldVisitor.visitEnd();
            }
            cv.visitEnd();
        }
    }

    class AddTimerMethodAdaptor extends MethodVisitor{
        private int time;
        private int maxStack;
        private LocalVariablesSorter lvs;
        private AnalyzerAdapter aa;
        public AddTimerMethodAdaptor(MethodVisitor mv) {
            super(Opcodes.ASM5, mv);
        }

        @Override
        public void visitCode() {
            mv.visitCode();
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,"java/lang/System","nanoTime",
                    "()J",false);
            time = lvs.newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(Opcodes.LSTORE,time);
            maxStack = 4;
        }

        @Override
        public void visitInsn(int opcode) {
            if (((opcode >= Opcodes.IRETURN && opcode <= RETURN) || opcode == Opcodes.ATHROW) && !isPresent){
                mv.visitMethodInsn(Opcodes.INVOKESTATIC,"java/lang/System",
                        "nanoTime","()J",false);
                mv.visitVarInsn(LLOAD,time);
                mv.visitInsn(Opcodes.LSUB);
                mv.visitVarInsn(Opcodes.LSTORE,time);
                mv.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;");
                mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                mv.visitInsn(DUP);
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                mv.visitFieldInsn(GETSTATIC, owner, fieldName, "Ljava/lang/String;");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

                mv.visitLdcInsn("  "+methodName+":");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

                mv.visitVarInsn(LLOAD, time);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

                maxStack=Math.max(aa.stack.size()+4,maxStack);
            }
            mv.visitInsn(opcode);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(Math.max(maxStack,this.maxStack), maxLocals);
        }
    }
}




















