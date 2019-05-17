此为体验servlet注解开发的一个demo。servlet不需要配置web.xml文件，使用@WebServlet等相关注解就可以。

servlet3.0插件能力：

1. servlet容器启动会扫描，当前应用里面每一个jar包的ServletContainerInitializer实现

2. 实现类必须绑定在META-INF/service下的javax.servlet.ServletContainerInitializer文件里面

   META-INF/services/javax.servlet.ServletContainerInitializer

   文件的内容指定了ServletContainerInitializer实现类。