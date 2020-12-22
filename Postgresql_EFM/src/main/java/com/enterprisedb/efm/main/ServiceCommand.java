package com.enterprisedb.efm.main;

import com.enterprisedb.efm.EfmController;
import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.Version;
import com.enterprisedb.efm.exec.AgentLauncher;
import com.enterprisedb.efm.nodes.EfmAgent;
import com.enterprisedb.efm.nodes.EfmWitness;
import com.enterprisedb.efm.utils.LockFile;
import com.enterprisedb.efm.utils.ShutdownNotificationService;
import com.enterprisedb.efm.utils.Utils;

public final class ServiceCommand extends AbstractCommand {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("No command line parameters specified.");
            return;
        }
        if (args[0].startsWith("-")) {
            printMessage(args[0]);
            return;
        }
        SubCommand command = SubCommand.getCommand(args, "Error: unknown command '" + args[0] + "'. Run 'service efm-" + '\002' + "." + '\001' + " help' for more information.");
        if (command == null)
            System.exit(-1);
        if (args.length != 2) {
            System.err.println("[ERROR] Missing command or properties file.");
            printHelp();
            System.exit(-1);
        }
        String propsFile = args[1];
        if (command == SubCommand.INT_START)
            ShutdownNotificationService.getInstance().registerShutdownNotificationHook();
        if (command == SubCommand.START) {
            Environment.getEnvironment().init(propsFile, command);
            AgentLauncher launcher = new AgentLauncher(propsFile);
            if (!launcher.launch())
                System.exit(1);
            return;
        }
        if (!Environment.getEnvironment().init(propsFile, command))
            System.exit(1);
        if (command == SubCommand.INT_START) {
            if (!Utils.hasPermission(true))
                return;
            LockFile lockFile = LockFile.getInstance();
            if (!lockFile.lock()) {
                System.err.println("[ERROR] Unable to obtain lock file: " + lockFile);
                System.err.println("Please make sure that you don't already have an agent running on this node for cluster '" + Environment.getEnvironment().getClusterName() + "'.  If not, please manually delete " + "the lock file and restart the agent.");
                return;
            }
            // 启动
            if (Environment.getEnvironment().isWitness()) {
                (new EfmWitness()).run();
            } else {
                (new EfmAgent()).run();
            }
        } else if (command == SubCommand.STOP) {
            if (!Utils.hasPermission(false))
                System.exit(-1);
            EfmController controller = new EfmController();
            if (controller.run(command))
                System.exit(0);
            System.exit(1);
        } else {
            System.err.println("Unknown command '" + command);
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
        System.out.println("usage: service efm-2.1 <command>\ncommands:\n\tstart\n\t\tStart efm on a single node.\n\tstop\n\t\tStop efm on a single node.\n\nFor other EFM commands, run '/usr/efm-2.1/bin/efm --help'.");
    }
}
