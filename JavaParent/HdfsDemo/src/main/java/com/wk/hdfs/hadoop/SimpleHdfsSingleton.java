package com.wk.hdfs.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Hdfs Client Singleton
 * <br />
 * Make sure that only single hdfs client in the process.
 */
@Slf4j
public class SimpleHdfsSingleton {
    private FileSystem fs;

    //创建一个客户端实例
    private SimpleHdfsSingleton(){
        this.fs = HdfsUtil.getHadoopFileSystem();
        log.info("create hadoop file system:"+fs);
    }

    public boolean exists(Path path){
        return HdfsUtil.exists(fs,path);
    }

    public boolean exists(String path){
        return HdfsUtil.exists(fs,path);
    }

    public FileStatus[] listStatus(Path path){
        return HdfsUtil.list(fs,path);
    }

    public FileStatus[] listStatus(String  path){
        return HdfsUtil.list(fs,path);
    }
    public FSDataOutputStream create(Path file){
        return HdfsUtil.createOutputStream(fs,file,true);
    }

    public OutputStream createCompressionOutputStream(Path path,boolean overwrite,boolean append){
        return HdfsUtil.createCompressionOutputStream(fs,path,overwrite,append);
    }

    public void close(){
        HdfsUtil.close(fs);
    }

    public FileStatus getFileStatus(Path path){
        return HdfsUtil.getFileStaus(fs,path);
    }

    public long getFileLength(Path file){
        return HdfsUtil.getFileSize(fs,file);
    }

    public long getFileLength(String file){
        return HdfsUtil.getFileSize(fs,file);
    }

    public InputStream createInputStream(Path path){
        return HdfsUtil.createInputStream(fs,path);
    }

    public InputStream createInputStream(String path){
        return createInputStream(new Path(path));
    }

    public boolean move(String src,String dst){
        return HdfsUtil.move(fs,src,dst);
    }

    public boolean deleteFile(String file){
        return HdfsUtil.deleteFile(fs,file);
    }

    public boolean concat(String dst,String ... srcs){
        return HdfsUtil.concat(fs,dst,srcs);
    }

    public void checkTgt(){
        try{
            UserGroupInformation.getLoginUser().checkTGTAndReloginFromKeytab();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }

    private static class Holder{
        private static final SimpleHdfsSingleton INSTANCE = new SimpleHdfsSingleton();
    }

    public static SimpleHdfsSingleton hdfs(){
        return Holder.INSTANCE;
    }
}


















