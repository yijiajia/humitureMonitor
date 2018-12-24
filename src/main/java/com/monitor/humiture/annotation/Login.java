package com.monitor.humiture.annotation;

import java.lang.annotation.*;

/**
 * 小程序登录效验
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
}
