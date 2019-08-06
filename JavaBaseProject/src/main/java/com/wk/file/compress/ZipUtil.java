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
    private static String To_Compress_Dir = "H:/压缩目录/";
    private static String To_UnCompress_File = "H:/FTPTest/压缩目录.zip";
    private static String Dest_File = "D:/image";

    /**
     *  压缩一个目录
     *      默认是生成在压缩目录的上级目录
     */
    public void zipDirectory(String path) throws IOException {
        validataPath(path);
        File file = new File(path);
        if (!file.isDirectory()){
            logger.error(path + " is not a directory");
            return;
        }
        String parent = file.getParent();
        File zipFile = new File(parent, file.getName() +".zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zip(zos,file,file.getName());
        zos.flush();
        zos.close();
        logger.info("compress success");
    }

    /**
     *  压缩目录
     * @param path 要压缩的目录
     * @param destPath 压缩文件生成的目录
     * @throws IOException
     */
    public void zipDirectory(String path,String destPath) throws IOException {
        validataPath(path);
        validataPath(destPath);
        File file = new File(path);
        if (!file.isDirectory()){
            logger.error(path + " is not a directory");
            return;
        }
        File zipFile = new File(destPath, file.getName() +".zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zip(zos,file,file.getName());
        zos.flush();
        zos.close();
        logger.info("compress success");
    }
    /**
     *  压缩一个文件
     *   默认生成在同一个目录下
     * @param filePath
     */
    public void zipFile(String filePath) throws IOException {
        validataPath(filePath);
        File file = new File(filePath);
        if (!file.isFile()){
            logger.error(filePath + " is not a file path");
            return;
        }
        String name = file.getName().substring(0,file.getName().lastIndexOf("."));
        String parent = file.getParent();
        File zipFile = new File(parent,name + ".zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zip(zos,file,file.getName());
        zos.flush();
        zos.close();
        logger.info("compress success");
    }

    /**
     *  压缩指定文件
     * @param filePath 要压缩的文件
     * @param savePath 生成的目录
     * @throws IOException
     */
    public void zipFile(String filePath,String savePath) throws IOException {
        validataPath(filePath);
        validataPath(savePath);
        File file = new File(filePath);
        if (!file.isFile()){
            logger.error(filePath + " is not a file path");
            return;
        }
        String name = file.getName().substring(0,file.getName().lastIndexOf("."));
        File zipFile = new File(savePath,name + ".zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zip(zos,file,file.getName());
        zos.flush();
        zos.close();
    }

    /**
     *  压缩
     * @param zos 压缩文件的输入流
     * @param file 要压缩的文件
     * @param path 当前文件相对于压缩文件夹的路径
     * @throws IOException
     */
    private void zip(ZipOutputStream zos,File file,String path) throws IOException {
        zip(zos, file, path,"UTF-8",Deflater.DEFAULT_COMPRESSION);
    }

    /**
     * @param zos 压缩文件的输入流
     * @param file 要压缩的文件
     * @param path 当前文件相对于压缩文件夹的路径
     * @param encoding 编码
     * @param level 压缩等级
     * @throws IOException
     */
    private void zip(ZipOutputStream zos,File file,String path,String encoding,int level) throws IOException {
        zos.setEncoding(encoding); // 压缩编码
        // zos.setComment("压缩测试"); // 压缩注释   // 当使用utf-8编码时，写入comment有乱码，修改为gbk后，comment就没有乱码了
        zos.setLevel(level); // 压缩等级
        //zos.setMethod(ZipOutputStream.DEFAULT_COMPRESSION);
        // 判断是文件还是文件夹，文件直接写入目录进入点，文件夹则进行遍历
        if (file.isDirectory()){
            ZipEntry zipEntry = new ZipEntry(path + File.separator);
            zos.putNextEntry(zipEntry);
            File[] files = file.listFiles();
            for (File tmp : files) {
                zip(zos,tmp,path+File.separator+tmp.getName());
            }
        } else { // 是文件，则直接进行写入
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(path);
            zos.putNextEntry(zipEntry);  // 创建目录进入点
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1){
                zos.write(buf,0,len);
            }
            zos.flush();
            fis.close();
            zos.closeEntry();  // 关闭当前目录进入点，将输入流移动到下一个目录进入点
        }
    }
/*******************************压缩---解压缩***********************************************************/
    /**
     *  使用默认编码进行解压
     * @param zipFile
     * @throws IOException
     */
    public void unzip(String zipFile) throws IOException {
        unzip(zipFile,"UTF-8");
    }
    /**
     *  解压文件
     *      默认解压到当前目录
     * @param zipFile
     */
    public void unzip(String zipFile,String encoding) throws IOException {
        validataPath(zipFile);
        File file = new File(zipFile);
        if (!file.isFile()){
            logger.error(zipFile + " is not a file path");
            return;
        }
        String parent = file.getParent();
        ZipFile toUnZip = new ZipFile(zipFile,encoding);
        doUnZip(toUnZip,parent);
    }

    /**
     *  解压文件
     * @param zipFile  要解压的文件
     * @param destPath 解压的路径
     * @throws IOException
     */
    public void unzip(String zipFile,String destPath,String encoding) throws IOException {
        validataPath(zipFile);
        validataPath(destPath);
        File file = new File(zipFile);
        if (!file.isFile()){
            logger.error(zipFile + " is not a file path");
            return;
        }
        ZipFile toUnZip = new ZipFile(zipFile,encoding);
        doUnZip(toUnZip,destPath);
    }
    /**
     *  解压操作
     * @param zipFile 要解压的文件输入流
     * @param savePath 解压文件保存目录
     */
    private void doUnZip(ZipFile zipFile,String savePath) throws IOException {
        Enumeration<ZipEntry> entries = zipFile.getEntries();
        String encoding = zipFile.getEncoding();
        logger.info("encoding:"+encoding);
        byte[] buf = new byte[1024];
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            if (zipEntry != null) {
                String name = zipEntry.getName();
                byte[] rawName = zipEntry.getRawName();
                logger.info("name is:{},rawname is:{}", name, new String(rawName));
                File file = new File(savePath +File.separator + name);
                if (zipEntry.isDirectory()){
                    file.mkdirs();
                }else {
                    // 指定文件目录不存在，则创建其目录
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()){
                        parentFile.mkdirs();
                    }
                    int len = 0;
                    // 获取该压缩实体的输入流
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    while ((len = inputStream.read(buf)) != -1){
                        outputStream.write(buf,0,len);
                    }
                    outputStream.close();
                    inputStream.close();
                }
            }
        }
        zipFile.close();
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
        //zipUtil.zipDirectory(To_Compress_Dir);
        // 压缩一个文件
        //zipUtil.zipFile(To_Compress_File);
        // 压缩到指定目录
        //zipUtil.zipDirectory(To_Compress_Dir,Dest_File);
        //zipUtil.zipFile(To_Compress_File,Dest_File);

        // 使用GBK UTF-8 进行解压
        //zipUtil.unzip(To_UnCompress_File,"GBK");
        //zipUtil.unzip(To_UnCompress_File,"UTF-8");

        // 解压到指定目录
        zipUtil.unzip(To_UnCompress_File,Dest_File,"UTF-8");
    }
}
