package com.wk.config;

import com.wk.web.service.UserService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@Configuration
/**
 * ComponentScans ：可以包含多个componentScan
 * ComponentScan ：可以写多次，进行扫描的配置
 */
/*@ComponentScans(value = {
        @ComponentScan(basePackages ={"com.wk"})
})*/
//@ComponentScan(basePackages ={"com.wk.web"})
//配置扫描时，去除某些类
/*@ComponentScan(basePackages = {"com.wk.web"},excludeFilters = {
        @ComponentScan.Filter(classes = {Service.class, Repository.class})
})*/
//只扫描某些类
//默认扫描规则是扫描所有，useDefaultFilters此配置项表示不使用默认配置
@ComponentScan(basePackages = {"com.wk.web"},includeFilters = {
    //@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class}), //扫描包含controller注解的类
   // @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = {UserService.class}) //把userServer类加载到容器中
   @ComponentScan.Filter(type = FilterType.CUSTOM,classes = {MyFilterConfig.class})   //自定义规则
},useDefaultFilters = false)
// FilterType.ANNOTATION: 注解过滤
// FilterType.ASSIGNABLE_TYPE 按照给定的类型
// FilterType.ASPECTJ
// FilterType.REGEX 正则表达式
// FilterType.CUSTOM  :自定义规则
public class ConfigScan {
}
