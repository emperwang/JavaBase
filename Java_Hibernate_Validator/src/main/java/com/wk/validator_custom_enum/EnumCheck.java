package com.wk.validator_custom_enum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValidator.class)
public @interface EnumCheck {
    /**
     *  必须的属性
     *  显示 校验信息
     *  利用 {} 获取属性值
     * @return
     */
    String message() default "must be man or women";

    /**
     *  必须的属性
     *  用于分组校验
     * @return
     */
    Class<?>[] groups() default {};
    // 必须的属性
    Class<? extends Payload>[] payload() default {};

    String[] enumValue() default {"",""};
}
