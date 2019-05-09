package com.wk.interpolation;

public class VarPartFactory {
    public DataVar createDatePart(String name,String format){
        return new DataVar(name,format);
    }

    public StringVar createStringPart(String name){
        return new StringVar(name);
    }

    public ConstantVar createConstantPart(String value){
        return new ConstantVar(value);
    }

    public ConstantVar createConstantPart(String name,String value){
        return new ConstantVar(name,value);
    }
    public static VarPartFactory create() {
        return new VarPartFactory();
    }
}
