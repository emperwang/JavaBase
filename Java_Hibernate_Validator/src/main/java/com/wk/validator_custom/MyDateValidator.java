package com.wk.validator_custom;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {MyDateValidator.DateValidatorInner.class})
public @interface MyDateValidator {
    /**
     *  必须的属性
     *  显示 校验信息
     *  利用 {} 获取属性值
     * @return
     */
    String message() default "日期格式不匹配{dataFormat}";

    /**
     *  必须的属性
     *  用于分组校验
     * @return
     */
    Class<?>[] groups() default {};
    // 必须的属性
    Class<? extends Payload>[] payload() default {};

    /**
     *  非必需
     *  指定时间格式
     * @return
     */
    String dataFormat() default "yyyy-MM-dd HH:mm:ss";

    /**
     *  内部类  实现具体的校验工作
     */
    class DateValidatorInner implements ConstraintValidator<MyDateValidator,String>{
        private String dataFormat;

        @Override
        public void initialize(MyDateValidator myDateValidator) {
            System.out.println("DateValidatorInner initialize");
            this.dataFormat = myDateValidator.dataFormat();
        }

        /***
         *  具体的校验逻辑
         *
         * @param value  需要校验的值
         * @param context
         * @return  布尔结果值
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            System.out.println("DateValidatorInner valid");
            if (value == null){
                return false;
            }

            if ("".equals(value)){
                return false;
            }

            SimpleDateFormat format = new SimpleDateFormat(dataFormat);

            try{
                Date date = format.parse(value);

                return date != null;
            } catch (ParseException e) {
                return false;
            }
        }
    }
}
