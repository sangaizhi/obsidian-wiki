package org.linetime.customspring.spring.framework.webmvc.servlet;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class LTHandlerMapping {

    private Pattern url;
    private Method method;
    private Object controller;  // method 对应的实例对象

    public LTHandlerMapping(Pattern url, Method method, Object controller) {
        this.url = url;
        this.method = method;
        this.controller = controller;
    }

    public Pattern getUrl() {
        return url;
    }

    public void setUrl(Pattern url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }
}
