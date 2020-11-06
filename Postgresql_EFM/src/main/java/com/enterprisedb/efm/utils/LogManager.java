package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.Environment;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public enum LogManager {
    INSTANCE;

    private static final MessageFormat DATE_FORMAT;

    private boolean initialized;

    private final Logger jgroupsLogger;

    private final Logger efmLogger;

    LogManager() {
        this.efmLogger = Logger.getLogger("com.enterprisedb.efm");
        this.jgroupsLogger = Logger.getLogger("org.jgroups");
        this.initialized = false;
    }

    static {
        DATE_FORMAT = new MessageFormat("[ {0,date,short} {0,time,medium} ] ");
    }

    public static synchronized void init() {
        if (INSTANCE.initialized)
            throw new IllegalArgumentException("Log manager already initialized.");
        INSTANCE.jgroupsLogger.setLevel(Environment.getEnvironment().getJgroupsLogLevel());
        INSTANCE.efmLogger.setLevel(Environment.getEnvironment().getLogLevel());
        INSTANCE.jgroupsLogger.setUseParentHandlers(false);
        INSTANCE.efmLogger.setUseParentHandlers(false);
        String clusterName = Environment.getEnvironment().getClusterName();
        Handler fileHandler = INSTANCE.createFileHandler(clusterName);
        INSTANCE.efmLogger.addHandler(fileHandler);
        INSTANCE.jgroupsLogger.addHandler(fileHandler);
        INSTANCE.initialized = true;
    }

    public static Logger getEfmLogger() {
        return INSTANCE.efmLogger;
    }

    public static void tsErr(String msg) {
        System.err.println(addStamp(msg));
    }

    public static void tsOut(String msg) {
        System.out.println(addStamp(msg));
    }

    private static String addStamp(String s) {
        StringBuffer sb = new StringBuffer();
        synchronized (DATE_FORMAT) {
            DATE_FORMAT.format(new Object[] { new Date() }, sb, (FieldPosition)null);
        }
        sb.append(s);
        return sb.toString();
    }

    private static class EfmFormatter extends Formatter {
        private final Date date = new Date();

        private final MessageFormat formatter = new MessageFormat("{0,date,short} {0,time,medium}");

        private final Object[] args = new Object[1];

        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder();
            this.date.setTime(record.getMillis());
            this.args[0] = this.date;
            StringBuffer text = new StringBuffer();
            this.formatter.format(this.args, text, (FieldPosition)null);
            sb.append(text);
            sb.append(" ");
            if (record.getSourceClassName() != null) {
                sb.append(record.getSourceClassName());
            } else {
                sb.append(record.getLoggerName());
            }
            if (record.getSourceMethodName() != null) {
                sb.append(" ");
                sb.append(record.getSourceMethodName());
            }
            sb.append(" ");
            String message = formatMessage(record);
            sb.append(record.getLevel().getLocalizedName());
            sb.append(": ");
            sb.append(message);
            sb.append("\n");
            if (record.getThrown() != null)
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    sb.append(sw.toString());
                } catch (Exception ignored) {
                    System.err.println("Error logging stacktrace: " + record.getThrown().toString());
                }
            sb.append("\n");
            return sb.toString();
        }

        private EfmFormatter() {}
    }

    private Handler createFileHandler(String clusterName) {
        Handler handler;
        String logFile = "/var/log/efm-2.1/" + clusterName + ".log";
        try {
            handler = new FileHandler(logFile, true);
            handler.setFormatter(new EfmFormatter());
            handler.setLevel(Level.FINEST);
        } catch (IOException ioe) {
            System.err.println(String.format("Could not create log '%s' due to error '%s'", new Object[] { logFile, ioe.getLocalizedMessage() }));
            System.err.println("Will send logs to standard.err instead.");
            handler = new StreamHandler(System.err, new EfmFormatter());
        }
        return handler;
    }
}
