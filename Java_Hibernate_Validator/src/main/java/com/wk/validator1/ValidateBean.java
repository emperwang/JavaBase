package com.wk.validator1;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Iterator;
import java.util.Set;

public class ValidateBean {
    public static void main(String[] args) {
        // 获取校验器
        Validator validator = Validation.byProvider(HibernateValidator.class)
                .configure().failFast(false).buildValidatorFactory().getValidator();

        MyCount myCount = new MyCount();
        myCount.setId("123");
        myCount.setLevel(15);
        myCount.setPassword("#123");
        myCount.setUserName("1234567890111213");

        // 进行校验
        Set<ConstraintViolation<MyCount>> violations = validator.validate(myCount, Default.class);

        // 遍历校验过程中的出现的错误
        Iterator<ConstraintViolation<MyCount>> iterator = violations.iterator();
        while (iterator.hasNext()){
            ConstraintViolation<MyCount> next = iterator.next();
            String propertyPath = next.getPropertyPath().toString();
            String message = next.getMessage();
            System.out.println("propertyPath = "+propertyPath +"; message = "+message);

        }
    }
}
