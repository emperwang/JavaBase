package com.wk;

import lombok.extern.slf4j.Slf4j;

// 设置 vm参数  -javaagent:E:/code-workSpace/project-javaBase/JavaBase/javaAgentMainAgent/javaAgentDemo.jar
@Slf4j
public class StarterJavaAgentPremain {
    public static void main(String[] args) {
        log.info("main start");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("main end");
    }
}
