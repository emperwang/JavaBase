package com.wk.FTPUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Setter
@Getter
public class FTPUtil {
    private static  String HOST = "127.0.0.1";

    private static  Integer PORT = 21;

    private static  String UserName = "root";

    private static  String Password = "admin";
    /**
     *  ftpClient 客户端
     */
    private static FTPClient ftpClient = null;
    /**
     *  FTP基础目录
     */
    //private static final String BasePath = "notepadPlus/";
    private static final String BasePath = "";
    /**
     *  本地编码
     */
    private static String localCharset = "GBK";
    /**
     *  FTP协议里面，规定文件名编码为ISO-8859-1
     */
    private static final String serverCharset = "ISO-8859-1";
    /**
     *  UTF8 编码
     */
    private static final String CharsetUTF8 = "UTF-8";
    /**
     *  OPTS UTF8 字符串常量
     */
    private static final String OPTS_UTF8 = "OPTS UTF8";
    // 缓冲区大小4M
    private static final Integer Buf_Size = 1024 * 1024 * 4;

    /**
     *  登录FTP服务器
     * @param address
     * @param port
     * @param userName
     * @param password
     */
    public static void login(String address,int port,String userName,String password) throws IOException {
        ftpClient = new FTPClient();
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
            closeConnect();
            log.error("FTP 服务器连接失败");
        }
    }

    /**
     *  关闭连接
     * @throws IOException
     */
    public static void closeConnect() throws IOException {
        if (ftpClient != null && ftpClient.isConnected()){
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }

    /**
     *  FTP服务器路径编码转换
     * @param ftpPath
     * @return
     */
    public static String changEncoding(String ftpPath) throws IOException {
        String directory = null;
        if(FTPReply.isPositiveCompletion(ftpClient.sendCommand(OPTS_UTF8,"on"))){
            localCharset = CharsetUTF8;
        }
        directory = new String(ftpPath.getBytes(localCharset),serverCharset);
        return directory;
    }

    /**
     *  获取一个目录下都有哪些文件
     * @param ftpPath
     * @return
     */
    public static List<String> getFileNameByPath(String ftpPath) throws IOException {
        login(HOST,PORT,UserName,Password);
        List<String> names = new ArrayList<>();
        if (ftpClient != null){
            String path = changEncoding(BasePath + ftpPath);
            // 判断目录是否存在
            if (!ftpClient.changeWorkingDirectory(path)){
                log.error(BasePath+ftpPath+" 目录不存在");
                return names;
            }
            ftpClient.enterLocalPassiveMode();
            String[] fileNames = ftpClient.listNames();
            // 判断目录下是否有文件存在
            if (fileNames == null || fileNames.length == 0){
                log.error(BasePath + ftpPath +" 该目录下没有文件存在");
                return names;
            }
            for (String fileName : fileNames) {
                String ftpname = new String(fileName.getBytes(serverCharset), localCharset);
                names.add(ftpname);
            }
            closeConnect();
        }
        return names;
    }
    /**
     *  创建目录
     * @param dirPath
     */
    public static void createDirectories(String dirPath) throws IOException {
        if (!dirPath.endsWith("/")){
            dirPath += "/";
        }
        String directory = dirPath.substring(0, dirPath.lastIndexOf("/") + 1);
        ftpClient.makeDirectory("/");
        int start = 0;
        int end = 0;
        if (directory.startsWith("/")){
            start = 1;
        }else {
            start = 0;
        }
        end = directory.indexOf("/",start);
        while (true){
            String subDirectory = new String(dirPath.substring(start, end));
            if (!ftpClient.changeWorkingDirectory(subDirectory)){
                if (ftpClient.makeDirectory(subDirectory)){
                    ftpClient.changeWorkingDirectory(subDirectory);
                }else {
                    log.info("创建目录失败");
                    return;
                }
            }
            start = end + 1;
            end = directory.indexOf("/",start);
            // 检查所有目录是否创建完毕
            if (end <= start){
                break;
            }
        }
    }

    /**
     *  本地文件上传到FTP服务器
     * @param ftpPath ftp服务器文件相对路径
     * @param LocalPath 本地文件夹路径
     * @param fileName 上传到服务器上的文件名
     * @return
     */
    public static Boolean uploadLocalFile(String ftpPath,String LocalPath,String fileName) throws IOException {
        login(HOST,PORT,UserName,Password);
        boolean flag = false;
        if (ftpClient != null){
            File file = new File(LocalPath);
            FileInputStream fileInputStream = new FileInputStream(file);
            ftpClient.setBufferSize(Buf_Size);
            // 设置编码，开启服务器对UTF-8的支持;如果服务器支持就是用UTF-8
            // 否则就使用本地编码(GBK)
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(OPTS_UTF8,"on"))){
                localCharset = CharsetUTF8;
            }
            ftpClient.setControlEncoding(localCharset);
            // 二进制格式
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 目录转换编码
            String path = changEncoding(BasePath + ftpPath);
            // 目录不存在，则递归创建
            if (!ftpClient.changeWorkingDirectory(path)){
                createDirectories(path);
            }
            // 设置被动模式，开通一个端口来传输数据
            ftpClient.enterLocalPassiveMode();
            // 上传文件
            String tmpFile = new String((fileName+".tmp").getBytes(localCharset), serverCharset);
            String destFile = new String(fileName.getBytes(localCharset), serverCharset);
            flag = ftpClient.storeFile(tmpFile,fileInputStream);
            if (flag) {
                ftpClient.rename(tmpFile, destFile);
            }
            closeConnect();
        }
        return flag;
    }

    /**
     *  远程文件上传到FTP服务器
     * @param ftpPath FTP服务器相对路径
     * @param remotePath 远程文件路径
     * @param fileName 上传到FTP服务器的文件名
     * @return
     */
    public static Boolean uploadRemoteFile(String ftpPath,String remotePath,String fileName) throws IOException {
        login(HOST,PORT,UserName,Password);
        boolean flag = false;
        if (ftpClient != null){
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(remotePath);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            ftpClient.setBufferSize(Buf_Size);
            // 设置编码
            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand(OPTS_UTF8,"on"))){
                localCharset = CharsetUTF8;
            }
            ftpClient.setControlEncoding(localCharset);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 目录编码转换
            String path = changEncoding(BasePath + ftpPath);
            // 目录不存在，则递归进行创建
            if (!ftpClient.changeWorkingDirectory(path)){
                createDirectories(path);
            }
            // 设置被动模式
            ftpClient.enterLocalPassiveMode();
            // 文件上传
            String tmpFile = new String((fileName+".tmp").getBytes(localCharset), serverCharset);
            String destFile = new String(fileName.getBytes(localCharset), serverCharset);
            flag = ftpClient.storeFile(tmpFile,inputStream);
            if (flag){
                // 重命名
                ftpClient.rename(tmpFile, destFile);
            }
            closeConnect();
            httpClient.close();
            response.close();
        }
        return flag;
    }

    /**
     *  下载文件到本地
     * @param ftpPath ftp服务器相对路径
     * @param fileName 要下载的文件名
     * @param localPath 保存到本地的路径
     * @return
     */
    public static Boolean downLoadfile(String ftpPath,String fileName,String localPath) throws IOException {
        //login(HOST,PORT,UserName,Password);
        boolean flag = false;
        if (ftpClient != null){
            String path = changEncoding(BasePath + ftpPath);
            // 判断目录是否存在
            if (!ftpClient.changeWorkingDirectory(path)){
                log.error(BasePath+ftpPath + " 该目录不存在");
                return flag;
            }
            // 设置被动模式
            ftpClient.enterLocalPassiveMode();;
            String[] files = ftpClient.listNames();
            // 判断该目录下是否有文件
            if (files == null || files.length == 0){
                log.error(BasePath+ftpPath + "  该目录中没有文件存在");
                return flag;
            }
            for (String file:files){
                String ftpname = new String(file.getBytes(serverCharset), localCharset);
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
            closeConnect();
        }
        return flag;
    }

    /**
     *  文件重命名
     */
    private static void renameFile(File destFile,File finalFile){
        if (destFile.exists() && finalFile != null){
            if( destFile.renameTo(finalFile)) {
                log.info("rename file success");
                destFile.delete();
                return;
            }else{
                log.info("rename file error");
                return;
            }
        }else {
            log.info("the file does not exists");
        }
    }

    /**
     *  把ftp目录中的所有文件下载下来
     * @param ftpPath FTP服务器的相对路径
     * @param savePath 保存文件到本地的路径
     * @return
     */
    public static Boolean downLoadAllFiles(String ftpPath,String savePath) throws IOException {
        login(HOST,PORT,UserName,Password);
        boolean flag = false;
        if (ftpClient != null){
            String path = changEncoding(BasePath + ftpPath);
            // 判断目录是否存在
            if (!ftpClient.changeWorkingDirectory(path)){
                log.error(BasePath+ftpPath+" 该目录不存在");
                return flag;
            }
            ftpClient.enterLocalPassiveMode();
            String[] fileNames = ftpClient.listNames();
            // 目录下是否有文件
            if (fileNames == null || fileNames.length == 0){
                log.error(BasePath + ftpPath + " 该目录下没有文件");
                return flag;
            }
            // 遍历文件进行存储操作
            for (String file:fileNames){
                String filename = new String(file.getBytes(serverCharset), localCharset);
                File destFile = new File(savePath + "/" + filename+".tmp");
                File finalFile = new File(savePath + "/" + filename);
                FileOutputStream outputStream = new FileOutputStream(destFile);
                flag = ftpClient.retrieveFile(file,outputStream);
                if (flag){
                    outputStream.flush();
                    outputStream.close();
                    renameFile(destFile,finalFile);
                }
            }
            closeConnect();
        }
        return flag;
    }

    /**
     *  获取目录下所有文件，以字节数组返回
     * @param filePath
     * @return
     */
    public static Map<String,byte[]> getFileBytes(String filePath) throws IOException {
        login(HOST,PORT,UserName,Password);
        Map<String,byte[]> map = new HashMap<>();
        if (ftpClient != null){
            String path = changEncoding(BasePath + ftpClient);
            // 判断目录是否存在
            if (!ftpClient.changeWorkingDirectory(path)){
                log.error(BasePath+ftpClient+" 该目录不存在");
                return map;
            }
            ftpClient.enterLocalPassiveMode();
            // 判断路径下是否有文件
            String[] files = ftpClient.listNames();
            if (filePath == null || files.length == 0){
                log.error(BasePath+ftpClient + " 该目录下不存在文件");
                return map;
            }
            // 遍历文件,获取其byte[]数组
            for (String file : files) {
                InputStream inputStream = ftpClient.retrieveFileStream(file);
                String fileName = new String(file.getBytes(serverCharset), localCharset);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[Buf_Size];
                int readLength = 0;
                while ((readLength = inputStream.read(buffer,0,Buf_Size))>0){
                    byteArrayOutputStream.write(buffer,0,readLength);
                }
                map.put(fileName,byteArrayOutputStream.toByteArray());
            }
        }
        return map;
    }

    /**
     *  根据名称获取文件，以字节数组返回
     * @param ftpPath ftp服务器路径
     * @param fileName 文件名
     * @return
     */
    public static byte[] getFileBytesName(String ftpPath,String fileName) throws IOException {
        login(HOST,PORT,UserName,Password);
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        if (ftpClient != null){
            String path = changEncoding(BasePath + ftpPath);
            // 判断文件目录是否存在
            if (!ftpClient.changeWorkingDirectory(path)){
                log.error(BasePath+ftpPath+" 该目录不存在");
                return arrayOutputStream.toByteArray();
            }
            ftpClient.enterLocalPassiveMode();
            String[] fileNames = ftpClient.listNames();
            // 判断目录中是否有文件
            if (fileNames == null || fileNames.length == 0){
                log.error(BasePath+ftpPath+" 该目录下没有文件");
                return arrayOutputStream.toByteArray();
            }
            for (String file:fileNames){
                String ftpName = new String(file.getBytes(serverCharset), localCharset);
                if (ftpName.equals(fileName)){
                    try(InputStream inputStream = ftpClient.retrieveFileStream(file)){
                        byte[] buffer = new byte[Buf_Size];
                        int len = -1;
                        while ((len = inputStream.read(buffer,0,Buf_Size))>0){
                            arrayOutputStream.write(buffer,0,len);
                        }
                    }
                    break;
                }
            }
            closeConnect();
        }
        return arrayOutputStream.toByteArray();
    }

    /**
     * 获取目录下所有文件的输入流
     * @param ftpPath
     * @return
     */
    public static Map<String,InputStream> getAllFileInputStream(String ftpPath) throws IOException {
        login(HOST,PORT,UserName,Password);
        Map<String,InputStream> map = new HashMap<>();
        if (ftpClient != null){
            String path = changEncoding(BasePath + ftpPath);
            // 判断目录是否存在
            if (!ftpClient.changeWorkingDirectory(path)){
                log.error(BasePath+ftpPath+"  该目录不存在");
                return map;
            }
            ftpClient.enterLocalPassiveMode();
            String[] fileNames = ftpClient.listNames();
            // 判断文件是否有文件存在
            if (fileNames == null || fileNames.length == 0){
                log.error(BasePath+ftpPath + "  该目录下不存在文件");
                return map;
            }
            // 遍历文件，获取输入流
            for (String fileName : fileNames) {
                String ftpName = new String(fileName.getBytes(serverCharset), localCharset);
                InputStream inputStream = ftpClient.retrieveFileStream(fileName);
                map.put(ftpName,inputStream);
                // 处理多个文件
                ftpClient.completePendingCommand();
            }
        }
        return map;
    }

    /**
     *  根据名称获取文件的输入流
     * @param ftpPath
     * @param fileName
     * @return
     */
    public static InputStream getInputStreamByName(String ftpPath,String fileName) throws IOException {
        login(HOST,PORT,UserName,Password);
        InputStream inputStream = null;
        if (ftpClient != null){
            String path = changEncoding(BasePath + ftpPath);
            // 判断目录是否存在
            if (!ftpClient.changeWorkingDirectory(path)){
                log.error(BasePath+ftpPath+" 该目录不存在");
                return inputStream;
            }
            ftpClient.enterLocalPassiveMode();
            String[] fileNames = ftpClient.listNames();
            // 判断目录下是否有文件存在
            if (fileName == null || fileNames.length == 0){
                log.error(BasePath+ftpPath+" 该目录下没有文件存在");
                return inputStream;
            }
            // 遍历查找文件，返回其输入流
            for (String name : fileNames) {
                String ftpName = new String(name.getBytes(serverCharset), localCharset);
                if (fileName.equals(fileName)){
                    inputStream = ftpClient.retrieveFileStream(name);
                    break;
                }
            }
        }
        return inputStream;
    }
    /**
     *  删除指定文件
     * @param filePath
     * @return
     */
    public static Boolean deleteFile(String filePath) throws IOException {
        login(HOST,PORT,UserName,Password);
        boolean flag = false;
        if (ftpClient != null){
            // 文件路径编码转换
            String path = changEncoding(BasePath + filePath);
            // 删除操作
            flag = ftpClient.deleteFile(path);
        }
        return flag;
    }

    /**
     *  删除目录下的所有文件
     * @return 成功返回true
     */
    public static Boolean deleteFiles(String dirPath) throws IOException {
        login(HOST,PORT,UserName,Password);
        boolean flag = false;
        if (ftpClient != null){
            // 设置被动模式，开通一个端口来传输数据
            ftpClient.enterLocalPassiveMode();
            // 把路径转换一个编码
            String path = changEncoding(BasePath + dirPath);
            String[] files = ftpClient.listNames(path);
            // 判断此目录下是否有文件
            if (files == null || files.length == 0){
                log.info(BasePath+dirPath + "改目录为空");
                return flag;
            }
            for (String file:files){
                ftpClient.deleteFile(file);
            }
            flag = true;
            closeConnect();
        }
        return flag;
    }

    public static void listFile() throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles();
        for (FTPFile ftpFile : ftpFiles) {
            log.info(ftpFile.getName());
        }
    }

    public static void main(String[] args) throws IOException {
        String hosts = "192.168.72.18";
        int port  = 21;
        String pwd = "loongson";
        String user = "root";
        String localPath = "H:\\FTPTest";
        FTPUtil.login(hosts,port,user,pwd);
        FTPUtil.listFile();
        FTPUtil.downLoadfile("/mnt","user.txt",localPath);
    }
}
