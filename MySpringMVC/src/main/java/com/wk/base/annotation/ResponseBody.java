package com.wk.base.annotation;
/*
 * @author uv
 * @date 2018/9/30 14:06
 *
 */

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {

}
