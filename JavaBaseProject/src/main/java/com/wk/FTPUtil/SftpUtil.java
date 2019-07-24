package com.wk.FTPUtil;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

@Slf4j
public class SftpUtil {

    private static final String PROTOCOL_TYPE="sftp";

    private static ChannelSftp sftp;

    private static Session session;
    /**
     *  sftp 登录
     */
    private static String userName = "root";
    /**
     *  sftp 密码
     */
    private static String password = "admin";
    /**
     *  主机地址
     */
    private static String host = "127.0.0.1";
    /**
     *  主机端口
     */
    private static Integer port = 22;
    /**
     *  私钥
     */
    private static String privateKey;

    /**
     *  连接sftp 服务器
     */
    public static void login() throws JSchException {
        JSch jsch = new JSch();
        if (privateKey != null){
            jsch.addIdentity(privateKey);
        }
        Session session = jsch.getSession(userName, host, port);
        if (password != null){
            session.setPassword(password);
        }

        Properties config = new Properties();
        config.put("StrictHostKeyChecking","no");
        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel(PROTOCOL_TYPE);
        channel.connect();
        sftp = (ChannelSftp) channel;
    }

    /**
     *  关闭连接
     */
    public static void logout(){
        if (sftp != null){
            if (sftp.isConnected()){
                sftp.disconnect();
            }
        }
        if (session != null){
            if (session.isConnected()){
                session.disconnect();
            }
        }
    }

    /**
     *  将输入流的数据上传到sftp作为文件
     *  文件完整路径=basePath+directory
     * @param basePath
     * @param directory
     * @param sftpFileName
     * @param filePath
     */
    public static void upload(String basePath, String directory, String sftpFileName, String filePath) throws SftpException, FileNotFoundException {
        try {
            login();
            sftp.cd(basePath);
            sftp.cd(directory);
        } catch (SftpException e) {
            // 目录不存在，则创建文件夹
            String[] dirs = directory.split("/");
            for (String dir : dirs) {
                if (dir == null || "".equals(dir)){
                    continue;
                }
                try {
                    sftp.cd(basePath);
                } catch (SftpException e1) {
                    sftp.mkdir(basePath);
                    sftp.cd(basePath);
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }
        // 上传文件
        FileInputStream inputStream = new FileInputStream(new File(filePath));
        sftp.put(inputStream,sftpFileName);
    }

    /**
     *  下载文件
     * @param directory 下载目录
     * @param downloadFile 下载文件
     * @param saveFile 本地存放路径
     */
    public static void downLoad(String directory,String downloadFile,String saveFile) throws SftpException, FileNotFoundException, JSchException {
        login();
        if (directory != null && !"".equals(directory)){
            sftp.lcd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile,new FileOutputStream(file));
    }

    /**
     *  下载文件
     * @param directory  下载目录
     * @param downloadFile 下载的文件名
     * @return
     */
    public byte[] downLoad(String directory,String downloadFile) throws SftpException, IOException, JSchException {
        login();
        if(directory != null && !"".equals(directory)){
            sftp.cd(directory);
        }
        InputStream inputStream = sftp.get(downloadFile);
        byte[] fileDate = IOUtils.toByteArray(inputStream);
        return fileDate;
    }

    /**
     *  删除文件
     * @param directory 要删除文件所在的目录
     * @param deleteFile 要删除的文件
     */
    public static void delete(String directory,String deleteFile) throws SftpException, JSchException {
        login();
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }

    /**
     *  列出目录下的文件
     * @param directory
     * @return
     */
    public static Vector listFiles(String directory) throws SftpException, JSchException {
        login();
        return sftp.ls(directory);
    }
}
