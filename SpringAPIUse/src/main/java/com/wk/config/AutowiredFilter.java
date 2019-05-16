package com.wk.config;

import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

public class AutowiredFilter implements TypeFilter{
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //业务逻辑:名字是AutoWired的注册到容器中
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        String className = classMetadata.getClassName();
        //System.out.println(className);
        if (className.toLowerCase().contains("autowired")){
            return true;
        }
        return false;
    }
}
