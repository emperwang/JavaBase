package com.wk.JMX.ch5_Dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

public class DynamicMBeanSupport implements DynamicMBean {
    private static Logger log = LoggerFactory.getLogger(DynamicMBeanSupport.class);
    protected MBeanInfo mBeanInfo = null;
    protected Hashtable attributes = new Hashtable();
    protected Hashtable notifications = new Hashtable();
    protected Hashtable constructors = new Hashtable();
    protected Hashtable operations = new Hashtable();

    protected String description = "Description of the MBean";

    public DynamicMBeanSupport(){
        addMBeanAttribute("description","java.lang.String",true,true,
                false,"Description of the MBean");
        addMBeanConstructor(this.getClass().getConstructors()[0],"Default Constructor");
    }

    @Override
    public Object getAttribute(String name) throws AttributeNotFoundException, MBeanException, ReflectionException {
        Class<? extends DynamicMBeanSupport> c = this.getClass();
        try {
            Method method = c.getDeclaredMethod("get" + name, null);
            Object result = method.invoke((Object) this, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        String name = attribute.getName();
        Object value = attribute.getValue();
        Class<? extends DynamicMBeanSupport> c = this.getClass();
        String type = getType(name, false, true);
        if (type == null)
            throw new AttributeNotFoundException(name);

        try {
            Class[] types = {Class.forName(type)};
            Method method = c.getDeclaredMethod("set" + name, types);
            Object[] args = {value};
            method.invoke((Object)this,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getType(String attrName,boolean read,boolean write){
        boolean allowed = true;

        if (attributes.containsKey(attrName)){
            MBeanAttributeInfo temp = (MBeanAttributeInfo) attributes.get(attrName);
            if (read){
                if (! temp.isReadable())
                    allowed = false;
            }
            if (write){
                if ( !temp.isWritable()){
                    allowed = false;
                }
            }
            if (!allowed){
                return null;
            }else{
                String type = temp.getType();
                log.info("type is:"+type);
                return type;
            }
        }
        return null;
    }

    @Override
    public AttributeList getAttributes(String[] names) {
        AttributeList list = new AttributeList();
        try{
            for (int i = 0; i < names.length; i++) {
                list.add(new Attribute(names[i],this.getAttribute(names[i])));
            }
        } catch (Exception e){
            e.getStackTrace();
        }
        return list;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        Attribute[] attrs = (Attribute[]) attributes.toArray();
        try {
            for (int i = 0; i < attrs.length; i++) {
                Attribute attribute = attrs[i];
                this.setAttribute(attribute);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attributes;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        Class<? extends DynamicMBeanSupport> c = this.getClass();
        Class sig[] = null;
        try {
            if (signature != null) {
                sig = new Class[signature.length];
                for (int i = 0; i < signature.length; i++) {
                    sig[i] = Class.forName(signature[i]);
                }
            }
            Method method = c.getDeclaredMethod(actionName, sig);
            Object result = method.invoke(this, params);
            return result;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        try {
            buildDynamicMBeanInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBeanInfo;
    }

    protected void addMBeanOperation(String name,String[] paramTypes,String[] paramNames,
                    String[] paramDescs,String desc,String rtype,int type){
        MBeanParameterInfo[] params = null;
        if (paramTypes != null){
            params = new MBeanParameterInfo[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                params[i] = new MBeanParameterInfo(paramNames[i],paramTypes[i],paramDescs[i]);
            }
        }

        operations.put(name,new MBeanOperationInfo(name,desc,params,rtype,type));
    }

    protected void addMBeanConstructor(Constructor constructor,String desc){
        this.constructors.put(constructor,new MBeanConstructorInfo(desc,constructor));
    }

    protected void addMBeanAttribute(String name,String type,boolean read,boolean write,
                                     boolean is,String desc){
        attributes.put(name,new MBeanAttributeInfo(name,type,desc,read,write,is));
    }

    protected void buildDynamicMBeanInfo() throws Exception{
        MBeanOperationInfo[] ops = new MBeanOperationInfo[operations.size()];
        copyInfo(ops,operations);

        MBeanAttributeInfo[] attrs = new MBeanAttributeInfo[attributes.size()];
        copyInfo(attrs,attributes);

        MBeanConstructorInfo[] constructorInfos = new MBeanConstructorInfo[constructors.size()];
        copyInfo(constructorInfos,constructors);

        mBeanInfo = new MBeanInfo(this.getClass().getName(),description,attrs,constructorInfos,
                ops,null);

    }

    private void copyInfo(Object[] array,Hashtable table){
        Vector temp = new Vector(table.values());

        temp.copyInto(array);
    }
}
