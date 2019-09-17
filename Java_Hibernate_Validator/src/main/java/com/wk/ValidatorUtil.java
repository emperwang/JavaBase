package com.wk;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorUtil {
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(false)
            .buildValidatorFactory().getValidator();

    /**
     *  获取 校验器
     * @return
     */
    public static Validator getvalidator(){
        return validator;
    }

    /**
     *  打印
     * @param violations
     */
    public static <T> void printViolations(Set<ConstraintViolation<T>>violations){
        violations.forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            Object invalidValue = violation.getInvalidValue();
            // Object leafBean = violation.getLeafBean();
            String message = violation.getMessage();
            String messageTemplate = violation.getMessageTemplate();
            // FtpSocketServiceBean rootBean = violation.getRootBean();
            // String className = violation.getRootBeanClass().getName();

            System.out.println("propertyPath = "+ propertyPath);
            System.out.println("invalidValue = "+ invalidValue);
            //System.out.println("leafBean = "+ leafBean.toString());
            System.out.println("message = "+ message);
            System.out.println("messageTemplate = "+ messageTemplate);
            //System.out.println("rootBean = "+ rootBean.toString());
            //System.out.println("className = "+ className);
            System.out.println("********************************************************************");

            //ConstraintDescriptor<?> constraintDescriptor = violation.getConstraintDescriptor();

            /*Object[] executableParameters = violation.getExecutableParameters();
            if (executableParameters != null && executableParameters.length >0) {
                System.out.println(Arrays.asList(executableParameters).toString());
            }*/
            /*Object executableReturnValue = violation.getExecutableReturnValue();
            System.out.println(executableReturnValue.toString());*/
        });
    }
}
