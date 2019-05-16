package com.wk.config;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class MyFilterConfig implements TypeFilter {
    /**
     *
     * @param metadataReader  the metadata reader for the target class,就是读取当前的类
     * @param metadataReaderFactory a factory for obtaining metadata readers 能读取其他的类
     * for other classes (such as superclasses and interfaces
     * @return
     * @throws IOException
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
        String className = metadata.getClassName();
        System.out.println(className);   //获取当前的类名
        if (className.contains("er")){ //如果类型中包含er，那么就注册进容器中
            return true;
        }
        return false;
    }
}
