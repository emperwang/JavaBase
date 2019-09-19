package com.wk.validator_custom_enum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<EnumCheck,Object> {

    String[] values = null;
    @Override
    public void initialize(EnumCheck check) {
        values = check.enumValue();
        System.out.println(Arrays.asList(values).toString());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        System.out.println("input value is :"+value);
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(value))
                return true;
        }
        return false;
    }
}
