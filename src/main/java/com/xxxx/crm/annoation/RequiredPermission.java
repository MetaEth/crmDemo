package com.xxxx.crm.annoation;

import java.lang.annotation.*;

/**
 *自定义注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {
    //权限码
    String code() default "";
}
