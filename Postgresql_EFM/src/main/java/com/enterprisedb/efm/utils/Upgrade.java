package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.EfmProps;
import com.enterprisedb.efm.main.SubCommand;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Upgrade {
    private static final String PREVIOUS_VERSION = "2.0";

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss.SSS");

    private final Properties orig = new Properties();

    private final String nodeTimeout = EfmProps.JGROUPS_TOTAL_TIMEOUT.getPropName();

    public boolean run(String clusterName) {
        BufferedReader propsTemplateReader, origNodesReader, newNodesReader;
        String currentPropsDir = "/etc/efm-2.1";
        File propsTemplate = new File("/etc/efm-2.1", "efm.properties.in");
        if (!propsTemplate.exists()) {
            System.err.println("Cannot find: " + propsTemplate.getAbsolutePath());
            return false;
        }
        if (!propsTemplate.canWrite()) {
            System.err.println("User does not have permission to write properties file. The " + SubCommand.UPGRADE.getValue() + " utility must be run with root privileges.");
            return false;
        }
        String origPropsDir = "/etc/efm-2.1".replace("2.1", "2.0");
        File originalProps = new File(origPropsDir, clusterName + ".properties");
        FileInputStream in = null;
        try {
            in = new FileInputStream(originalProps);
            this.orig.load(in);
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find file " + originalProps.getAbsolutePath());
            return false;
        } catch (IOException e) {
            System.err.println("Cannot read file " + originalProps.getAbsolutePath() + " due to " + e);
            return false;
        } finally {
            closeQuietly(in);
        }
        try {
            InputStreamReader inStreamReader = new InputStreamReader(new FileInputStream(propsTemplate), "UTF-8");
            propsTemplateReader = new BufferedReader(inStreamReader);
        } catch (FileNotFoundException e) {
            return false;
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.toString());
            return false;
        }
        File propsOut = new File("/etc/efm-2.1", clusterName + ".properties");
        if (propsOut.exists()) {
            File moveTo = new File(propsOut.getParent(), propsOut.getName() + "_" + this.dateFormat.format(new Date()));
            System.out.println("Moving existing properties file to " + moveTo.getName());
            if (!propsOut.renameTo(moveTo)) {
                System.err.println("Could not move existing properties file. Please remove and try again.");
                return false;
            }
        }
        System.out.println("Processing " + clusterName + ".properties file.");
        PrintWriter propsWriter = null;
        try {
            if (!propsOut.createNewFile()) {
                System.err.println("There was a problem creating the new properties file.");
                return false;
            }
            propsWriter = new PrintWriter(propsOut, "UTF-8");
            String line = propsTemplateReader.readLine();
            while (line != null) {
                propsWriter.println(processLine(line.trim()));
                line = propsTemplateReader.readLine();
            }
        } catch (IOException e) {
            System.err.println("An error occurred during processing: " + e);
        } finally {
            closeQuietly(propsTemplateReader);
            closeQuietly(propsWriter);
        }
        System.out.println("");
        File origNodes = new File(origPropsDir, clusterName + ".nodes");
        File newNodes = new File("/etc/efm-2.1", "efm.nodes.in");
        try {
            InputStreamReader nodeStream = new InputStreamReader(new FileInputStream(origNodes), "UTF-8");
            origNodesReader = new BufferedReader(nodeStream);
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find " + origNodes.getAbsolutePath() + " to copy.");
            return false;
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.toString());
            return false;
        }
        try {
            InputStreamReader nodeStream = new InputStreamReader(new FileInputStream(newNodes), "UTF-8");
            newNodesReader = new BufferedReader(nodeStream);
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find: " + newNodes.getAbsolutePath());
            return false;
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.toString());
            return false;
        }
        File nodesOut = new File("/etc/efm-2.1", clusterName + ".nodes");
        if (nodesOut.exists()) {
            File moveTo = new File(nodesOut.getParent(), nodesOut.getName() + "_" + this.dateFormat.format(new Date()));
            System.out.println("Moving existing nodes file to " + moveTo.getName());
            if (!nodesOut.renameTo(moveTo)) {
                System.err.println("Could not move existing nodes file. Please remove and try again.");
                return false;
            }
        }
        PrintWriter nodesWriter = null;
        try {
            if (!nodesOut.createNewFile()) {
                System.err.println("There was a problem creating the new nodes file.");
                return false;
            }
            nodesWriter = new PrintWriter(nodesOut, "UTF-8");
            System.out.println("Processing " + clusterName + ".nodes file.");
            String line = newNodesReader.readLine();
            while (line != null) {
                nodesWriter.println(line);
                line = newNodesReader.readLine();
            }
            line = origNodesReader.readLine();
            while (line != null) {
                if (!line.isEmpty() && !line.trim().startsWith("#"))
                    nodesWriter.println(line);
                line = origNodesReader.readLine();
            }
        } catch (IOException e) {
            System.err.println("An error occurred during processing: " + e);
            return false;
        } finally {
            closeQuietly(origNodesReader);
            closeQuietly(newNodesReader);
            closeQuietly(nodesWriter);
        }
        set644Perms(propsOut);
        set600Perms(nodesOut);
        System.out.println("\nUpgrade of files is finished. Please ensure that the new file permissions match those of the template files before starting EFM.");
        System.out.println("The " + EfmProps.DB_SERVICE_NAME.getPropName() + " property should be set before starting a non-witness agent.");
        return true;
    }

    private void set644Perms(File file) {
        boolean ok = file.setReadable(false, false);
        ok &= file.setWritable(false, false);
        ok &= file.setExecutable(false, false);
        ok &= file.setReadable(true, false);
        ok &= file.setWritable(true, true);
        if (!ok)
            System.err.println("There was a problem setting the proper permissions for " + file.getAbsolutePath());
    }

    private void set600Perms(File file) {
        boolean ok = file.setReadable(false, false);
        ok &= file.setWritable(false, false);
        ok &= file.setExecutable(false, false);
        ok &= file.setReadable(true, true);
        ok &= file.setWritable(true, true);
        if (!ok)
            System.err.println("There was a problem setting the proper permissions for " + file.getAbsolutePath());
    }

    private String processLine(String line) {
        if (line.isEmpty() || line.startsWith("#"))
            return line;
        int loc = line.indexOf("=");
        if (loc > 0) {
            String key = line.substring(0, loc).trim();
            if (key.equals(this.nodeTimeout))
                try {
                    int tries = Integer.parseInt(this.orig.getProperty("jgroups.max.tries"));
                    int timeout = Integer.parseInt(this.orig.getProperty("jgroups.timeout"));
                    int newTimeoutValue = tries * timeout / 1000;
                    if (newTimeoutValue < 1)
                        timeout = 1;
                    System.out.println(String.format("Setting new property %s to %s (sec) based on existing timeout %s (ms) and max tries %s.", new Object[] { this.nodeTimeout, Integer.valueOf(newTimeoutValue), Integer.valueOf(timeout), Integer.valueOf(tries) }));
                    return this.nodeTimeout + "=" + String.valueOf(newTimeoutValue);
                } catch (NumberFormatException nfe) {
                    System.err.println("Could not read timeout values to set " + this.nodeTimeout + ". This must be set manually before starting EFM.");
                    return this.nodeTimeout + "=";
                }
            String value = this.orig.getProperty(key);
            if (value != null)
                return key + "=" + value;
        }
        return line;
    }

    private void closeQuietly(Closeable c) {
        if (c == null)
            return;
        try {
            c.close();
        } catch (IOException e) {
            System.err.println("(Problem closing file stream: " + e);
        }
    }
}
