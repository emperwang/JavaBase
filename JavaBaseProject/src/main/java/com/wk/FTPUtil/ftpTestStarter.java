package com.wk.FTPUtil;

import com.wk.FTPUtil.entity.FtpLogin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 13:56 2020/1/3
 * @modifier:
 */
@Slf4j
public class ftpTestStarter {

    public static FTPClient login(String address,int port,String userName,String password) throws IOException {
        FTPClient ftpClient = new FTPClient();
        // 连接
        ftpClient.connect(address,port);
        // 登录
        ftpClient.login(userName,password);
        // 设置传输的文件类型
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

        ftpClient.setAutodetectUTF8(true);
        // 进入主动模式, 外网一般使用主动
        ftpClient.enterLocalActiveMode();
        // 进入被动模式, 内网一般使用被动
        // ftpClient.enterLocalPassiveMode();

        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)){
            closeConnect(ftpClient);
            log.error("FTP 服务器连接失败");
        }
        return ftpClient;
    }

    public static void closeConnect(FTPClient ftpClient) throws IOException {
        if (ftpClient != null && ftpClient.isConnected()){
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    public static Boolean downLoadfile(String ftpPath,String fileName,String localPath,FtpLogin ftpLogin) throws IOException {
        FTPClient ftpClient = login(ftpLogin.getHost(), ftpLogin.getPort(), ftpLogin.getUser(), ftpLogin.getPassword());
        boolean flag = false;
        if (ftpClient != null){
            String path = ftpPath;
            // 判断目录是否存在
            if (!ftpClient.changeWorkingDirectory(path)){
                log.error(ftpPath + " 该目录不存在");
                return flag;
            }
            // 设置被动模式
            ftpClient.enterLocalPassiveMode();;
            String[] files = ftpClient.listNames();
            // 判断该目录下是否有文件
            if (files == null || files.length == 0){
                log.error(ftpPath + "  该目录中没有文件存在");
                return flag;
            }
            for (String file:files){
                String ftpname = file;
                if (ftpname.equals(fileName)){
                    File destFile = new File(localPath + "/" + ftpname+".tmp");
                    File finalFile = new File(localPath + "/" + ftpname);
                    FileOutputStream outputStream = new FileOutputStream(destFile);
                    // 下载文件
                    flag = ftpClient.retrieveFile(file, outputStream);
                    if (flag){
                        outputStream.flush();
                        outputStream.close();
                        renameFile(destFile,finalFile);
                    }
                    break;
                }
            }
            closeConnect(ftpClient);
        }
        return flag;
    }

    public static void renameFile(File destFile, File finalFile) {
        if (destFile.exists() && finalFile != null) {
            if (destFile.renameTo(finalFile)) {
                //destFile.delete();
                return;
            } else {
                log.info("rename file error, filename:{}",destFile.getName());
                return;
            }
        } else {
            log.info("the file does not exists");
        }
    }

    public static void main(String[] args) throws IOException {
        FtpLogin ftpLogin = new FtpLogin("192.168.72.18", 21, "root", "loongson");
        String localPath = "H:\\FTPTest";
        String remotePath = "/mnt";
        String fileName = "user.txt";

        Boolean aBoolean = downLoadfile(remotePath, fileName, localPath, ftpLogin);

    }
}
