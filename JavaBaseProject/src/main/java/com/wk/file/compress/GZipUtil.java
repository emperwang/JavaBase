package com.wk.file.compress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipUtil {
    private static Logger logger = LoggerFactory.getLogger(GZipUtil.class);
    private static String To_Comptess_File = "H:/FTPTest/测试文件.txt";
    private static String To_Comptess_Dir = "H:/FTPTest";
    private static String To_UnComptess_File = "H:/FTPTest/测试文件.txt.gz";
    private static String Dest_Path = "D:/image";

    private static Integer Buf_Size = 1024;
    private static String EXT = ".gz";
    /**
     *  对数据进行压缩
     */
    public byte[] compress(byte[] data) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        compress(bis,bos);

        byte[] bytes = bos.toByteArray();
        bos.flush();
        bos.close();
        bis.close();
        logger.info("压缩完成.");
        return bytes;
    }

    /**
     *  文件压缩
     */
    public void compress(String filePath) throws IOException {
        validataPath(filePath);
        compress(filePath,true);
    }

    /**
     *  文件压缩
     *   默认压缩到当前目录
     * @param filePath 要压缩的文件
     * @param delete 是否删除源文件
     */
    public void compress(String filePath,boolean delete) throws IOException {
        validataPath(filePath);
        File file = new File(filePath);
        if (!file.isFile()){
            logger.error(filePath + " is not a file");
            return;
        }
        String parent = file.getParent();
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(new File(parent,file.getName() + EXT));
        compress(fis,fos);
        fis.close();
        logger.info(file.getName() + " 压缩完成.");
        if (delete){
            file.delete();
        }
    }

    /**
     *  文件压缩
     * @param filePath 要压缩的文件
     * @param destPath 压缩后的文件存放位置
     * @param delete 是否删除源文件
     */
    public void compress(String filePath,String destPath,boolean delete) throws IOException {
        validataPath(filePath);
        validataPath(destPath);
        File file = new File(filePath);
        if (!file.isFile()){
            logger.error(filePath + " is not a file");
            return;
        }
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(new File(destPath,file.getName() + EXT));
        compress(fis,fos);
        fis.close();
        logger.info(file.getName() +" 压缩完成.");
        if (delete){
            file.delete();
        }
    }

    /**
     *  gzip压缩
     * @param inputStream 要压缩的输入流
     * @param outputStream 压缩的输出流
     * @throws IOException
     */
    private void compress(InputStream inputStream, OutputStream outputStream) throws IOException {
        GZIPOutputStream gzos = new GZIPOutputStream(outputStream);
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = inputStream.read(buf)) != -1){
            gzos.write(buf,0,len);
        }
        gzos.finish();
        gzos.flush();
        gzos.close();
    }

    /**
     *  对数据进行解压缩
     * @param data
     * @return
     */
    public byte[] deCompress(byte[] data) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        deCompress(bis,bos);
        byte[] result = bos.toByteArray();
        bos.flush();
        bos.close();
        return result;
    }

    /**
     *  对文件进行解压缩
     *      解压缩当前目录
     * @param filePath
     */
    public void deCompress(String filePath) throws IOException {
        deCompress(filePath,true);
    }

    /**
     *  对文件进行解压
     * @param filePath 要解压的文件
     * @param delete 是否删除源文件
     * @throws IOException
     */
    public void deCompress(String filePath,boolean delete) throws IOException {
        validataPath(filePath);
        File file = new File(filePath);
        if (!file.isFile()){
            logger.error(filePath + " is not a file");
            return;
        }
        String name = file.getName().substring(0, file.getName().lastIndexOf("."));
        String parent = file.getParent();
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(new File(parent, name));

        deCompress(fis,fos);
        fos.flush();
        fos.close();
        logger.info(file.getName() +" 解压完成.");
        if (delete){
            file.delete();
        }
    }

    /**
     *  解压文件到指定目录中
     * @param filePath  要解压的文件
     * @param delete 是否删除原文件
     * @param destPath 保存的目的目录
     */
    public void deCompress(String filePath,boolean delete,String destPath) throws IOException {
        validataPath(filePath);
        validataPath(destPath);
        File file = new File(filePath);
        if (!file.isFile()){
            logger.error(filePath + " is not a file");
            return;
        }
        String name = file.getName().substring(0, file.getName().lastIndexOf("."));
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(new File(destPath, name));

        deCompress(fis,fos);
        fos.flush();
        fos.close();
        logger.info(file.getName() +" 解压完成.");
        if (delete){
            file.delete();
        }
    }

    /**
     *  解压缩
     * @param inputStream 要进行解压缩的文件
     * @param outputStream
     */
    private void deCompress(InputStream inputStream,OutputStream outputStream) throws IOException {
        GZIPInputStream gzis = new GZIPInputStream(inputStream);
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = gzis.read(buf)) != -1){
            outputStream.write(buf,0,len);
        }
        gzis.close();
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

    public static void main(String[] args) throws IOException {
        GZipUtil gZipUtil = new GZipUtil();

        // 压缩文件
        //gZipUtil.compress(To_Comptess_File,true);
        //gZipUtil.compress(To_Comptess_File,Dest_Path,true);

        // 解压缩
        //gZipUtil.deCompress(To_UnComptess_File,false);
        //gZipUtil.deCompress(To_UnComptess_File,true,Dest_Path);


    }
}
