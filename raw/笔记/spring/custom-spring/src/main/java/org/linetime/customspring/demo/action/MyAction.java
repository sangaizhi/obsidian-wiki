package org.linetime.customspring.demo.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.linetime.customspring.demo.service.IModifyService;
import org.linetime.customspring.demo.service.IQueryService;
import org.linetime.customspring.spring.framework.annotation.LTAutowired;
import org.linetime.customspring.spring.framework.annotation.LTController;
import org.linetime.customspring.spring.framework.annotation.LTRequestMapping;
import org.linetime.customspring.spring.framework.annotation.LTRequestParam;
import org.linetime.customspring.spring.framework.webmvc.servlet.LTModelAndView;

/**
 * 公布接口url
 * @author Tom
 *
 */
@LTController
@LTRequestMapping("/web")
public class MyAction {

	@LTAutowired IQueryService queryService;
	@LTAutowired IModifyService modifyService;

	@LTRequestMapping("/query.json")
	public LTModelAndView query(HttpServletRequest request, HttpServletResponse response,
								@LTRequestParam("name") String name){
		String result = queryService.query(name);
		return out(response,result);
	}

	@LTRequestMapping("/add*.json")
	public LTModelAndView add(HttpServletRequest request,HttpServletResponse response,
			   @LTRequestParam("name") String name,@LTRequestParam("addr") String addr){
		String result = modifyService.add(name,addr);
		return out(response,result);

	}

	@LTRequestMapping("/remove.json")
	public LTModelAndView remove(HttpServletRequest request, HttpServletResponse response,
								 @LTRequestParam("id") Integer id){
		String result = modifyService.remove(id);
		return out(response,result);
	}

	@LTRequestMapping("/edit.json")
	public LTModelAndView edit(HttpServletRequest request,HttpServletResponse response,
			@LTRequestParam("id") Integer id,
			@LTRequestParam("name") String name){
		String result = modifyService.edit(id,name);
		return out(response,result);
	}



	private LTModelAndView out(HttpServletResponse resp,String str){
		try {
			resp.getWriter().write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
