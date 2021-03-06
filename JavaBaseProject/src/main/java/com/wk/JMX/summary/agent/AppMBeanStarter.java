package com.wk.JMX.summary.agent;

public class AppMBeanStarter {
    public static void main(String[] args) {
        MBeanAgent agent = new MBeanAgent();
        /********** Agent level *****************************************/
        agent.createServerTwo();

        /*********** Instrumentation level ****************************************/
        agent.registerStandardMBean();
        agent.registerDynamicBean();
        agent.registerDynamicCreateWithSupport();
        agent.registerModelMBean();
        agent.registerNotification();
        /*********** Distribute service level ****************************************/
        agent.registerHtmlAdapter();
        agent.registerRMIConnectorServer();
    }
}
