package com.wk.config;

import com.wk.beans.Color;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
//通过import快速导入组件,组件名字就是全类名.因为是数组，所以一次可以导入多个类
@Import(value = {Color.class})
public class ImportConfig {
}
