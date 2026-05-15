package org.linetime.customspring.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * controller 注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LTController {
    String value() default  "";
}
