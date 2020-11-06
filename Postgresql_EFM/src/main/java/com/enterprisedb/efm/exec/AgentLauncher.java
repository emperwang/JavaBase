package com.enterprisedb.efm.exec;

import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.main.ServiceCommand;
import com.enterprisedb.efm.main.SubCommand;
import com.enterprisedb.efm.utils.LogManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgentLauncher {
    private static final String AGENT_RUNNING = "the_agent_is_running";

    private static final Logger LOGGER = LogManager.getEfmLogger();

    private final String propsFile;

    public AgentLauncher(String propsFile) {
        this.propsFile = propsFile;
    }

    public boolean launch() {
        Process process;
        LaunchWatcher watcher;
        List<String> command = new ArrayList<String>();
        command.add(System.getProperty("java.home") + "/bin/java");
        command.add("-cp");
        command.add(System.getProperty("java.class.path"));
        Collections.addAll(command, Environment.getEnvironment().getJvmOptions());
        command.add(ServiceCommand.class.getName());
        command.add(SubCommand.INT_START.getValue());
        command.add(this.propsFile);
        ProcessBuilder pBuilder = new ProcessBuilder(command);
        pBuilder.redirectErrorStream(true);
        try {
            process = pBuilder.start();
        } catch (IOException e) {
            LogManager.tsErr(e.toString());
            return false;
        }
        InputStream allProcOut = process.getInputStream();
        CountDownLatch exitLatch = new CountDownLatch(1);
        try {
            watcher = new LaunchWatcher(allProcOut, exitLatch);
        } catch (UnsupportedEncodingException e) {
            LogManager.tsErr(e.toString());
            return false;
        }
        Thread procDrainerThread = new Thread(watcher, "launch_stream_watcher");
        procDrainerThread.setDaemon(true);
        procDrainerThread.start();
        try {
            exitLatch.await();
        } catch (InterruptedException e) {
            LogManager.tsErr("Failover Manager launcher stopped prematurely.");
            return false;
        }
        return watcher.isSuccess();
    }

    public static boolean signalStartToLauncher() {
        PrintStream newErr, newOut;
        try {
            newErr = new PrintStream(new LoggerStream(Level.WARNING), true, "UTF-8");
            newOut = new PrintStream(new LoggerStream(Level.INFO), true, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            LOGGER.log(Level.SEVERE, "Could not create print streams: {0}", uee.toString());
            LogManager.tsErr("Could not start agent. See logs for more information.");
            return false;
        }
        LOGGER.info("Agent signalling: the_agent_is_running");
        System.out.println("the_agent_is_running");
        PrintStream oldErr = System.err;
        PrintStream oldOut = System.out;
        System.setErr(newErr);
        System.setOut(newOut);
        oldErr.close();
        oldOut.close();
        return true;
    }

    private static class LaunchWatcher implements Runnable {
        private final BufferedReader reader;

        private final CountDownLatch latch;

        private volatile boolean success = false;

        LaunchWatcher(InputStream stream, CountDownLatch latch) throws UnsupportedEncodingException {
            this.reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            this.latch = latch;
        }

        public void run() {
            try {
                String line = this.reader.readLine();
                while (line != null) {
                    if (line.trim().equals("the_agent_is_running")) {
                        this.success = true;
                        this.latch.countDown();
                    } else {
                        LogManager.tsOut(line);
                    }
                    line = this.reader.readLine();
                    try {
                        Thread.sleep(2L);
                    } catch (InterruptedException e) {
                        LogManager.tsErr("temp output: interrupted.");
                    }
                }
            } catch (IOException e) {
                LogManager.tsErr("Unexpected error launching process: " + e.toString());
            } finally {
                this.latch.countDown();
            }
        }

        public boolean isSuccess() {
            return this.success;
        }
    }
}
