package com.wk.classLoader;

import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;

import java.io.*;

/**
 *  自定义一个类加载器
 */
public class MyClassLoader extends ClassLoader{
    // 指定类文件路径
    private String path;

    public MyClassLoader(String path){
        this.path = path;
    }

    /**
     *  重写查找类的方法，找到要加载的类
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class toLoaded = null;
        try {
            byte[] date = getDate();
            if (date != null){
                toLoaded = defineClass(name,date,0,date.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toLoaded;
    }

    /**
     *  获取要加载类的字节数组
     * @return
     */
    private byte[] getDate() throws IOException {
        File file = new File(path);
        if (file.exists()){
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int size = 0;
            while ((size = fis.read(buf)) != -1){
                baos.write(buf,0,size);
            }
            fis.close();
            return baos.toByteArray();
        }
        return null;
    }
}
