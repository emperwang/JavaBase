package com.wk.interpolation;

import java.util.Map;

public class ConstantVar extends AbstractVarPart {

    public ConstantVar(String value){
        super(null,value);
        this.name = this.key;
    }

    public ConstantVar(String name,String value){
        super(name,value);
    }

    @Override
    public String value(Map<String, Object> bindings) {
        return value;
    }

    public void concat(String s){
        if(value == null){
            value = s;
        }else {
            value += s;
        }
    }
}
