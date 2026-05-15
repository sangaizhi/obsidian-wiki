package org.linetime.customspring.spring.framework.aop.config;

import lombok.Data;

/**
 * AOP 配置
 */
@Data
public class LTAopConfig {
    /**
     * 切面表达式，定义切入点
     */
    private String pointCut;

    /**
     * 切面类
     */
    private String aspectClass;

    /**
     * 前置切面执行的方法
     */
    private String aspectBefore;

    /**
     * 后置切面执行的方法
     */
    private String aspectAfter;

    /**
     * 异常切面执行的方法
     */
    private String aspectAfterThrow;

    /**
     * 需要执行异常切面的异常
     */
    private String aspectAfterThrowingName;
}
