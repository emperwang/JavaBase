[TOC]

# spring的一个使用

	1. 添加依赖
 	2. 使用xml的方式注册bean
 	3. 使用configuration注解类加上bean注解添加bean到容器中
 	4. @componentScan使用
      	1. 配置扫描的包
      	2. includeFilters使用
      	3. excludeFilters使用
      	4. 自定义过滤规则的使用
	5. @ComponScans使用

6. @scope注解使用 

   ​    1) prototype 多实例

   ​	2) singleton 单实例

   ​    3) request 生命周期是一次request

   ​	4) session 生命周期是一次session

7. @lazy注解使用

8. @Condition注解使用

   实现condition接口进行具体的使用.  1, 可以根据环境变量判断 2, 也可以新创建一个bean到容器, 3, 可以从容器中删除一个bean

9. @Import注解使用

   1, 快速导入一个bean到容器中,名字就是全类名

   2, importSelector使用. 通过实现importSelector接口进行具体的业务操作.

   3, importBeanDefinitionRegistrar 注册bean

10.  FactoryBean 创建实例

    1. 创建的class类型是要注册的类
    2. 获取factoryBean的方法 applicationContext.getBean("&beanName")  ,如注册的bean的名字是factoryBean,那么这里就写 "&factoryBean", 那么获得的就是工厂类的实例

11. @Bean注解指定初始化和销毁方法

    1. 单实例的bean容器会帮忙进行销毁
    2. 多实例的bean,容器不会进行销毁操作

12. Bean实现InitializingBean和DisposableBean来定义初始化和销毁逻辑

13. 使用@PostConstruct和@PreDestory实现初始化和销毁方法

14. BeanPostProcesser接口的使用, 分别是在bean初始化之前和之后调用一次

15. @Value注解的使用

    1. 支持直接赋值 @Value("zhangsan")
    2. spEL表达式 @Value("#{20-1}")
    3. ${} 取出环境变量的值 @Value("${os.name}")

16. @PropertySource加载外部配置文件

    1. 此注解可以重复标注

    2. @PropertySources此注解表示使用多个propertySource注解

17. @Autowired注入
	1. 首先按照类型进行注入
	2. 如果有同一个类型有两个bean,那么会再次按照要注入的变量的名字进行查找注入
	3. 使用@Qualifier指定注入哪个bean
	4. @Primary注解,优先注入哪一个bean

       