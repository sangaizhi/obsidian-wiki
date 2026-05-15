package org.linetime.customspring.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * Request Param
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LTRequestParam {

    String value() default "";

    boolean required() default true;
}
