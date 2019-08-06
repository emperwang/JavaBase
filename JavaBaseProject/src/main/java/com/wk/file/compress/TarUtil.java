package com.wk.file.compress;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class TarUtil {
    private static Logger logger = LoggerFactory.getLogger(TarUtil.class);
    private static String To_Compress_File = "H:/FTPTest/测试文件.txt";
    private static String To_Compress_Dir = "H:/FTPTest";
    private static String To_UnCompress_File = "H:/FTPTest/测试文件.txt.tar";
    private static String Dest_Path = "D:/image";

    private static Integer Buf_Size = 1024;
    private static String EXT = ".tar";

    /**
     *  对文件或目录进行归档操作
     *      并且归档文件在当前目录下
     * @param filePath
     */
    public void archive(String filePath) throws IOException {
        validataPath(filePath);
        File file = new File(filePath);
        String parent = file.getParent();
        String name = file.getName();
        String destPath = parent + File.separator+name+EXT;
        archive(file,destPath);
    }

    /**
     *  针对目录和文件进行不同的归档操作
     * @param srcFile
     * @param destPath
     */
    private void archive(File srcFile,String destPath) throws IOException {
        validataPath(destPath);
        TarArchiveOutputStream taos = new TarArchiveOutputStream(new FileOutputStream(destPath));
        archive(srcFile,taos,"");
        taos.close();
    }

    /**
     *  归档操作
     * @param srcFile 要进行归档的文件或目录
     * @param taos 归档文件
     * @param baseDir srcFile在归档文件中的相对路径
     * @throws IOException
     */
    private void archive(File srcFile,TarArchiveOutputStream taos,String baseDir) throws IOException {
        if (srcFile.isDirectory()){
            archiveDir(srcFile,taos,baseDir);
        }else {
            archiveFile(srcFile,taos,baseDir);
        }
    }

    /**
     *  对目录进行归档操作
     * @param fileDir
     * @param taos
     */
    private void archiveDir(File fileDir,TarArchiveOutputStream taos,String baseDir) throws IOException {
        File[] files = fileDir.listFiles();
        String path = "/";
        // 空目录
        if (files == null || files.length < 1){
            TarArchiveEntry entry = new TarArchiveEntry(fileDir.getName() + path);
            taos.putArchiveEntry(entry);
            taos.closeArchiveEntry();
        }
        // 递归归档
        for (File file : files) {
            archive(file,taos,baseDir + fileDir.getName() + path);
        }
    }
    /**
     *  把文件归档
     * @param file 要归档的文件
     * @param taos 归档文件
     */
    private void archiveFile(File file, TarArchiveOutputStream taos,String baseDir) throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(baseDir + file.getName());

        entry.setSize(file.length());
        taos.putArchiveEntry(entry);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        int count = 0;
        byte[] data = new byte[Buf_Size];
        while ((count = bis.read(data)) != -1){
            taos.write(data,0,count);
        }
        bis.close();
        taos.closeArchiveEntry();
        logger.info(file.getName()+" 归档成功");
    }
/******************************************************上面是归档--下面是解归档******************************************************************************/

    /**
     *  对文件进行解归档操作
     *     此处解压到当前目录
     * @param filePath
     */
    public void dearchive(String filePath) throws IOException {
        validataPath(filePath);
        File archFile = new File(filePath);
        if (archFile.isDirectory()){
            logger.error(filePath +"  is not a file");
            return;
        }
        String name = archFile.getName().substring(0, archFile.getName().lastIndexOf("."));
        String parent = archFile.getParent();
        String destPath = parent +File.separator;
        dearchive(archFile,destPath);
    }

    /**
     *  解压操作
     * @param filePath 要解压的文件
     * @param destPath 解压后的目录
     */
    public void dearchive(String filePath,String destPath) throws IOException {
        validataPath(filePath);
        File archFile = new File(filePath);
        if (archFile.isDirectory()){
            logger.error(filePath +"  is not a file");
            return;
        }
        dearchive(archFile,destPath);
    }

    /**
     *  解归档
     * @param archFile  归档文件
     * @param destPath 目录文件
     */
    private void dearchive(File archFile,String destPath) throws IOException {
        validataPath(destPath);
        File destFile = new File(destPath);
        if (!destFile.isDirectory()){
            logger.error(destPath +"  is not a directory");
            return;
        }
        dearchive(archFile,destFile);
    }

    /**
     *  解接档操作
     * @param srcFile
     * @param destFile
     */
    private void dearchive(File srcFile,File destFile) throws IOException {
        TarArchiveInputStream tais = new TarArchiveInputStream(new FileInputStream(srcFile));
        dearchive(destFile,tais);

        tais.close();
    }

    /**
     *  解归档
     * @param destFile 目的文件
     * @param tais 归档文件输入流
     * @throws IOException
     */
    private void dearchive(File destFile,TarArchiveInputStream tais) throws IOException {
        TarArchiveEntry entry = null;

        while ((entry = tais.getNextTarEntry()) != null){
            String path = destFile.getPath();
            String dir = path + File.separator + entry.getName();
            File dirFile = new File(dir);

            fileProber(dirFile);
            if (entry.isDirectory()){
                dirFile.mkdirs();
            }else {
                dearchiveFile(dirFile,tais);
            }
        }
    }

    /**
     *  解归档
     * @param destFile 目标文件
     * @param tais
     */
    public void dearchiveFile(File destFile, TarArchiveInputStream tais) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));

        int count = 0;
        byte[] data = new byte[Buf_Size];
        while ((count = tais.read(data,0,Buf_Size)) != -1){
            bos.write(data,0,count);
        }
        bos.close();
    }
    /**
     *  对给出额路径进行检查
     */
    private void validataPath(String path){
        if (path == null || path.length() == 0){
            logger.error(path +"  must be set");
            throw new RuntimeException("filePath must be set");
        }
    }

    /**
     *  文件探针，查看上级目录是否存在
     * @param file
     */
    private void fileProber(File file){
        File parentFile = file.getParentFile();
        if (!parentFile.exists()){
            fileProber(parentFile);
            parentFile.mkdir();
        }
    }

    public static void main(String[] args) throws IOException {
        TarUtil tarUtil = new TarUtil();
        // 归档操作
        /*tarUtil.archive(To_Compress_Dir);
        tarUtil.archive(To_Compress_File);*/

        //tarUtil.dearchive(To_UnCompress_File);
        tarUtil.dearchive(To_UnCompress_File,Dest_Path);
    }
}
