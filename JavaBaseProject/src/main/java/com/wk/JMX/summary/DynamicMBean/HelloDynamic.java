package com.wk.JMX.summary.DynamicMBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.reflect.Constructor;

/**
 *  动态MBean 示例
 */
public class HelloDynamic implements DynamicMBean{
    private static Logger log = LoggerFactory.getLogger(HelloDynamic.class);
    // attributes
    private String name;
    private MBeanInfo mBeanInfo = null;
    private String className;
    private String description;

    private MBeanAttributeInfo[] attributes;
    private MBeanConstructorInfo[] constructors;
    private MBeanOperationInfo[] operations;

    MBeanNotificationInfo[]  notificationInfos;

    public HelloDynamic(){
        init();
        buildDynamicMBean();
    }

    /**
     *  初始化一些 attributes
     */
    private void init(){
        // 记录类名 和 初始化介绍
        className = this.getClass().getName();
        description = "Simple implementation of a MBean";

        // init attributes
        attributes = new MBeanAttributeInfo[1];
        constructors = new MBeanConstructorInfo[1];
        operations = new MBeanOperationInfo[1];

        notificationInfos = new MBeanNotificationInfo[0];
    }

    /**
     *  创建一个动态MBean
     */
    private void buildDynamicMBean(){
        // 获取构造器
        Constructor[] helloMBeanConstructors = this.getClass().getConstructors();

        // create constructor
        constructors[0] = new MBeanConstructorInfo("HelloDynamic():Constructor a helloDynamic object",
                helloMBeanConstructors[0]);

        // create attribute
        attributes[0] = new MBeanAttributeInfo("name",String.class.getName(),"HelloDynamicMBean name attribute",
                                true,true,false);
        // create operations
        // no paramter
        MBeanParameterInfo[] params = null;
        operations[0] = new MBeanOperationInfo("print","print() a name",params,
                                "void",MBeanOperationInfo.INFO);

        mBeanInfo = new MBeanInfo(className, description, attributes, constructors, operations, notificationInfos);
    }

    @Override
    public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (attribute == null){
            log.info("getAttribute ,but given attribute is null");
            return null;
        }
        log.info("getAttribute, the attribute name is :" + attribute);
        if (attribute.equals("name")){
            return name;
        }
        return null;
    }

    /**
     *  设置属性值
     * @param attribute
     * @throws AttributeNotFoundException
     * @throws InvalidAttributeValueException
     * @throws MBeanException
     * @throws ReflectionException
     */
    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if (attribute == null){
            log.info("setAttribute ,but given attribute is null");
            return;
        }
        String temp = attribute.getName();
        Object value = attribute.getValue();
        log.info("setAttribute,name is:"+name +", the value is :"+value.toString());
        try{
            if (temp.equals("name")){
                if (value == null){
                    log.info("the given value is null");
                    this.name = null;
                }else if ((Class.forName(String.class.getName()).isAssignableFrom(value.getClass()))){
                    log.info("setAttribute, is set ot not");
                    this.name = (String)value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        if (attributes == null){
            log.info("getAttributes ,but given attributes is null");
            return null;
        }
        AttributeList resultList = new AttributeList();
        // 如果参数的是空的，则返回空链表
        if (attributes.length == 0){
            return resultList;
        }
        for (int i = 0; i < attributes.length; i++) {
            try{
                Object value = getAttribute(attributes[i]);
                resultList.add(new Attribute(attributes[i],value));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return resultList;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        if (attributes == null){
            log.info("setAttributes ,but given attributes is null");
            return null;
        }
        AttributeList resultList = new AttributeList();
        if (attributes.isEmpty()){
            return resultList;
        }

        attributes.forEach(attr -> {
            Attribute attr1 = (Attribute) attr;
            try {
                setAttribute(attr1);
                String name = attr1.getName();
                Object value = attr1.getValue();

                resultList.add(new Attribute(name,value));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return resultList;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        // 真正实现应该使用反射,这是相当于是取巧方式
        // 不过本例只是用于示例展示，就可以了
        if (actionName.equals("print")){
            try {
                log.info("Hello ,"+name + ", this is HelloDynamic print function");
            } catch (Exception e) {
                e.printStackTrace();
            }

            dynamicAddOperation();
            return null;
        }else if (actionName.equals("print1")){
            log.info("dynamicMBean add a print1 method");
            return null;
        }else {
            throw new ReflectionException(new NoSuchMethodException(actionName),
                    "Cannot find the operation "+actionName + " in class "+className);
        }
    }

    /**
     *  在添加一个方法
     */
    private void dynamicAddOperation() {
        init();
        operations = new MBeanOperationInfo[2];
        buildDynamicMBean();
        operations[1] = new MBeanOperationInfo("print1","add print1 method",
                null,"void",MBeanOperationInfo.INFO);
        mBeanInfo = new MBeanInfo(className,description,attributes,constructors,
                operations,notificationInfos);
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return mBeanInfo;
    }
}
