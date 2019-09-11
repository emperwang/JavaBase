package com.wk.JMX.ch7_modelMBean;

import com.wk.JMX.ch3.util.RMIClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.modelmbean.ModelMBeanInfo;
import java.io.IOException;
import java.io.Serializable;

public class ModelClass implements Serializable{
    private static Logger log = LoggerFactory.getLogger(ModelClass.class);
    private String attribute = "My Attribute";
    private static ModelMBeanInfoBuilder builder = ModelMBeanInfoBuilder.getInstalce();
    public String getMyAttribute(){
        log.info("Returning attribute to MBean");
        return attribute;
    }

    public void printAttribute(){
        log.info("print attribute:"+attribute);
    }

    public static ModelMBeanInfo createModelMBean(){
        // 创建ModelMBean
        Descriptor attributeDesc = builder.buildAttributeDescriptor("attribute", null, "always",
                "10",null, "getAttribute", null, "10");

        builder.addModelMBeanAttribute("attribute","java.lang.String",true,false,false,"",attributeDesc);

        Descriptor operationDescriptor = builder.buildOperationDescriptor("getMyAttribute", null, "getter",
                null, null,"com.wk.JMX.ch7_modelMBean.ModelClass", "10");
        builder.addModelMBeanMethod("getMyAttribute",null,null,null,"",
                "java.lang.String", MBeanOperationInfo.INFO,operationDescriptor);

        Descriptor printOperation = builder.buildOperationDescriptor("printAttribute", null, "operation",
                null, null,"com.wk.JMX.ch7_modelMBean.ModelClass", "10");
        builder.addModelMBeanMethod("printAttribute",null,null,null,
                "","void",MBeanOperationInfo.ACTION,printOperation);

        Descriptor mbeanDesc = builder.buildMBeanDescriptor("modeledClass", "", "always", "10",
                ".", "ModeledClass", null, null);

        ModelMBeanInfo modelMBeanInfo = builder.buildModelMBeanInfo(mbeanDesc);
        return modelMBeanInfo;
    }

    public static ModelMBeanInfoBuilder getBuilder() {
        return builder;
    }

    /**
     * jmx in action示例使用的代码,原理是:先注册进入,然后利用反射,把信息注入到bean中
     *          运行出错
     * @param args
     */
    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, ReflectionException,
                                        MBeanException, InstanceAlreadyExistsException, IOException, InstanceNotFoundException {
        ModelClass modelClass = new ModelClass();

        ModelMBeanInfo modelMBeanInfo = createModelMBean();

        MBeanServerConnection connection = RMIClientFactory.getConnection();
        ObjectName objectName = new ObjectName("JMXBookAgent:name=Modeled");
        connection.createMBean("javax.management.modelmbean.RequiredModelMBean",objectName);


        String[] sig = {"java.lang.Object","java.lang.String"};
        Object[] params = { modelClass,"ObjectReference" };

        /**
         *  在这里调用方法 注入时  报错；
         *  javax.management.ServiceNotFoundException: Operation setManagedResource not in ModelMBeanInfo
         *
         *  这里调用方式不对 ~~~ ????
         */
        // connection.invoke(objectName,"setManagedResource",params,sig);
        sig = new String[1];
        sig[0] = "javax.management.modelmbean.ModelMBeanInfo";
        params = new Object[1];
        params[0] = modelMBeanInfo;
        /**
         * javax.management.ServiceNotFoundException: Operation setModelMBeanInfo not in ModelMBeanInfo
         */
        connection.invoke(objectName,"setModelMBeanInfo",params,sig);

        // connection.invoke(objectName,"store",null,null);
    }
}
