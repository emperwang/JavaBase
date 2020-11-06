package com.enterprisedb.efm.exec;

import com.enterprisedb.efm.utils.LogManager;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ExecUtil {
    private static final String ERROR = "ERR";

    private static final String OUT = "OUT";

    private static final Logger LOGGER = LogManager.getEfmLogger();

    public static ProcessResult performExec(String... command) {
        return performExec(true, command);
    }

    public static ProcessResult performExec(boolean logCommand, String... command) {
        ProcessResult processResult = new ProcessResult();
        Process process = null;
        StreamWatcher errWatcher = null;
        StreamWatcher outWatcher = null;
        try {
            if (logCommand) {
                LOGGER.info(Arrays.toString((Object[])command));
            } else if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(Arrays.toString((Object[])command));
            }
            if (command.length == 1) {
                process = Runtime.getRuntime().exec(command[0]);
            } else {
                process = Runtime.getRuntime().exec(command);
            }
            errWatcher = new StreamWatcher(process.getErrorStream(), "ERR", processResult);
            outWatcher = new StreamWatcher(process.getInputStream(), "OUT", processResult);
            errWatcher.start();
            outWatcher.start();
            try {
                processResult.setExitValue(process.waitFor());
            } catch (InterruptedException ie) {
                Thread.interrupted();
                LOGGER.log(Level.WARNING, "Thread {0} was interrupted waiting on process >> {1} << Forcing process to terminate.", new Object[] { Thread.currentThread().getName(), Arrays.toString((Object[])command) });
                process.destroy();
                LOGGER.warning("Process terminated.");
            }
            long joinTimeout = 10000L;
            errWatcher.join(10000L);
            outWatcher.join(10000L);
        } catch (Throwable t) {
            if (processResult.getExitValue() == 0)
                processResult.setExitValue(-1);
            String procErr = processResult.getErrorOut();
            if (procErr == null || procErr.isEmpty())
                processResult.setErrorOut(t.toString());
            processResult.setThrown(t);
        } finally {
            closeStreamsQuietly(process);
            verifyClosed(errWatcher, command);
            verifyClosed(outWatcher, command);
        }
        if (logCommand)
            LOGGER.info(processResult.toString());
        return processResult;
    }

    private static void verifyClosed(Thread thread, String... command) {
        if (thread == null)
            return;
        if (!thread.isAlive()) {
            if (LOGGER.isLoggable(Level.FINEST))
                LOGGER.log(Level.FINEST, "Verified thread {0} is closed for command: {1}", new Object[] { thread.getName(), Arrays.toString((Object[])command) });
            return;
        }
        try {
            thread.interrupt();
            thread.join(5000L);
        } catch (InterruptedException e) {
            LOGGER.warning(String.format("Interrupted waiting for watcher thread %s to exit for command: %s", new Object[] { thread.getName(), Arrays.toString((Object[])command) }));
        }
        if (thread.isAlive())
            LOGGER.severe(String.format("Thread %s still alive for the following command. The process may not have exited and should be checked. Command: %s", new Object[] { thread.getName(), Arrays.toString((Object[])command) }));
    }

    private static class StreamWatcher extends Thread {
        private final ProcessResult eResult;

        private final BufferedReader reader;

        StreamWatcher(InputStream stream, String name, ProcessResult eResult) throws UnsupportedEncodingException {
            super(name);
            setDaemon(true);
            this.reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            this.eResult = eResult;
        }

        public void run() {
            StringBuilder sb = new StringBuilder();
            try {
                String line = this.reader.readLine();
                while (line != null) {
                    sb.append(line).append("\n");
                    line = this.reader.readLine();
                    Thread.sleep(2L);
                }
            } catch (Exception e) {
                ExecUtil.LOGGER.log(Level.WARNING, "Unexpected problem in stream watcher: {0}", e.toString());
            } finally {
                if ("ERR".equals(getName())) {
                    this.eResult.setErrorOut(sb.toString());
                } else {
                    this.eResult.setStdOut(sb.toString());
                }
            }
        }
    }

    public static void closeStreamQuietly(Closeable s) {
        if (s == null)
            return;
        try {
            s.close();
        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING, "Exception closing stream. {0}", ioe.toString());
        }
    }

    public static void closeStreamsQuietly(Process process) {
        if (process == null)
            return;
        closeStreamQuietly(process.getErrorStream());
        closeStreamQuietly(process.getInputStream());
        closeStreamQuietly(process.getOutputStream());
    }
}
