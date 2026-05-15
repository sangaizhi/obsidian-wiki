package org.linetime.customspring.spring.framework.aop;

import org.linetime.customspring.spring.framework.aop.aspect.LTAdvice;
import org.linetime.customspring.spring.framework.aop.support.LTAdvisesSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 切面代理类
 */
public class LTJdkDynamicAopProxy implements InvocationHandler {


    private LTAdvisesSupport config;

    public LTJdkDynamicAopProxy(LTAdvisesSupport config) {
        this.config = config;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Map<String, LTAdvice> advices = config.getAdvice(method, null);
        Object returnValue = null;
        try {

            invokeAdvice(advices.get("before"));

            returnValue = method.invoke(this.config.getTarget(), args);

            invokeAdvice(advices.get("after"));

        } catch (Exception e) {

            invokeAdvice(advices.get("afterThrow"));
            throw e;
        }

        return returnValue;
    }

    private void invokeAdvice(LTAdvice advice) {
        try {
            advice.getAdviceMethod().invoke(advice.getAspect());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), this.config.getTargetClass().getInterfaces(), this);
    }
}
