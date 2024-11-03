package com.wk;

import com.sun.tools.attach.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

// 添加 JDK/lib/tools.jar
@Slf4j
public class StarterJavaAgentMainAemain {
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException, InterruptedException {
        log.info("running jvm start...");
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {
            log.info(vmd.displayName());
            log.info(vmd.id());
            // 查找指定的虚拟机
            // 查找到 则把mainAgent 加载进去
            if (vmd.displayName().endsWith("com.wk.StarterJavaAgentMainAemain")){
                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                virtualMachine.loadAgent("D:\\project\\project-java\\project-base\\JavaBase\\javaAgentMainAgent\\target\\javaAgentMainDemo.jar");

                Thread.sleep(2000);

                virtualMachine.detach();
            }
        }
    }
}
