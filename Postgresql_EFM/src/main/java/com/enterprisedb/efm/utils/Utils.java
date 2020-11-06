package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.exec.ExecUtil;
import com.enterprisedb.efm.exec.ProcessResult;
import com.enterprisedb.efm.exec.SudoFunctions;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Utils {
    private static final Logger LOGGER = LogManager.getEfmLogger();

    private static boolean hasRootPrivs() {
        boolean hasRoot;
        Level l = LOGGER.getLevel();
        LOGGER.setLevel(Level.OFF);
        ProcessResult r = ExecUtil.performExec(new String[] { "id -u" });
        LOGGER.setLevel(l);
        if (r.getExitValue() == 0) {
            String out = r.getStdOut();
            try {
                int uid = Integer.valueOf(out.trim()).intValue();
                hasRoot = (uid == 0);
            } catch (NumberFormatException e) {
                hasRoot = false;
            }
        } else {
            hasRoot = false;
        }
        return hasRoot;
    }

    public static boolean hasPermission(boolean startup) {
        if (hasRootPrivs())
            return true;
        File lockDir = new File("/var/lock/efm-2.1/");
        File logDir = new File("/var/log/efm-2.1/");
        File lockFile = Environment.getEnvironment().getLockFile();
        boolean lockDirOk = (lockDir.exists() && lockDir.canWrite());
        boolean logDirOk = (logDir.exists() && logDir.canWrite());
        boolean lockFileOk = false;
        if (startup || (lockFile.exists() && lockFile.canWrite())) {
            lockFileOk = true;
        } else if (!lockFile.exists() && lockDirOk) {
            lockFileOk = true;
        }
        if (lockDirOk && logDirOk && lockFileOk)
            return true;
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, "Permission check: Lock dir ok? {0} Log dir ok? {1} Lock file ok? {2}", new Object[] { Boolean.valueOf(lockDirOk), Boolean.valueOf(logDirOk), Boolean.valueOf(lockFileOk) });
        LOGGER.severe("Permission denied.");
        return false;
    }

    public static boolean hasControllerPermission() {
        File authFile = new File(Environment.getEnvironment().getAuthFileLocation());
        if (!authFile.exists()) {
            System.err.println("Authorization file not found. Is the local agent running?");
            return false;
        }
        if (!authFile.canRead()) {
            System.err.println("Not authorized to run this command.");
            return false;
        }
        return true;
    }

    public static boolean chmod(File file, String mode) {
        ProcessResult r = ExecUtil.performExec(new String[] { "chmod " + mode + " " + file.getPath() });
        if (r.getExitValue() == 0)
            return true;
        LOGGER.log(Level.WARNING, "Error changing file mode of {0} to {1}\n\tcause: {2}", new Object[] { file.getPath(), mode, r.getErrorOut() });
        return false;
    }

    public static boolean writePid() {
        try {
            ProcessResult r1 = ExecUtil.performExec(new String[] { "sh", "-c", "ps h -o ppid $$" });
            StringBuilder s = new StringBuilder();
            r1.addNiceOutput(s);
            if (r1.getExitValue() == 0) {
                int pid = (new Scanner(r1.getStdOut())).nextInt();
                Environment env = Environment.getEnvironment();
                ProcessResult r2 = ExecUtil.performExec(new String[] { env.getRootSudoCommand() + " " + env.getRootFunctionsScript() + " " + SudoFunctions.WRITE_PID_KEY + " " + env.getClusterName() + " " + pid });
                if (r2.getExitValue() == 0)
                    return true;
                StringBuilder stringBuilder = new StringBuilder();
                r2.addNiceOutput(stringBuilder);
                System.err.println("Error writing files in /var/run: " + stringBuilder);
                return false;
            }
            StringBuilder msg = new StringBuilder();
            r1.addNiceOutput(msg);
            System.err.println("Error obtaining pid: " + msg);
            return false;
        } catch (Exception e) {
            System.err.println("Error writing pid file: " + e.getMessage());
            LOGGER.log(Level.WARNING, "Error writing pid file: ", e);
            return false;
        }
    }

    public static boolean deletePidAndKey() {
        Environment env = Environment.getEnvironment();
        ProcessResult result = ExecUtil.performExec(new String[] { env.getRootSudoCommand() + " " + env.getRootFunctionsScript() + " " + SudoFunctions.DELETE_PID_KEY + " " + env.getClusterName() });
        if (result.getExitValue() == 0)
            return true;
        StringBuilder msg = new StringBuilder();
        result.addNiceOutput(msg);
        LOGGER.log(Level.WARNING, "Error deleting /var/run/{0}.*: {1}", new Object[] { "efm-2.1." + env.getClusterName(), msg });
        return false;
    }

    public static String getMsg(Exception e) {
        String message = e.getMessage();
        if (message == null || message.isEmpty())
            return e.toString();
        return message;
    }

    public static boolean isIPv6(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            LogManager.getEfmLogger().log(Level.FINE, "Checking ip {0} for type", ip);
            if (addr instanceof java.net.Inet4Address)
                return false;
            if (addr instanceof java.net.Inet6Address)
                return true;
            throw new IllegalArgumentException(ip + " is neither IPv4 or IPv6.");
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Cannot check type of ip address: " + ip);
        }
    }
}
