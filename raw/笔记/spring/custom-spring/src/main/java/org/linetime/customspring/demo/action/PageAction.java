package org.linetime.customspring.demo.action;


import org.linetime.customspring.demo.service.IQueryService;
import org.linetime.customspring.spring.framework.annotation.LTAutowired;
import org.linetime.customspring.spring.framework.annotation.LTController;
import org.linetime.customspring.spring.framework.annotation.LTRequestMapping;
import org.linetime.customspring.spring.framework.annotation.LTRequestParam;
import org.linetime.customspring.spring.framework.webmvc.servlet.LTModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 公布接口url
 * @author Tom
 *
 */
@LTController
@LTRequestMapping("/")
public class PageAction {

    @LTAutowired
    IQueryService queryService;

    @LTRequestMapping("/first.html")
    public LTModelAndView query(@LTRequestParam("teacher") String teacher){
        String result = queryService.query(teacher);
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("teacher", teacher);
        model.put("data", result);
        model.put("token", "123456");
        return new LTModelAndView("first.html",model);
    }

}
