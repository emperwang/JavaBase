package com.wk.resource;

import com.wk.loader.ClassLoaderUtil;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class ResourceUtil {

    /**
     * 获取加载文件的输出流
     * @param location
     * @return
     */
    public static InputStream getStream(String location){
        InputStream resourceAsStream = ClassLoaderUtil.getClassLoader().getResourceAsStream(location);
        return resourceAsStream;
    }

    /**
     *  把一个文件的输入流 转换为字符串
     * @param location
     * @param charset
     * @return
     */
    public static String getString(String location,String charset){
        try{
            String s = IOUtils.toString(getStream(location), charset);
            return s;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 把文件输入流转换为UTF-8格式的字符串
     * @param location
     * @return
     */
    public static String getUTF8String(String location){
        try {
           return IOUtils.toString(getStream(location),"UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 把输入文件转换为字节数组
     * @param location
     * @return
     */
    public static byte[] getByteArray(String location){
        try{
            return  IOUtils.toByteArray(getStream(location));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 把输入文件转换为字符流
     * @param location
     * @param charset
     * @return
     */
    public static Reader getReader(String location,String charset){
        try{
            return new InputStreamReader(getStream(location),charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}
