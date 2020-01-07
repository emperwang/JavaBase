package com.wk.FTPUtil;

import com.jcraft.jsch.*;
import com.wk.FTPUtil.entity.FtpLogin;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 14:12 2020/1/3
 * @modifier:
 */

@Slf4j
public class sftpTestStarter {
    private static final String PROTOCOL_TYPE = "sftp";

    private static String privateKey;

    public static ChannelSftp login(String address, int port, String userName, String password) throws JSchException {
        JSch jsch = new JSch();
        if (privateKey != null) {
            // 添加私钥
            jsch.addIdentity(privateKey);
        }
        Session session = jsch.getSession(userName, address, port);
        if (password != null) {
            session.setPassword(password);
        }

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        // 连接
        Channel channel = session.openChannel(PROTOCOL_TYPE);
        channel.connect();
        log.info("sftp connect {} success",address);
        return (ChannelSftp) channel;
    }

    public static Boolean downLoad(String directory, String downloadFile, String saveFile, FtpLogin ftpLogin) throws JSchException {
        ChannelSftp sftp = login(ftpLogin.getHost(), ftpLogin.getPort(), ftpLogin.getUser(), ftpLogin.getPassword());
        boolean flag = false;
        if (sftp != null && sftp.isConnected()) {
            if (directory != null && !"".equals(directory)) {
                // 检测目录是否存在
                /*try {
                    sftp.cd(directory);
                } catch (SftpException e) {
                    // 目录不存在
                    log.error(directory + " 此目录不存在,错误信息:" + e.getMessage());
                    return flag;
                }*/
                // 检测目录中是否有文件
                /*try {
                    Vector files = sftp.ls(directory);
                    if (files == null || files.size() == 0) {
                        log.error(directory + " 此目录中没有文件存在");
                        return flag;
                    }
                } catch (SftpException e) {
                    log.error(directory + " 此目录不存在,错误信息:" + e.getMessage());
                    return flag;
                }*/
            }
            FileOutputStream outputStream = null;
            File tempFile = null;
            File destFile = null;
            try {
                String substring = saveFile.substring(0, saveFile.lastIndexOf("/"));
                File parentDir = new File(substring);
                String tempFileName = saveFile + ".tmp";
                tempFile = new File(tempFileName);
                destFile = new File(saveFile);
                // 目录不存在则创建
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                // 文件存在，则不进行下载
                if (destFile.exists()) {
                    return flag;
                }
                // 下载
                outputStream = new FileOutputStream(tempFile);
                sftp.get(directory+"/"+downloadFile, outputStream);
                outputStream.flush();
                outputStream.close();
                renameFile(tempFile,destFile);
                /*if (tempFile != null && destFile != null) {
                    tempFile.renameTo(destFile);
                    //解压缩
                    *//*try {
                        GZipUtil.deCompress(saveFile ,true);
                    } catch (IOException e) {
                        log.info("deCompress file error ,error msg is:"+e.getMessage());
                    }*//*
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                closeConnect(sftp);
                flag = true;
            }
        }
        return flag;
    }

    public static void renameFile(File destFile, File finalFile) {
        if (destFile.exists() && finalFile != null) {
            if (destFile.renameTo(finalFile)) {
                destFile.delete();
                return;
            } else {
                log.info("rename file error, filename:{}",destFile.getName());
                return;
            }
        } else {
            log.info("the file does not exists");
        }
    }

    public static void closeConnect(ChannelSftp sftp) throws JSchException {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (sftp.getSession() != null) {
            if (sftp.getSession().isConnected()) {
                sftp.getSession().disconnect();
            }
        }
    }

    public static void main(String[] args) throws JSchException {
        FtpLogin ftpLogin = new FtpLogin("192.168.72.18", 22, "root", "loongson");
        String localPath = "H:/FTPTest/user.txt";
        String remotePath = "/mnt";
        String fileName = "user.txt";
        downLoad(remotePath, fileName, localPath, ftpLogin);
    }
}
