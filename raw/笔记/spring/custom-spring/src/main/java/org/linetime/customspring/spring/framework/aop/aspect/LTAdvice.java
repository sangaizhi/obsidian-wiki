package org.linetime.customspring.spring.framework.aop.aspect;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * 切面通知
 */
@Data
public class LTAdvice {

    /**
     * 切面中需要调方法的实例
     */
    private Object aspect;

    /**
     * 切面中需要调用的方法
     */
    private Method adviceMethod;


    private String throwName;

    public LTAdvice(Object aspect, Method adviceMethod) {
        this.aspect = aspect;
        this.adviceMethod = adviceMethod;
    }
}
