package com.wk.validator_custom_enum;

public enum SexEnum {
    Man("man"),Women("women");

    private String sex;
    SexEnum(String sex){
        this.sex = sex;
    }

    @Override
    public String toString() {
        return this.sex +"_";
    }
}
