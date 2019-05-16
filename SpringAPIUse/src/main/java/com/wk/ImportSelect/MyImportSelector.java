package com.wk.ImportSelect;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

// 返回值为全类名，就是需要注册到容器中的类
public class MyImportSelector implements ImportSelector {
    /**
     * @param importingClassMetadata  当前添加了@Import注解的类的全部注解信息
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"com.wk.beans.Blue","com.wk.beans.Yellow"};
    }
}
