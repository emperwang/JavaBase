package com.wk.JMX.summary.modelMbean;

import com.wk.JMX.ch7_modelMBean.ModelMBeanInfoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.Descriptor;
import javax.management.MBeanOperationInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;

/**
 *  ModelCMBean 不需要实现其他接口
 */
public class HelloModel {
    private static Logger log = LoggerFactory.getLogger(HelloModel.class);

    private String name = "HelloModel";

    public HelloModel() {
    }

    public void sayHello(){
        log.info("say hello to name :"+name);
    }

    public void sayHelloToOne(String name){
        log.info("sayHelloToOne, say hello to  :"+name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static RequiredModelMBean createHelloModelInfo(){
        ModelMBeanInfoBuilder builder = ModelMBeanInfoBuilder.getInstalce();
        // create attribute
        Descriptor attributeDescriptor = builder.buildAttributeDescriptor("name", "HelloModel-name", "always",
                "10", "DefaultName", "getName", "setName", "10");
        builder.addModelMBeanAttribute("name",String.class.getName(),true,true,false,
                "this is HelloName descriptor",attributeDescriptor);

        // create setter  and  getter method
        Descriptor setNameDescriptor = builder.buildOperationDescriptor("setName", "setName Operation", "setter",
                null, null, HelloModel.class.getName(), "10");
        builder.addModelMBeanMethod("setName",new String[]{String.class.getName()},new String[]{"name"},new String[]{"set attribute name"},
                "SayHello-Method-Descriptor","void", MBeanOperationInfo.ACTION,setNameDescriptor);

        Descriptor getNameDescriptor = builder.buildOperationDescriptor("getName", "getName Operation", "getter",
                null, null, HelloModel.class.getName(), "10");
        builder.addModelMBeanMethod("getName",null,null,null,"SayHello-Method-Descriptor",
                "void", MBeanOperationInfo.INFO,getNameDescriptor);

        // create operation
        Descriptor opsDescriptor = builder.buildOperationDescriptor("sayHello", "SayHello Operation", "operation",
                null, null, HelloModel.class.getName(), "10");
        builder.addModelMBeanMethod("sayHello",null,null,null,"SayHello-Method-Descriptor",
                "void", MBeanOperationInfo.INFO,opsDescriptor);

        Descriptor ops2Descriptor = builder.buildOperationDescriptor("sayHelloToOne", "sayHelloToOne Operation", "operation",
                null, null, HelloModel.class.getName(), "10");
        builder.addModelMBeanMethod("sayHelloToOne",new String[]{String.class.getName()},new String[]{"name"},
                new String[]{"SayHelloOne Parameter Descriptor"},"SayHelloOne-Method-Descriptor","void",
                MBeanOperationInfo.INFO,ops2Descriptor);

        // create constructor
        Descriptor constructorDesc = builder.buildOperationDescriptor(HelloModel.class.getName(), "Constructor Name", "constructor",
                null, null, HelloModel.class.getName(), "10");
        builder.addModelMBeanConstructor(HelloModel.class.getConstructors()[0],"HelloModel-Constructor-Method-Desc",constructorDesc);

        Descriptor beanDesc = builder.buildMBeanDescriptor(HelloModel.class.getName(), "HelloModelClass", "always", "10",
                ".", "HelloModelPersistname", null, null);
        ModelMBeanInfo modelMBeanInfo = builder.buildModelMBeanInfo(beanDesc);


        HelloModel helloModel = new HelloModel();
        RequiredModelMBean modelMBean = builder.createModelMBean(helloModel, modelMBeanInfo);

        return modelMBean;
    }
}
