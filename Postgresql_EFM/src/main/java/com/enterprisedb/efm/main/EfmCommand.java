package com.enterprisedb.efm.main;

import com.enterprisedb.efm.EfmController;
import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.Version;
import com.enterprisedb.efm.utils.PasswordManager;
import com.enterprisedb.efm.utils.Upgrade;
import com.enterprisedb.efm.utils.Utils;
import java.security.InvalidKeyException;
import java.util.Arrays;

public final class EfmCommand extends AbstractCommand {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("No command line parameters specified.");
            return;
        }
        if (args[0].startsWith("-")) {
            printMessage(args[0]);
            return;
        }
        if (args.length < 2) {
            System.err.println("[ERROR] Missing arguments.");
            printHelp();
            System.exit(-1);
        }
        SubCommand command = SubCommand.getCommand(args, "Error: unknown command '" + args[0] + "'. Run 'efm --help' for more information.");
        if (command == null)
            System.exit(-1);
        if (command == SubCommand.ENCRYPT) {
            if (args.length > 2) {
                if ("--from-env".equals(args[2])) {
                    if (!encryptPasswordFromEnv(args[1]))
                        System.exit(-1);
                } else {
                    System.err.println("Unknown option for encrypt command: " + args[2]);
                    System.exit(-1);
                }
            } else if (!encryptPassword(args[1])) {
                System.exit(-1);
            }
            return;
        }
        if (command == SubCommand.UPGRADE) {
            Upgrade upgrade = new Upgrade();
            if (!upgrade.run(args[1]))
                System.exit(-1);
            return;
        }
        if (command == SubCommand.PROP) {
            Environment.getEnvironment().init(getPropsLocation(args[1]));
            try {
                Environment.getEnvironment().outputPropertyCheck();
            } catch (RuntimeException e) {
                return;
            }
            return;
        }
        if (!Environment.getEnvironment().init(getPropsLocation(args[1]), command))
            System.exit(1);
        if ((command == SubCommand.ADD_NODE || command == SubCommand.REMOVE_NODE || command == SubCommand.ALLOW_NODE || command == SubCommand.DISALLOW_NODE || command == SubCommand.SET_PRIORITY || command == SubCommand.STOP_CLUSTER || command == SubCommand.PROMOTE || command == SubCommand.FORCE_PROMOTE || command == SubCommand.RESUME) && !Utils.hasControllerPermission())
            System.exit(-1);
        if (command == SubCommand.ALLOW_NODE || command == SubCommand.DISALLOW_NODE) {
            if (args.length < 3) {
                System.err.println(String.format("The %s command requires ip information.", new Object[] { command.getValue() }));
                System.exit(1);
            }
            String ip = args[2];
            EfmController controller = new EfmController();
            if (command == SubCommand.ALLOW_NODE) {
                if (!controller.runAllowNode(ip))
                    System.exit(1);
            } else if (!controller.runDisallowNode(ip)) {
                System.exit(1);
            }
        } else if (command == SubCommand.SET_PRIORITY) {
            if (args.length < 4) {
                System.err.println("The set-priority command requires ip information and standby priority.");
                System.exit(1);
            }
            String ip = args[2];
            String priority = args[3];
            EfmController controller = new EfmController();
            if (!controller.runSetPriority(ip, priority))
                System.exit(1);
        } else if (command == SubCommand.ADD_NODE) {
            String priority;
            if (args.length < 3) {
                System.err.println("The add-node command requires ip information and an optional standby priority.");
                System.exit(1);
            }
            String ip = args[2];
            if (args.length > 3) {
                priority = args[3];
            } else {
                priority = null;
            }
            EfmController controller = new EfmController();
            if (!controller.runAddNode(ip, priority))
                System.exit(1);
        } else if (command == SubCommand.REMOVE_NODE) {
            if (args.length < 3) {
                System.err.println("The remove-node command requires ip information.");
                System.exit(1);
            }
            EfmController controller = new EfmController();
            if (!controller.runRemoveNode(args[2]))
                System.exit(1);
        } else if (command == SubCommand.RESUME || command == SubCommand.FORCE_PROMOTE || command == SubCommand.STOP_CLUSTER || command == SubCommand.CLUSTER_STATUS || command == SubCommand.CLUSTER_STATUS_JSON || (command == SubCommand.PROMOTE && args.length < 3)) {
            EfmController controller = new EfmController();
            if (!controller.run(command))
                System.exit(1);
        } else if (command == SubCommand.PROMOTE) {
            if ("-switchover".equalsIgnoreCase(args[2])) {
                EfmController controller = new EfmController();
                if (!controller.switchover())
                    System.exit(1);
            } else {
                System.err.println("Unknown option: " + args[2]);
                System.exit(1);
            }
        } else {
            System.err.println("Unknown command: " + command);
            System.exit(1);
        }
    }

    private static boolean encryptPasswordFromEnv(String clusterName) {
        String pass = System.getenv("EFMPASS");
        if (pass == null || pass.trim().isEmpty()) {
            System.err.println("EFMPASS password not found.");
            return false;
        }
        String encrypted = getEncryptedPassword(pass, clusterName);
        if (encrypted == null)
            return false;
        System.out.println(encrypted);
        return true;
    }

    private static boolean encryptPassword(String clusterName) {
        System.out.println("This utility will generate an encrypted password for you to place in your\nEFM cluster property file.\nPlease enter the password and hit enter: ");
        char[] passwd = System.console().readPassword();
        System.out.print("Please enter the password again to confirm: ");
        char[] passwdConfirm = System.console().readPassword();
        if (!Arrays.equals(passwd, passwdConfirm)) {
            System.out.println("Error, the passwords do not match.");
            return false;
        }
        String encrypted = getEncryptedPassword(new String(passwd), clusterName);
        if (encrypted == null)
            return false;
        System.out.println("\nThe encrypted password is: " + encrypted + "\n");
        System.out.println("Please paste this into your cluster properties file.");
        System.out.println("\tdb.password.encrypted=" + encrypted);
        return true;
    }

    private static String getEncryptedPassword(String pass, String clusterName) {
        try {
            return PasswordManager.encrypt(pass, clusterName);
        } catch (InvalidKeyException ike) {
            String msg = ike.getMessage();
            if (msg == null || msg.isEmpty())
                msg = ike.toString();
            System.err.println("Could not encode password: " + msg);
            System.err.println("If full-strength encryption has not been installed in the current version of Java, search for \"<vendor name> java full strength encryption\" for more information.");
            return null;
        } catch (Exception e) {
            System.err.println("Could not encode password due to unexpected error: " + e.toString());
            return null;
        }
    }

    static void printMessage(String arg) {
        if ("--version".equals(arg) || "-v".equals(arg)) {
            System.out.println(Version.getVersion());
        } else if ("--help".equals(arg) || "-h".equals(arg)) {
            printHelp();
        } else {
            System.err.println("Unknown option: " + arg);
        }
    }

    static void printHelp() {
        System.out.println("usage: 'efm <command> <cluster_name>' with additional information described below:\n\tallow-node\n\t\tAllow given node to join cluster.\n\t\tFull command: efm allow-node <cluster_name> <ip>\n\tcluster-status\n\t\tOutput the status of the entire efm cluster.\n\tcluster-status-json\n\t\tOutput cluster status in machine-readable format.\n\tdisallow-node\n\t\tRemove given node from allowed-to-join list.\n\t\tFull command: efm disallow-node <cluster_name> <ip>\n\tencrypt\n\t\tEncrypt the database password. Use the --from-env option to not be prompted\n\t\tfor the password. EFM will read the value from the EFMPASS enviroment variable.\n\tpromote\n\t\tPerform manual failover of master to standby (overrides 'auto.failover' property).\n\t\tUse the -switchover switch to promote and reconfigure the master as a new standby.\n\tprop-check\n\t\tRun utility to help resolve differences in properties files among nodes.\n\tresume\n\t\tResume monitoring a database that had previously stopped.\n\tset-priority\n\t\tSet failover priority for a standby. Set to 0 to remove from list.\n\t\tFull command: efm set-priority <cluster_name> <ip> <priority>\n\tstop-cluster\n\t\tStop efm on all nodes.\n\t" + SubCommand.UPGRADE.getValue() + "\n" + "\t\tFull command: efm " + SubCommand.UPGRADE.getValue() + " <2.0 cluster name>\n" + "\t\tWill create a 2.1 compatible .properties and .nodes file based on existing files.\n" + "\t\tMust be run with root privileges." + "\nFor other EFM commands run 'service efm-" + '\002' + "." + '\001' + "'.");
    }

    private static String getPropsLocation(String clusterName) {
        return "/etc/efm-2.1/" + clusterName + ".properties";
    }
}
