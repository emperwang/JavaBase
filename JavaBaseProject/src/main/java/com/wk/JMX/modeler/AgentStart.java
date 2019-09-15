package com.wk.JMX.modeler;

import com.sun.jdmk.comm.HtmlAdaptorServer;
import org.apache.commons.modeler.ManagedBean;
import org.apache.commons.modeler.Registry;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBean;
import java.io.InputStream;

public class AgentStart {
    private static String XMLPath="/jmx/mbeans-desc.xml";
    public static void main(String[] args) throws Exception {
        // 基于xml总的信息构建一个Registry
        Registry registry = Registry.getRegistry(null, null);

        InputStream resourceAsStream = AgentStart.class.getResourceAsStream(XMLPath);
        registry.loadMetadata(resourceAsStream);

        MBeanServer mBeanServer = registry.getMBeanServer();

        ManagedBean managedBean = registry.findManagedBean("Hello");

        ObjectName objectName = new ObjectName(managedBean.getName() + ":name=Hello");

        ModelMBean hello = managedBean.createMBean(new Hello());

        mBeanServer.registerMBean(hello,objectName);

        ObjectName htmlAdaptor = new ObjectName("HelloAgent:name=htmladaptor");

        HtmlAdaptorServer adaptor = new HtmlAdaptorServer();

        adaptor.setPort(8980);

        mBeanServer.registerMBean(adaptor,htmlAdaptor);

        adaptor.start();


    }
}
