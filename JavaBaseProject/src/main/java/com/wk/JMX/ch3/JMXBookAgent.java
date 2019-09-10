package com.wk.JMX.ch3;

import com.sun.jdmk.comm.HtmlAdaptorServer;
import com.sun.jdmk.comm.RmiConnectorServer;
import com.wk.JMX.ch2.HelloWorld;
import com.wk.JMX.ch3.util.ExceptionUtil;
import com.wk.JMX.ch7_modelMBean.ModelClass;
import com.wk.JMX.ch7_modelMBean.ModelMBeanInfoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class JMXBookAgent {
    private static final Logger log = LoggerFactory.getLogger(JMXBookAgent.class);
    private MBeanServer server = null;

    public JMXBookAgent(){
        log.info("create the MBeanServer");
        server = MBeanServerFactory.createMBeanServer("JMXBookAgent");
        HelloWorld hw = new HelloWorld();
        ObjectName helloWorld = null;

        try {
            helloWorld = new ObjectName("JMXBookAgent:name=helloWorld");
            server.registerMBean(hw,helloWorld);
        } catch (Exception e) {
            ExceptionUtil.printException(e);
        }

        startHTMLAdaptor();
        startRMIConnector();
        addModelMBean();
    }

    protected void startRMIConnector() {
        try {
            LocateRegistry.createRegistry(2099);
            JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:2099/server");
            JMXConnectorServer jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, server);
            jmxConnectorServer.start();
            log.info("jmxConnectorServer started");
        } catch (RemoteException e) {
            ExceptionUtil.printException(e);
        } catch (MalformedURLException e) {
            ExceptionUtil.printException(e);
        } catch (IOException e) {
            ExceptionUtil.printException(e);
        }
    }

    protected void startRMIConnector_bok() {
        RmiConnectorServer rmiConnector = new RmiConnectorServer();
        ObjectName connectorName = null;

        rmiConnector.setPort(2099);
        try {
            connectorName = new ObjectName("JMXBookAgent:name=RMIConnector");

            server.registerMBean(rmiConnector,connectorName);

            rmiConnector.start();

        } catch (Exception e) {
            ExceptionUtil.printException(e);
        }
    }

    protected void startHTMLAdaptor() {
        HtmlAdaptorServer adaptor = new HtmlAdaptorServer();
        adaptor.setPort(9092);
        ObjectName adatorName = null;
        try {
            adatorName = new ObjectName("JMXBookAgent:name=html,port=9092");

            server.registerMBean(adaptor,adatorName);

            adaptor.start();

        } catch (Exception e) {
            ExceptionUtil.printException(e);
        }
    }

    /**
     *  ch7 代码进行修改后，注册到server中
     */
    protected void addModelMBean(){
        ModelMBeanInfo modelMBean = ModelClass.createModelMBean();
        ModelMBeanInfoBuilder builder = ModelClass.getBuilder();
        RequiredModelMBean requiredModelMBean = builder.createModelMBean(new ModelClass(), modelMBean);
        ObjectName objectName = null;
        try {
            objectName = new ObjectName("JMXBookAgent:name=Modeled");
            server.registerMBean(requiredModelMBean,objectName);

        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        log.info(">> start of JMXBook Agent");
        JMXBookAgent jmxBookAgent = new JMXBookAgent();

        log.info("Agent is Ready ro service....");
    }
}
