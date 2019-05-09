package com.wk.interpolation;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

public class DataVar extends AbstractVarPart {

    private final String format;

    public DataVar(String name,String format){
        super(name, StringUtils.EMPTY);
        this.format = format;
    }

    @Override
    public String value(Map<String, Object> bindings) {
        String v = StringUtils.EMPTY;
        Object o = bindings.get(name);
        if(o != null){
            v = DateFormatUtils.format((Date)o,format);
        }
        return setValueIfChnaged(v);
    }
}
