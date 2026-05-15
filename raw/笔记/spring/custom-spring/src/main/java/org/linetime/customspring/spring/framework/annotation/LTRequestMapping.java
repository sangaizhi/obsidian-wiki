package org.linetime.customspring.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * Request Mapping 注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LTRequestMapping {
    String value() default "";
}
