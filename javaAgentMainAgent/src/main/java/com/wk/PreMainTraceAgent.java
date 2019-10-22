package com.wk;

import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
@Slf4j
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
