package com.wk.validator_custom_enum;

import com.wk.ValidatorUtil;

import javax.validation.groups.Default;
import java.util.Map;

public class MainEntry {
    public static void main(String[] args) {
        ValidateBean validateBean = new ValidateBean();
        validateBean.setSex("hh");

        Map<String, String> map = ValidatorUtil.validateBean(validateBean, Default.class);
        System.out.println(map.toString());
    }
}
