package org.linetime.customspring.spring.framework.webmvc.servlet;

import java.util.Map;

public class LTModelAndView {
    private String viewName;
    private Map<String,?> model;

    public LTModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public LTModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }
}
