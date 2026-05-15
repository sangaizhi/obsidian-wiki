package org.linetime.customspring.spring.framework.beans;

public class LTBeanWrapper {

    private Object wrapperInstance;

    private Class<?> wrapperClass;

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public Class<?> getWrapperClass() {
        return wrapperClass;
    }


    public LTBeanWrapper(Object instance) {
        this.wrapperInstance = instance;
        this.wrapperClass = instance.getClass();
    }


}
