package com.wk.validator_custom;

import com.wk.ValidatorUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

public class ValidateBean {
    public static void main(String[] args) {
        Bean bean = new Bean();
        bean.setBrithday("2019-09-1711:58:00");

        Validator validator = ValidatorUtil.getvalidator();

        Set<ConstraintViolation<Bean>> violations = validator.validate(bean, Default.class);

        ValidatorUtil.printViolations(violations);

    }
}
