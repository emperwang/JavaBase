package com.wk.spi;

import com.wk.spi.iface.IFace;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 1. 创建一个接口 com.wk.spi.iface.IFace
 * 2. 创建此接口的实现类
 * 3. 在resources目录下创建: MEAT-INF/services/com.wk.spi.iface.IFace  文件
 * 4. 在 第3步 创建的文件中 写入com.wk.spi.iface.IFace的实现类的全类名
 */
public class SpiStarter {
    public static void main(String[] args) {
        ServiceLoader<IFace> load = ServiceLoader.load(IFace.class);
        Iterator<IFace> iter = load.iterator();
        while (iter.hasNext()){
            iter.next().printInfo();
        }
    }
}
