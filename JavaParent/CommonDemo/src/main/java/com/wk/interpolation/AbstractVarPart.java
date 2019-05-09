package com.wk.interpolation;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractVarPart implements VarPart {
    private static final char C = '$';
    private static final AtomicInteger id = new AtomicInteger(0);

    /**
     * combining key, must be unique
     */
    protected String key;
    /**
     * binding name,may be repetitive
     */
    protected String name;

    /**
     * binding value
     */
    protected String value;
    protected boolean changed;

    AbstractVarPart(String name,String value){
        this.key = generateKey();
        this.name = name;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String setValueIfChnaged(String v){
        if(StringUtils.equals(value,v)){
            changed = false;
        }else {
            value = v;
            changed = true;
        }
        return value;
    }

    /**
     *  generate key
     * @return
     */
    private static String generateKey(){
        return C+Integer.toString(id.incrementAndGet());
    }
}
