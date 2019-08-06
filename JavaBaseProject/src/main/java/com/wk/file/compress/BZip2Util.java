package com.wk.file.compress;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BZip2Util {
    private static Logger logger = LoggerFactory.getLogger(BZip2Util.class);
    private static String To_Compress_File = "H:/FTPTest/测试文件.txt";
    private static String To_Compress_Dir = "H:/FTPTest";
    private static String To_UnCompress_File = "H:/FTPTest/测试文件.txt.bz2";
    private static String Dest_Path = "D:/image";

    private static Integer Buf_Size = 1024;
    private static String EXT = ".bz2";

    /**
     *  压缩数据
     * @param is
     * @param os
     */
    public void bzCompress(InputStream is, OutputStream os) throws IOException {
        BZip2CompressorOutputStream bzos = new BZip2CompressorOutputStream(os);
        int len = 0;
        byte[] buf = new byte[Buf_Size];
        while ((len = is.read(buf)) != -1){
            bzos.write(buf,0,len);
        }
        bzos.finish();

        bzos.flush();
        bzos.close();
    }

    /**
     *  压缩数据
     * @param data
     * @return
     */
    public byte[] bzCompress(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bzCompress(bais,baos);

        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    /**
     *  压缩文件
     * @param filePath
     */
    public void bzCompress(String filePath) throws IOException {
        validataPath(filePath);
        bzCompress(filePath,true);
    }

    /**
     *  压缩文件
     *      压缩到当前目录
     * @param filePath 要压缩的文件
     * @param delete 是否删除原文件
     */
    public void bzCompress(String filePath,boolean delete) throws IOException {
        validataPath(filePath);
        File file = new File(filePath);
        if (!file.isFile()){
            logger.error(filePath + " is not a file");
            return;
        }
        String parent = file.getParent();
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(new File(parent, file.getName() + EXT));

        bzCompress(fis,fos);
        fis.close();
        logger.info(file.getName() + "  压缩完成");
        if (delete){
            file.delete();
        }
    }

    /**
     *  压缩文件
     * @param filePath 要压缩的文件
     * @param destPath 压缩的保存目录
     * @param delete 是否删除原目录
     */
    public void bzCompress(String filePath,String destPath,boolean delete) throws IOException {
        validataPath(filePath);
        validataPath(destPath);
        File file = new File(filePath);
        if (!file.isFile()){
            logger.error(filePath + " is not a file");
            return;
        }
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(new File(destPath, file.getName() + EXT));
        bzCompress(fis,fos);

        fis.close();
        logger.info(file.getName() + "  压缩完成");
        if (delete){
            file.delete();
        }
    }

    /**
     *  解压缩
     * @param is
     * @param os
     */
    public void bzDeCompress(InputStream is,OutputStream os) throws IOException {
        BZip2CompressorInputStream bzIs = new BZip2CompressorInputStream(is);
        int len = 0;
        byte[] buf = new byte[Buf_Size];
        while ((len = bzIs.read(buf)) != -1){
            os.write(buf,0,len);
        }
        bzIs.close();
    }

    /**
     *  解压缩文件
     * @param filePath
     */
    public void bzDeCompress(String filePath) throws IOException {
        validataPath(filePath);
        bzDeCompress(filePath,true);
    }

    /**
     *  对文件进行解压缩
     *      解药到当前目录
     * @param filePath 要解压的文件
     * @param delete 是否删除原文件
     */
    public void bzDeCompress(String filePath,boolean delete) throws IOException {
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
        bzDeCompress(fis,fos);
        fos.flush();
        fos.close();
        logger.info(file.getName() + " 文件解压成功");
        if (delete){
            file.delete();
        }
    }

    /**
     *  把文件解压的指定目录
     * @param filePath 要看解压的文件
     * @param destPath 指定目录
     * @param delete 是否删除原文件
     */
    public void bzDeCompress(String filePath,String destPath,boolean delete) throws IOException {
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
        bzDeCompress(fis,fos);

        fos.flush();
        fos.close();
        logger.info(file.getName()+" 文件解压成功");
        if (delete){
            file.delete();
        }
    }

    /**
     *  数据解压缩
     * @param data
     * @return
     */
    public byte[] bzDeCompress(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bzDeCompress(bais,baos);
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
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
        BZip2Util bZip2Util = new BZip2Util();
        // 压缩文件
        //bZip2Util.bzCompress(To_Compress_File,false);
        //bZip2Util.bzCompress(To_Compress_File,Dest_Path,true);
        
        // 解压缩
        bZip2Util.bzDeCompress(To_UnCompress_File,false);
        bZip2Util.bzDeCompress(To_UnCompress_File,Dest_Path,true);
    }
}
