package com.wk.validator1;

import com.wk.ValidatorUtil;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ValidateBean {
    public static void main(String[] args) {
        // baseValidate();
        // validateBean();
        // validateProperty();
        validateMehtod();

    }

    /**
     *  校验bean
     */
    public static void validateBean(){
        MyCount myCount = new MyCount();
        myCount.setId("123");
        myCount.setLevel(15);
        myCount.setPassword("#123");
        myCount.setUserName("1234567890111213");

        Map<String, String> stringStringMap = ValidatorUtil.validateBean(myCount, Default.class);
        System.out.println(stringStringMap.toString());
    }

    /**
     *  校验属性
     */
    public static void validateProperty(){
        MyCount myCount = new MyCount();
        myCount.setId("123");
        myCount.setLevel(15);
        myCount.setPassword("#123");
        myCount.setUserName("1234567890111213");

        Map<String, String> stringMap = ValidatorUtil.validateProp(myCount,"userName",Default.class);
        System.out.println(stringMap);
    }

    /**
     *  校验参数
     */
    public static void validateMehtod() {
        MyCount myCount = new MyCount();
        try {
            Method method = myCount.getClass().getDeclaredMethod("printName", String.class);
            // method.invoke(myCount,"hello");

            Object[] params = {"name"};
            Map<String, String> methodParameter = ValidatorUtil.validateMethodParameter(myCount, method, params);

            System.out.println(methodParameter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  基础校验
     */
    private static void baseValidate() {
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
