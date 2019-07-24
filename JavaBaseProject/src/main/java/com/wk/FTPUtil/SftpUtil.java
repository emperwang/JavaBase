package com.wk.FTPUtil;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

@Slf4j
public class SftpUtil {

    private static final String PROTOCOL_TYPE = "sftp";

    private static ChannelSftp sftp;

    private static Session session;
    /**
     * sftp 登录
     */
    private static String userName = "root";
    /**
     * sftp 密码
     */
    private static String password = "admin";
    /**
     * 主机地址
     */
    private static String host = "127.0.0.1";
    /**
     * 主机端口
     */
    private static Integer port = 22;
    /**
     * 私钥
     */
    private static String privateKey;

    /**
     * 连接sftp 服务器
     */
    public static void login() throws JSchException {
        JSch jsch = new JSch();
        if (privateKey != null) {
            // 添加私钥
            jsch.addIdentity(privateKey);
        }
        // 创建一个session
        Session session = jsch.getSession(userName, host, port);
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
        sftp = (ChannelSftp) channel;
    }

    /**
     * 关闭连接
     */
    public static void logout() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     * 文件完整路径=basePath+directory
     *
     * @param basePath 基本目录
     * @param directory 文件所在目录
     * @param sftpFileName 上传后的文件名字
     * @param filePath  要上传的文件
     */
    public static void upload(String basePath, String directory, String sftpFileName, String filePath) throws JSchException, IOException, SftpException {
        try {
            login();
            sftp.cd(basePath);
            sftp.cd(directory);
        } catch (SftpException e) {
            // 目录不存在，则创建文件夹
            String[] dirs = directory.split("/");
            String temePath = basePath;
            for (String dir : dirs) {
                if (dir == null || "".equals(dir)) {
                    continue;
                }
                temePath += "/" + dir;
                try {
                    sftp.cd(temePath);
                } catch (SftpException e1) {
                    // 创建目录
                    sftp.mkdir(temePath);
                    sftp.cd(temePath);
                }
            }
        }
        // 上传文件
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(filePath));
            String tempFileName = sftpFileName+".tmp";
            sftp.put(inputStream, tempFileName);
            sftp.rename(tempFileName,sftpFileName);
        } catch (FileNotFoundException e) {
            log.error(filePath + " 此文件不存在");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            logout();
        }
    }



    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载文件
     * @param saveFile     本地存放路径
     */
    public static void downLoad(String directory, String downloadFile, String saveFile) throws JSchException {
        login();
        if (sftp != null) {
            if (directory != null && !"".equals(directory)) {
                // 检测目录是否存在
                try {
                    sftp.lcd(directory);
                } catch (SftpException e) {
                    // 目录不存在
                    log.error(directory + " 此目录不存在,错误信息:" + e.getMessage());
                    return;
                }
                // 检测目录中是否有文件
                try {
                    Vector files = sftp.ls(directory);
                    if (files == null || files.size() == 0) {
                        log.error(directory + " 此目录中没有文件存在");
                        return;
                    }
                } catch (SftpException e) {
                    log.error(directory + " 此目录不存在,错误信息:" + e.getMessage());
                    return;
                }
            }
            try {
                String substring = saveFile.substring(0, saveFile.lastIndexOf("/"));
                File parentDir = new File(substring);
                File file = new File(saveFile);
                // 目录不存在则创建
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                // 文件存在，则不进行下载
                if (file.exists()) {
                    return;
                }
                // 下载
                sftp.get(downloadFile, new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (SftpException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名
     * @return
     */
    public byte[] downLoad(String directory, String downloadFile) throws IOException, JSchException, SftpException {
        login();
        byte[] fileDate = null;
        if (sftp != null) {
            if (directory != null && !"".equals(directory)) {
                // 检测目录是否存在
                try {
                    sftp.cd(directory);
                } catch (SftpException e) {
                    log.error(directory + " 此目录不存在,错误信息:"+e.getMessage());
                    return fileDate;
                }

                // 检测目录中是否存在文件
                try{
                    Vector files = sftp.ls(directory);
                    if (files == null || files.size() == 0){
                        log.info(directory+" 此目录中没有文件可下载");
                        return fileDate;
                    }
                } catch (SftpException e) {
                    log.error(directory + " 此目录不存在,错误信息:"+e.getMessage());
                    return fileDate;
                }

            }
            InputStream inputStream = sftp.get(downloadFile);
            fileDate = IOUtils.toByteArray(inputStream);
        }
        return fileDate;
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在的目录
     * @param deleteFile 要删除的文件
     */
    public static void delete(String directory, String deleteFile) throws SftpException, JSchException {
        login();
        if (sftp != null) {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory
     * @return
     */
    public static Vector listFiles(String directory) throws SftpException, JSchException {
        login();
        Vector files = null;
        if (sftp != null) {
            files = sftp.ls(directory);
        }
        return files;
    }
}
