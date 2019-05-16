package com.wk.config;

import com.wk.ImportSelect.MyImportBeanDefinitionRegistrar;
import com.wk.ImportSelect.MyImportSelector;
import com.wk.beans.Color;
import com.wk.beans.Red;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
/**
 * ImportSelector  选择注入哪些类，返回值是类的全类名
 * ImportBeanDefinitionRegistrar  手动注册bean，名字可以自己指定
 */
@Import(value = {Color.class, Red.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
public class ImportSelectConfig {
}
