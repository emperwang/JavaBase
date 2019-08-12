package com.wk.securityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FilePermission;

/**
 *  对于java-security-manager的测试
 *  虚拟机参数设置 -Djava.security.manager : 让securityManager类加载(sun.misclauncher函数中实现)
 *  -Djava.security.policy=D:/my.policy   :设置策略文件位置
 *
 *  接下来需要补充权限的配置项
 */
public class JavaSecurityManager {
    private static Logger logger = LoggerFactory.getLogger(JavaSecurityManager.class);
    public static void main(String[] args) {
        checkPermission();
    }

    public static void checkPermission(){
        SecurityManager sm = System.getSecurityManager();
        if(sm!= null){
            FilePermission permission = new FilePermission("D:/gc.log", "read,write");
            sm.checkPermission(permission);
            logger.info(permission.getName() + " has read permission");
        }else {
            logger.info("security is null");
        }
    }
}
