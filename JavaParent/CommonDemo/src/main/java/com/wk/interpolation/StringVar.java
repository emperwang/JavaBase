package com.wk.interpolation;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class StringVar extends AbstractVarPart {

    public StringVar(String name){
        super(name, StringUtils.EMPTY);
    }

    @Override
    public String value(Map<String, Object> bindings) {
        Object o = bindings.get(name);
        String v = o == null ? StringUtils.EMPTY : o.toString();
        return setValueIfChnaged(v);
    }
}
