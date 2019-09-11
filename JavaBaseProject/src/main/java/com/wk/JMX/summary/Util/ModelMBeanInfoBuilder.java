package com.wk.JMX.summary.Util;

import javax.management.Descriptor;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.*;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Vector;

/**
 *  创建 ModelMBean 的工具类
 */
public class ModelMBeanInfoBuilder {
    protected Hashtable attributes = new Hashtable();
    protected Hashtable notifications = new Hashtable();
    protected Hashtable constructors = new Hashtable();
    protected Hashtable operations = new Hashtable();

    private static ModelMBeanInfoBuilder builder = new ModelMBeanInfoBuilder();

    /**
     *  创建RequiredModelMBean，被ModelMBean管理的资源，会 RequiredModelMBean 进行包装
     * @param modelBean 被管理的资源
     * @param info  ModelMBeanInfo 表示相关的信息
     * @return
     */
    public RequiredModelMBean createModelMBean(Object modelBean,ModelMBeanInfo info){
        RequiredModelMBean model = null;
        try {
           model = new RequiredModelMBean();
           model.setManagedResource(modelBean,"ObjectReference");
           model.setModelMBeanInfo(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     *  添加 ModelMBeanMethod
     * @param name      方法名字
     * @param paramTypes   参数类型
     * @param paramNames   参数名字
     * @param paramDescs   参数介绍
     * @param description  方法介绍
     * @param rtype    方法返回类型
     * @param type   INFO,ACTION,INFO_ACTION，表示此方法属于哪种类型
     * @param desc   方法的 Descriptor
     */
    public void addModelMBeanMethod(String name, String[] paramTypes, String[] paramNames, String[] paramDescs,
                                    String description, String rtype, int type, Descriptor desc){
        MBeanParameterInfo[] params = null;
        if (paramTypes != null){
            params = new MBeanParameterInfo[paramTypes.length];

            for (int i = 0; i < paramTypes.length; i++) {
                params[i] = new MBeanParameterInfo(paramNames[i],paramTypes[i],paramDescs[i]);
            }
        }
        operations.put(name,new ModelMBeanOperationInfo(name,description,params,rtype,type,desc));
    }

    /**
     *  添加  ModelMBeanNotification
     * @param type      要产生的notification的类型
     * @param className  全限定类名
     * @param description  对此notification 的介绍
     * @param desc
     */
    public void addModelMBeanNotification(String[] type,String className,String description,Descriptor desc){
        notifications.put(className,new ModelMBeanNotificationInfo(type,className,description,desc));
    }

    /**
     *  添加 ModelMbeanAttribute
     * @param name  属性名字
     * @param type  属性类型
     * @param read  是否可读
     * @param write 是否可写
     * @param is    是否是boolean
     * @param description 对此属性的一个介绍
     * @param desc
     */
    public void addModelMBeanAttribute(String name,String type,boolean read,boolean write,boolean is,
                                       String description,Descriptor desc){
        attributes.put(name,new ModelMBeanAttributeInfo(name,type,description,read,write,is,desc));
    }

    /**
     *   添加构造器
     * @param c     构造器
     * @param description   对此构造器的介绍
     * @param desc
     */
    public void addModelMBeanConstructor(Constructor c,String description,Descriptor desc){
        constructors.put(c,new ModelMBeanConstructorInfo(description,c,desc));
    }

    /**
     *  创建 ModelMBeanInfo
     * @param desc
     * @return
     */
    public ModelMBeanInfo buildModelMBeanInfo(Descriptor desc){
        ModelMBeanOperationInfo[] ops = new ModelMBeanOperationInfo[operations.size()];
        copyInfo(ops,operations);

        ModelMBeanAttributeInfo[] attrs = new ModelMBeanAttributeInfo[attributes.size()];
        copyInfo(attrs,attributes);

        ModelMBeanConstructorInfo[] constrs = new ModelMBeanConstructorInfo[constructors.size()];
        copyInfo(constrs,constructors);

        ModelMBeanNotificationInfo[] notifs = new ModelMBeanNotificationInfo[notifications.size()];
        copyInfo(notifs,notifications);

        ModelMBeanInfoSupport modelMBeanInfoSupport = new ModelMBeanInfoSupport("", "description",
                            attrs, constrs, ops, notifs, desc);

        return modelMBeanInfoSupport;
    }

    /**
     *  创建  AttributeDescriptor
     * @param name      属性的名字
     * @param displayName  属性 对外显示的名字
     * @param persistPolicy 持久化策略(也就是进行保存的方法)
     * @param persistPeriod 持久化周期
     * @param defaultValue  默认的值
     * @param getter        属性的getter方法
     * @param setter        属性的 setter方法
     * @param currency
     * @return
     */
    public Descriptor buildAttributeDescriptor(String name, String displayName, String persistPolicy,
                                               String persistPeriod, Object defaultValue, String getter,
                                               String setter, String currency){
        DescriptorSupport desc = new DescriptorSupport();
        if (name != null)
            desc.setField("name",name);
        desc.setField("descriptorType","attribute");
        if (displayName != null)
            desc.setField("displayName",displayName);
        if (getter != null)
            desc.setField("getMethod",getter);
        if (setter != null)
            desc.setField("setMethod",setter);
        if (currency != null)
            desc.setField("currencyTimeLimit",currency);
        if (persistPolicy != null)
            desc.setField("persistPolicy",persistPolicy);
        if (persistPeriod != null)
            desc.setField("persistPeriod",persistPeriod);
        if (defaultValue != null)
            desc.setField("default",defaultValue);

        return desc;
    }

    /**
     *  创建 OperationDescriptor
     * @param name          函数名字
     * @param displayName   函数对外显示的名字
     * @param role          getter  setter operation constructor，表示是get or  set方法或者是其他操作方法
     *                        set 和 get方法分别对应setter  getter，其他打印方法对应 operation，构造方法对应 constructor
     * @param targetObject  目标对象
     * @param targetType    目标对象类型
     * @param ownerClass    所属的类额全限定名
     * @param currency
     * @return
     */
    public Descriptor buildOperationDescriptor(String name,String displayName,String role,Object targetObject,
                                               Object targetType,String ownerClass,String currency){
        DescriptorSupport desc = new DescriptorSupport();
        if (name != null)
            desc.setField("name",name);
        desc.setField("descriptorType","operation");
        if (displayName != null)
            desc.setField("displayName",displayName);
        if (role != null)
            desc.setField("role",role);
        if (targetObject != null)
            desc.setField("targetObject",targetObject);
        if (targetType != null)
            desc.setField("taretType",targetType);
        if (ownerClass != null)
            desc.setField("class",ownerClass);
        if (currency != null)
            desc.setField("currencyTimeLimit",currency);

        return desc;
    }

    /**
     *  创建 MBeanDescription
     * @param name          MBean的全限定名
     * @param displayName   bean对外显示名称
     * @param persistPolicy 持久化策略
     * @param persistPeriod 持久化周期
     * @param persistLocation   持久化位置
     * @param persistName   持久化名字
     * @param log
     * @param logFile       日志记录的文件
     * @return
     */
    public Descriptor buildMBeanDescriptor(String name,String displayName,String persistPolicy,String persistPeriod,
                                           String persistLocation,String persistName,String log,String logFile){
        DescriptorSupport desc = new DescriptorSupport();
        if (name != null)
            desc.setField("name",name);
        desc.setField("descriptorType","mbean");
        if (displayName != null)
            desc.setField("displayName",displayName);
        if (persistLocation != null)
            desc.setField("persistLocation",persistLocation);
        if (persistName != null)
            desc.setField("persistName",persistName);
        if (log != null)
            desc.setField("log",log);
        if (persistPolicy != null)
            desc.setField("persistPolicy",persistPolicy);
        if (persistPeriod != null)
            desc.setField("persistPeriod",persistPeriod);
        if (logFile != null)
            desc.setField("logFile",logFile);

        return desc;
    }

    private void copyInfo(Object[] array,Hashtable table){
        Vector vector = new Vector(table.values());
        vector.copyInto(array);
    }

    public static ModelMBeanInfoBuilder getInstalce(){
        return builder;
    }
}





















