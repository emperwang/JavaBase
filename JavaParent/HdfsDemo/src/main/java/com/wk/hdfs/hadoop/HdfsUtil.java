package com.wk.hdfs.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

@Slf4j
public class HdfsUtil {

    private static final Properties codecs = loadCodecs();

    /**
     * 加载压缩类型的配置文件
     * @return
     */
    private static Properties loadCodecs() {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("codecs.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    //创建配置文件
    public static Configuration createConfiguration(){
        /*Configuration.addDefaultResource("core-site.xml");
        Configuration.addDefaultResource("hdfs-site.xml");
        Configuration.addDefaultResource("yarn-site.xml");*/
        Configuration config = new Configuration();
        config.set("fs.defaultFs","hdfs://192.168.72.18:9000");
        //config.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        return config;
    }

    //创建hadoop文件系统客户端
    public static FileSystem getHadoopFileSystem(){
        return getHadhoopFileSystem(createConfiguration(),true);
    }

    public static FileSystem getHadoopFileSystem(String configFile) {
        Configuration.addDefaultResource(configFile);
        return getHadhoopFileSystem(new Configuration());
    }

    public static FileSystem getHadhoopFileSystem(Configuration configuration){
        try {
            return FileSystem.get(configuration);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }
    //是否是连接远程的hdfs
    public static FileSystem getHadhoopFileSystem(Configuration configuration,boolean remote){
        try {
            return FileSystem.get(new URI("hdfs://192.168.72.18:9000"),configuration,"root");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    //这是hadoop添加kerberos认证后,需要添加认证的信息
    public static FileSystem getSecurityFileSystem(String defaultUser,String defaultKeyTab) {
        Configuration configuration = HdfsUtil.createConfiguration();
        final String  krb5UserKey = "krb5.user";
        final String  krb5KeyTabKey = "krb5.keytab";

        if(System.getProperties().containsKey(krb5UserKey)){
            configuration.set("hadoop.security.authentication", "kerberos");
            String user = System.getProperty(krb5UserKey, defaultUser);
            String path = System.getProperty(krb5KeyTabKey,defaultKeyTab);
            UserGroupInformation.setConfiguration(configuration);

            try{
                UserGroupInformation.loginUserFromKeytab(user,path);
            }catch (IOException e){
                throw new RuntimeException(e.getMessage(),e);
            }
            log.info("security hdfs returned with user("+user+") and keytab(" + path + ").");
        }else{
            log.info("no kerberos user and keytab file specified, no security hdfs returned.");
        }

        return getHadhoopFileSystem(configuration);
    }

    public static boolean deleteFile(FileSystem fileSystem, Path path){
        try {
            return fileSystem.delete(path,true);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public static boolean deleteFile(FileSystem fileSystem,String path){
        return deleteFile(fileSystem,new Path(path));
    }

    public static boolean exists(FileSystem fileSystem,Path path){
        try {
            return fileSystem.exists(path);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public static boolean exists(FileSystem fileSystem,String path){
        return  exists(fileSystem,new Path(path));
    }

    public static boolean move(FileSystem fileSystem,String oldPath,String newPath){
        try {
            return fileSystem.rename(new Path(oldPath),new Path(newPath));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public static boolean move(FileSystem fileSystem,Path oldPath,Path newPath){
        try {
            return fileSystem.rename(oldPath,newPath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public static boolean concat(FileSystem fileSystem,String trg,String ... srcs){


        return false;
    }

    /**
     * 获取文件大小
     * @param fileSystem  文件系统
     * @param filePath   文件路径
     * @return
     */
    public static long getFileSize(FileSystem fileSystem,Path filePath){
        return getFileStaus(fileSystem,filePath).getLen();
    }

    public static long getFileSize(FileSystem fileSystem,String filePath){
        return getFileSize(fileSystem,new Path(filePath));
    }

    /**
     *  获取文件的状态
     * @param fileSystem  文件系统
     * @param filePath   文件路径
     * @return
     */
    public static FileStatus getFileStaus(FileSystem fileSystem, Path filePath) {
        try {
            FileStatus status = fileSystem.getFileStatus(filePath);
            return status;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     *  获取一个文件夹下的文件的状态
     * @param fileSystem
     * @param directory
     * @return
     */
    public static FileStatus[] list(FileSystem fileSystem,Path directory){
        try {
            return fileSystem.listStatus(directory);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public static FileStatus[] list(FileSystem fileSystem,String directory){
        return list(fileSystem,new Path(directory));
    }

    public static void close(FileSystem fileSystem){
        try {
            if (fileSystem != null) {
                fileSystem.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    private static CompressionCodec getCompressionCodec(FileSystem fileSystem,Path path){
        String name = path.getName();
        String codecClassName = null;
        if(StringUtils.isNotEmpty(name)){
            int i = name.lastIndexOf(".");
            if(i>-1){
                //获取压缩文件的尾缀
                String suf = StringUtils.lowerCase(name.substring(i));
                //获取压缩格式对应的压缩类名
                codecClassName = codecs.getProperty(suf);
            }
        }
        log.info("codecClassName = "+codecClassName + ",path="+path);
        //如果没有对应的类 返回null
        if(StringUtils.isEmpty(codecClassName)){
            return null;
        }
        try{
            //通过反射获取类
            return (CompressionCodec) ReflectionUtils.newInstance(
                    Class.forName(codecClassName),fileSystem.getConf()
            );
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 判断是否支持某种类型的压缩
     * @param compressionCodec
     * @return
     */
    public static boolean supportCompressionCodec(String compressionCodec){
        if(StringUtils.isEmpty(compressionCodec)){
            return false;
        }
        return StringUtils.isEmpty(codecs.getProperty(compressionCodec));
    }

    /**
     *  获取追加文件内容的文件流
     * @param fileSystem
     * @param path
     * @return
     */
    public static FSDataOutputStream appendOutputStream(FileSystem fileSystem,Path path){
        try {
            FSDataOutputStream fsDataOutputStream = fileSystem.append(path);
            return fsDataOutputStream;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public static FSDataOutputStream createOutputStream(FileSystem fileSystem,Path path,boolean overwrite){
        try {
            return fileSystem.create(path,overwrite);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     *  创建压缩文件的输入流,可以设置是否覆盖和是否追加
     * @param fileSystem
     * @param path
     * @param overwrite  是否覆盖
     * @param append  是否追加
     * @return
     */
    public static OutputStream createCompressionOutputStream(FileSystem fileSystem,Path path,
                                                             boolean overwrite,boolean append){
        CompressionCodec compressionCodec = getCompressionCodec(fileSystem, path);
        OutputStream os = null;
        try{
            FSDataOutputStream outputStream;
            //文件存在,并设置为追加,则对文件进行追加
            if(exists(fileSystem,path) && append){
                outputStream = appendOutputStream(fileSystem, path);
            }else{ //不然创建新文件
                outputStream = fileSystem.create(path,overwrite);
            }
            //创建压缩型的输出流
            if (compressionCodec != null){
                os = compressionCodec.createOutputStream(outputStream);
            }
            if(os == null){
                os = outputStream;
            }
        }catch (IOException e){
            throw new RuntimeException(e.getMessage(),e);
        }
        return os;
    }

    public static InputStream createInputStream(FileSystem fileSystem,String path){
        return createInputStream(fileSystem,new Path(path));
    }

    public static InputStream createInputStream(FileSystem fileSystem,Path path){
        CompressionCodec compressionCodec = getCompressionCodec(fileSystem, path);
        InputStream is = null;
        try{
            FSDataInputStream inputStream = fileSystem.open(path);
            //创建压缩型的输出流
            if (compressionCodec != null){
                is = compressionCodec.createInputStream(inputStream);
            }
            //创建普通的输出流
            if (is == null){
                is = inputStream;
            }
        }catch (IOException e){
            throw new RuntimeException(e.getMessage(),e);
        }
        return is;
    }
}
