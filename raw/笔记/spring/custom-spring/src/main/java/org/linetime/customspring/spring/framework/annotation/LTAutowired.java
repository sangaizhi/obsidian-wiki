package org.linetime.customspring.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * 自动注入
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LTAutowired {

    String value() default "";
}
