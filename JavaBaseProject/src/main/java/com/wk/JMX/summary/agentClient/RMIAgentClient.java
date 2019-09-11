package com.wk.JMX.summary.agentClient;

import com.wk.JMX.summary.standardMbean.HelloMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Arrays;
import java.util.Set;

public class RMIAgentClient {
    private static Logger log = LoggerFactory.getLogger(RMIAgentClient.class);

    private static String SERVER_URL="service:jmx:rmi:///jndi/rmi://localhost:8888/server";

    /**
     *  创建一个连接
     * @return
     */
    public static MBeanServerConnection getConnectionToServer(){
        MBeanServerConnection mBeanServerConnection = null;
        try {
            // create URL
            JMXServiceURL serviceURL = new JMXServiceURL(SERVER_URL);
            // 创建一个连接器
            JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceURL, null);
            // 获取连接的id
            String connectionId = jmxConnector.getConnectionId();
            log.info("get connectorId is :" + connectionId);
            // 创建一个连接
            mBeanServerConnection = jmxConnector.getMBeanServerConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBeanServerConnection;
    }

    /**
     *  打印agent的信息
     */
    public static void printAgentInfo(){
        MBeanServerConnection connection = getConnectionToServer();
        try {
            // print domain
            String defaultDomain = connection.getDefaultDomain();
            String[] domains = connection.getDomains();
            log.info("defaultDomain is "+defaultDomain);
            Arrays.asList(domains).forEach(domain -> {
                    log.info("domain is :"+domain);
            });
            // print MBean count
            Integer mBeanCount = connection.getMBeanCount();
            log.info("mBeanCount is "+mBeanCount);
            // 动态set  value
            ObjectName helloBeanName = new ObjectName("jmxAgent:name=hello");
            connection.setAttribute(helloBeanName,new Attribute("Name","agentSetValue"));

            // 代理设置
            HelloMBean hello = (HelloMBean) MBeanServerInvocationHandler.newProxyInstance(connection, helloBeanName,
                                                    HelloMBean.class,false);
            hello.helloWorld();

            // invoke via rmi
            connection.invoke(helloBeanName,"helloWorld",null,null);
            connection.invoke(helloBeanName,"helloWorld",
                    new Object[]{"I'll connect to JMX server via client2"},new String[]{String.class.getName()});

            // get MBeanInfo
            MBeanInfo mBeanInfo = connection.getMBeanInfo(helloBeanName);
            mBeanInfo.getOperations();
            mBeanInfo.getClassName();
            mBeanInfo.getAttributes();
            mBeanInfo.getDescriptor();
            mBeanInfo.getConstructors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有
     */
    public static void QueryMBeanAll(){
        MBeanServerConnection connection = getConnectionToServer();
        // 查询所有
        try {
            Set<ObjectInstance> objectInstances = connection.queryMBeans(null, null);

            objectInstances.forEach(instance->{
                log.info("ClassName is " + instance.getClassName());
                log.info("ObjectName is :" + instance.getObjectName().toString());
                log.info("**************************************************************");
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  查询某一个ObjectName
     */
    public static void QueryMBeanByObjectName(ObjectName objectName){
        MBeanServerConnection connection = getConnectionToServer();
        // 查询所有
        try {
            Set<ObjectInstance> objectInstances = connection.queryMBeans(objectName, null);

            objectInstances.forEach(instance->{
                log.info("ClassName is " + instance.getClassName());
                log.info("ObjectName is :" + instance.getObjectName().toString());
                log.info("**************************************************************");
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  使用 匹配查询
     */
    public static void QueryMBeanByRegx(){
        MBeanServerConnection connection = getConnectionToServer();
        // 查询所有
        try {
            // 获取 HelloMBean 类中的 属性 name的值
            AttributeValueExp name = Query.attr(HelloMBean.class.getName(), "name");
            // 查询 name 的值为  HelloModel
            QueryExp helloModel = Query.eq(name, Query.value("HelloModel"));

            Set<ObjectInstance> objectInstances = connection.queryMBeans(null, helloModel);

            objectInstances.forEach(instance->{
                log.info("ClassName is " + instance.getClassName());
                log.info("ObjectName is :" + instance.getObjectName().toString());
                log.info("**************************************************************");
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws MalformedObjectNameException {
        // QueryMBeanAll();
        // ObjectName objectName = new ObjectName("jmxAgent:name=HelloModelMBean");
        // QueryMBeanByObjectName(objectName);

        QueryMBeanByRegx();
    }
}
