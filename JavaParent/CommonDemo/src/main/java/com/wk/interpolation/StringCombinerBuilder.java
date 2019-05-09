package com.wk.interpolation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

@Slf4j
public class StringCombinerBuilder {
    private VarPartFactory factory = VarPartFactory.create();
    private LinkedHashMap<String,VarPart> parts = new LinkedHashMap<>();

    private VarPart last;

    private void addPart(VarPart part){
        last = part;
        parts.put(part.getKey(),part);
    }


    public void addPart(Object context,String name,String format){
        if(StringUtils.isNotEmpty(format)){
            addPart(factory.createDatePart(name,format));
            return;
        }
        String s = evalVariable(context, name);
        if(StringUtils.isNotEmpty(s)){
            addConstantPart(name,s);
            return;
        }
        addStringPart(name);
    }

    private void addConstantPart(String name,String value){
        addConstantPart(factory.createConstantPart(name,value));
    }

    private void addConstantPart(ConstantVar var){
        if(last instanceof ConstantVar){
            ((ConstantVar)last).concat(var.getValue());
        }else {
            addPart(var);
        }
    }

    public void addConstantPart(String value){
        addConstantPart(factory.createConstantPart(value));
    }

    private void addStringPart(String string){
        addPart(factory.createStringPart(string));
    }

    private String evalVariable(Object context,String name){
        Object value = null;
        try{
            value = PropertyUtils.getProperty(context, name);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
        return value != null?value.toString():null;
    }

    public static StringCombinerBuilder create(){
        return new StringCombinerBuilder();
    }

    public StringCombiner build(){
        return StringCombiner.create(parts);
    }

}
