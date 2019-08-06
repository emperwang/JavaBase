package com.wk.file.compress;

// 使用ant.jar 包,解决中文乱码问题

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.Deflater;

// 说是使用原生导致中文乱码，目录测试没有复现内容
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;

/**
 *  使用zip方式对文件的压缩和解压缩
 */
public class ZipUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);
    private static String To_Compress_File = "H:/FTPTest/test1.txt";
    private static String To_Compress_Dir = "H:/FTPTest/";
    private static String To_UnCompress_File = "H:/FTPTest/FTPTest.zip";
    private static String Dest_File = "D:/image";
    private static String EXT = ".zip";

    private static int Buf_Size = 1024;

    private static String Charset_UTF8 = "UTF-8";

    /**
     *  归档一个文件 或 文件夹
     *      归档目录在当前目录中
     * @param filePath
     */
    public void zip(String filePath) throws IOException {
        validataPath(filePath);
        File file = new File(filePath);
        String parent = file.getParent();
        String name = file.getName();
        String destPath = parent + File.separator + name + EXT;
        zip(file,destPath);
    }

    public void zip(String filePath,String encoding,Integer level) throws IOException {
        validataPath(filePath);
        File file = new File(filePath);
        String parent = file.getParent();
        String name = file.getName();
        String destPath = parent + File.separator + name + EXT;
        zip(file,destPath,encoding,level);
    }
    /**
     *  归档文件或目录
     *      归档文件在destPath目录中
     * @param filePath
     * @param destPath
     */
    public void zip(String filePath,String destPath) throws IOException {
        validataPath(filePath);
        validataPath(destPath);
        File file = new File(filePath);
        zip(file,destPath+File.separator+file.getName() + EXT);
    }

    /**
     *  指定编码格式和压缩等级
     * @param filePath
     * @param destPath
     * @param encoding
     * @param level
     * @throws IOException
     */
    public void zip(String filePath,String destPath,String encoding,Integer level) throws IOException {
        validataPath(filePath);
        validataPath(destPath);
        File file = new File(filePath);
        zip(file,destPath+File.separator+file.getName() + EXT,encoding,level);
    }
    /**
     *  压缩
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    private void zip(File srcFile,String destFile) throws IOException {
        File file = new File(destFile);
        zip(srcFile,file);
    }

    /**
     *  压缩
     * @param srcFile
     * @param destFile
     * @param encoding
     * @param level
     * @throws IOException
     */
    private void zip(File srcFile,String destFile,String encoding,Integer level) throws IOException {
        File file = new File(destFile);
        zip(srcFile,file,encoding,level);
    }

    private void zip(File srcFile,File destFile) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destFile));
        zip(srcFile,zos,"");
        zos.close();
    }

    /**
     *  压缩
     * @param srcFile 要归档的文件
     * @param destFile 归档文件
     * @param encoding 编码
     * @param level 压缩等级
     * @throws IOException
     */
    private void zip(File srcFile,File destFile,String encoding,Integer level) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destFile));
        zip(srcFile,zos,"",encoding,level);
        zos.close();
    }

    /**
     *  根据目录或文件进行不同的操作
     * @param srcFile 要贵的那个的文件后目录
     * @param zos 归档输出流
     * @param baseDir 当前文件想对于归档文件的路径
     */
    private void zip(File srcFile, ZipOutputStream zos,String baseDir) throws IOException {
        if (srcFile.isDirectory()){
            zipDir(srcFile,zos,baseDir);
        }else {
            zipFile(srcFile,zos,baseDir,Charset_UTF8, Deflater.DEFAULT_COMPRESSION);
        }
    }

    /**
     *  压缩文件
     * @param srcFile
     * @param zos
     * @param baseDir
     * @param encoding  编码
     * @param level 压缩等级
     * @throws IOException
     */
    private void zip(File srcFile, ZipOutputStream zos,String baseDir,String encoding,Integer level) throws IOException {
        if (srcFile.isDirectory()){
            zipDir(srcFile,zos,baseDir);
        }else {
            zipFile(srcFile,zos,baseDir,encoding, level);
        }
    }
    /**
     *  归档文件
     * @param srcFile
     * @param zos
     */
    private void zipFile(File srcFile, ZipOutputStream zos,String baseDir,String encoding,Integer level) throws IOException {
        zos.setLevel(level);
        zos.setEncoding(encoding);
        FileInputStream fis = new FileInputStream(srcFile);
        ZipEntry zipEntry = new ZipEntry(baseDir + srcFile.getName());
        zos.putNextEntry(zipEntry);
        int len = 0;
        byte[] buf = new byte[Buf_Size];
        while ((len = fis.read(buf)) != -1){
            zos.write(buf,0,len);
        }
        fis.close();
        zos.closeEntry();
        logger.info(srcFile.getName() + " 压缩完成");
    }

    /**
     *  归档目录
     * @param srcFile 要归档的目录
     * @param zos 归档文件
     */
    private void zipDir(File srcFile, ZipOutputStream zos,String baseDir) throws IOException {
        File[] files = srcFile.listFiles();
        // 空目录
        if (files == null || files.length < 1){
            ZipEntry zipEntry = new ZipEntry(baseDir + srcFile.getName() + File.separator);
            zos.putNextEntry(zipEntry);
            zos.closeEntry();
            return;
        }
        // 对文件进行递归遍历
        for (File file : files) {
            zip(file,zos,baseDir + srcFile.getName()+File.separator);
        }
    }
/*********************************************压缩---解压缩*************************************************************************/
    /**
     *  对文件进行解压缩
     *      解压到当前文件夹
     * @param filePath 要解压缩的文件
     */
    public void unZip(String filePath) throws IOException {
        validataPath(filePath);
        File file = new File(filePath);
        if (file.isDirectory()) {
            logger.error(filePath + "  should be a file");
            return;
        }
        String name = file.getName().substring(0, file.getName().lastIndexOf("."));
        String parent = file.getParent();
        String destPath  = parent + File.separator + name;
        unZip(file,destPath);
    }

    /**
     *  解压缩文件到指定目录
     * @param filePath 要解压的文件
     * @param destPath 指定的目录
     */
    public void unZip(String filePath,String destPath) throws IOException {
        validataPath(filePath);
        validataPath(destPath);
        File file = new File(filePath);
        if (file.isDirectory()) {
            logger.error(filePath + "  should be a file");
            return;
        }
        unZip(file,destPath);
    }

    /**
     *  解压文件
     * @param srcFile
     * @param destPath
     * @throws IOException
     */
    private void unZip(File srcFile,String destPath) throws IOException {
        File file = new File(destPath);
        if (file.isFile()) {
            logger.error(destPath + "  should be a directory");
            return;
        }
        ZipFile zipFile = new ZipFile(srcFile,Charset_UTF8);
        unZip(zipFile,destPath);
        zipFile.close();
        logger.info(srcFile.getName() + " 解压完成");
    }

    /**
     *  指定编码格式
     * @param srcFile
     * @param destPath
     * @param encoding
     * @throws IOException
     */
    private void unZip(File srcFile,String destPath,String encoding) throws IOException {
        File file = new File(destPath);
        if (file.isFile()) {
            logger.error(destPath + "  should be a directory");
            return;
        }
        ZipFile zipFile = new ZipFile(srcFile,encoding);
        unZip(zipFile,destPath);
        zipFile.close();
        logger.info(srcFile.getName() + " 解压完成");
    }
    /**
     *  解压文件
     * @param zipFile
     * @param destPath
     */
    private void unZip(ZipFile zipFile,String destPath) throws IOException {
        Enumeration<ZipEntry> entries = zipFile.getEntries();
        byte[] buf = new byte[Buf_Size];
        while (entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            String name = entry.getName();
            File destFile = new File(destPath+File.separator+name);
            if (entry.isDirectory()){
                destFile.mkdirs();
            }else {
                fileProbe(destFile);
                int len = 0;
                InputStream inputStream = zipFile.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(destFile);
                while ((len = inputStream.read(buf)) != -1){
                    fos.write(buf,0,len);
                }
                fos.close();
                inputStream.close();
            }
        }
    }

    /**
     *  文件探针
     * @param file
     */
    private void fileProbe(File file){
        File parentFile = file.getParentFile();
        if (!parentFile.exists()){
            fileProbe(parentFile);
            parentFile.mkdir();
        }
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
        ZipUtil zipUtil = new ZipUtil();
        // 压缩一个目录
        //zipUtil.zip(To_Compress_Dir);
        //zipUtil.zip(To_Compress_Dir,Dest_File);

        // 解压缩
        //zipUtil.unZip(To_UnCompress_File);
         zipUtil.unZip(To_UnCompress_File,Dest_File);
    }
}
