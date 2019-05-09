package com.wk.hdfs.hadoop;

import com.wk.data.DataWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class SimpleHdfsFileWriter implements DataWriter {

    private final SimpleHdfsSingleton fs;
    private String filePath;
    private final OutputStream outputStream;
    private long fileSize;

    public SimpleHdfsFileWriter(String filePath){
        this(filePath,true);
    }
    public SimpleHdfsFileWriter(String filePath,boolean overwrite){
        this(new Path(filePath),overwrite);
        this.filePath = filePath;
    }

    private SimpleHdfsFileWriter(Path path,boolean overwrite){
        this(path,overwrite,false);
    }

    public SimpleHdfsFileWriter(String filePath,boolean overwrite,boolean append){
        this(new Path(filePath),overwrite,append);
    }

    public SimpleHdfsFileWriter(Path path,boolean overwrite,boolean append){
        this.fs = SimpleHdfsSingleton.hdfs();
        this.outputStream = fs.createCompressionOutputStream(path,overwrite,append);
        this.fileSize = fs.getFileLength(path);
    }

    public void writeline(String message){
        String line = (StringUtils.isEmpty(message)?"":message)+"\n";
        writeline(line);
    }

    public void writeStirng(String line){
        try{
            byte[] bytes = line.getBytes("UTF-8");
            writeBytes(bytes);
        }catch (UnsupportedEncodingException e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public void writeBytes(byte[] data) {
        write(data,0,data.length);
    }

    @Override
    public void write(byte[] data, int offet, int length) {
        try {
            outputStream.write(data,offet,length);
            fileSize += length;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        }catch (IOException e){
            throw e;
        }
    }

    public long getFileSize(){
        return fileSize;
    }

    public String getFilePath(){
        return filePath;
    }

    public static DataWriter open4Appending(String filePath,boolean overwrite){
        return new SimpleHdfsFileWriter(new Path(filePath),overwrite,true);
    }
}
