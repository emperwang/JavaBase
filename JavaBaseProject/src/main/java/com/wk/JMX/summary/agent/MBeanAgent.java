package com.wk.JMX.summary.agent;

import com.sun.jdmk.comm.HtmlAdaptorServer;
import com.wk.JMX.summary.DynamicMBean.HelloDynamic;
import com.wk.JMX.summary.DynamicMBean.HelloDynamicWithSupport;
import com.wk.JMX.summary.modelMbean.HelloModel;
import com.wk.JMX.summary.notification.SayHello;
import com.wk.JMX.summary.notification.SayHelloListener;
import com.wk.JMX.summary.standardMbean.Hello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.modelmbean.RequiredModelMBean;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.Set;

public class MBeanAgent {
    private static Logger log = LoggerFactory.getLogger(MBeanAgent.class);
    private MBeanServer server = null;
    // rmi server 注册的url地址
    private String RMI_URL = "service:jmx:rmi:///jndi/rmi://localhost:8888/server";

/********************** 代理层(Agent Level) ************************************/
    /**
     *  创建server的第一种方式
     */
    public void createServerOne(){
        this.server = MBeanServerFactory.createMBeanServer();
    }

    /**
     *  创建server的第二种方式
     */
    public void createServerTwo(){
        this.server = ManagementFactory.getPlatformMBeanServer();
    }
/********************** MBean(Instrumentation Level,俗称设备层) ************************************/
    /**
     *  注册Hello
     */
    public void registerStandardMBean(){
        try {
            ObjectName helloBeanName = new ObjectName("jmxAgent:name=hello");
            Hello hello = new Hello();
            server.registerMBean(hello,helloBeanName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  注册一个Dynamic MBean
     */
    public void registerDynamicBean(){
        try{
            ObjectName dynamicName = new ObjectName("jmxAgent:name=dynamicMbean");
            HelloDynamic helloDynamic = new HelloDynamic();

            server.registerMBean(helloDynamic,dynamicName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  注册使用DynamicMBeanSupport构造的dynamic MBean
     */
    public void registerDynamicCreateWithSupport(){
        try{
            ObjectName dynamicName = new ObjectName("jmxAgent:name=dynamicSupportMbean");
            HelloDynamicWithSupport helloDynamicWithSupport = new HelloDynamicWithSupport();
            server.registerMBean(helloDynamicWithSupport,dynamicName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  注册一个ModelMbean
     */
    public void registerModelMBean(){
        RequiredModelMBean helloModelInfo = HelloModel.createHelloModelInfo();
        try{
            ObjectName modelName = new ObjectName("jmxAgent:name=HelloModelMBean");

            server.registerMBean(helloModelInfo,modelName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  注册监听器 相关的 MBean
     */
    public void registerNotification(){
        SayHello sayHello = new SayHello();
        try{
            ObjectName notificationName = new ObjectName("jmxAgent:name=SayHelloNotification");
            server.registerMBean(sayHello,notificationName);

            server.addNotificationListener(notificationName,new SayHelloListener(),null,new Hello());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  MBean 查找
     * @param beanName
     */
    public void queryMBean(ObjectName beanName){
        // 此查找为: 属性 name 等于 张三
        QueryExp query = Query.eq(Query.attr("name"),Query.value("zhangsan"));
        Set<ObjectInstance> instances = server.queryMBeans(beanName, null);

    }
/********************** Adapter  or  connector(Distributed Service Level) ************************************/
    /**
     * 注册html适配器
     */
    public void registerHtmlAdapter(){
        // create adaptor
        HtmlAdaptorServer htmlAdaptorServer = new HtmlAdaptorServer();
        try {
            // create objectName
            ObjectName htmlName = new ObjectName("jmxAgent:name=htmladaptor,port=8980");

            // register MBean
            server.registerMBean(htmlAdaptorServer,htmlName);

            htmlAdaptorServer.setPort(8980);

            htmlAdaptorServer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  注册 RMI server端
     */
    public void registerRMIConnectorServer(){
        try {
            // 注册一个 端口, 绑定一个URL,客户端就可以通过rmi进行连接了
            LocateRegistry.createRegistry(8888);
            // 创建url
            JMXServiceURL serviceURL = new JMXServiceURL(RMI_URL);

            JMXConnectorServer jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(serviceURL, null, server);

            log.info("before jmx server start");
            jmxConnectorServer.start();

            log.info("jmx server started");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
