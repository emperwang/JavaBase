package com.wk;

import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

@Slf4j
/**
 *  此函数会在 真正的main函数执行前加载并执行，所以可以在这里多class文件进行修改等操作
 *  参考: https://www.cnblogs.com/rickiyang/p/11368932.html
 */
public class PreMainTraceAgent {

    public static void premain(String agentArgs, Instrumentation inst){
        log.info("agentArgs: {}",agentArgs);
        inst.addTransformer(new DefineTransformer(),true);
    }

    static class DefineTransformer implements ClassFileTransformer{

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            log.info("premain load class: {}",className);
            return classfileBuffer;
        }
    }
}
