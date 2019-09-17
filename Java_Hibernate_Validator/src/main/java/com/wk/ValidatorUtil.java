package com.wk;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.parameternameprovider.ParanamerParameterNameProvider;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ValidatorUtil {
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(false)
            .buildValidatorFactory().getValidator();

    private static ExecutableValidator executableValidator = Validation.byProvider(HibernateValidator.class).configure()
            .failFast(false).parameterNameProvider(new ParanamerParameterNameProvider())
            .buildValidatorFactory().getValidator().forExecutables();
    /**
     *  获取 校验器
     * @return
     */
    public static Validator getValidator() {
        return validator;
    }

    public static ExecutableValidator getExecutableValidator() {
        return executableValidator;
    }

    /**
     *  校验bean
     * @param t  要校验的bean
     * @param clazz 分组
     * @param <T>
     * @return 返回出错信息
     */
    public static <T> Map<String,String> validateBean(T t,Class<?>... clazz){
        Set<ConstraintViolation<T>> violations = validator.validate(t, clazz);
        Map<String,String> results = null;
        boolean hasError = violations !=null && violations.size() >0;
        if (hasError){
            results = analysisViolations(violations);
        }
        return results;
    }

    /**
     *  把出错信息 转换为成一个map
     * @param violations 出错信息
     * @param <T>
     * @return
     */
    private static <T> Map<String,String> analysisViolations(Set<ConstraintViolation<T>> violations) {
        HashMap<String, String> result = new HashMap<>();
        violations.forEach(violation -> {
            String propPath = violation.getPropertyPath().toString();
            String msg = violation.getMessage();
            result.put(propPath,msg);
        });
        return result;
    }

    /**
     *  校验属性(property)
     * @param obj  bean
     * @param propertyName  属性名字
     * @param <T>
     * @param clazz  分组
     * @return
     */
    public static <T> Map<String,String> validateProp(T obj,String propertyName,Class<?>... clazz){
        Set<ConstraintViolation<T>> violations = validator.validateProperty(obj, propertyName, clazz);
        Map<String,String> results = null;
        boolean hasError = violations !=null && violations.size() >0;
        if (hasError){
            results = analysisViolations(violations);
        }
        return results;
    }

    /**
     *  校验方法参数
     * @param obj   bean
     * @param method  方法
     * @param parameters 参数
     * @param <T>
     * @return
     */
    public static <T> Map<String,String> validateMethodParameter(T obj, Method method,Object[] parameters){
        Set<ConstraintViolation<T>> violations = executableValidator.validateParameters(obj, method, parameters);
        Map<String,String> results = null;
        boolean hasError = violations !=null && violations.size() >0;
        if (hasError){
            results = analysisViolations(violations);
        }
        return results;
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
