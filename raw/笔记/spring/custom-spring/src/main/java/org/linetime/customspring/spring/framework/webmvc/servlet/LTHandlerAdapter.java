package org.linetime.customspring.spring.framework.webmvc.servlet;

import org.linetime.customspring.spring.framework.annotation.LTRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LTHandlerAdapter {

    public LTModelAndView handler(HttpServletRequest request, HttpServletResponse response, LTHandlerMapping handlerMapping) throws Exception {


        // 保存形参列表，参数名称与位置的映射
        Map<String, Integer> paramIndexMap = new HashMap<String, Integer>();

        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for (Annotation a : pa[i]) {
                if (a instanceof LTRequestParam) {
                    String paramName = ((LTRequestParam) a).value();
                    if (!"".equals(paramName.trim())) {
                        paramIndexMap.put(paramName, i);
                    }
                }
            }
        }

        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> parameterType = paramTypes[i];
            if (parameterType == HttpServletRequest.class || parameterType == HttpServletResponse.class) {
                paramIndexMap.put(parameterType.getName(), i);
            }
        }

        // 拼接实参列表
        Map<String, String[]> params = request.getParameterMap();
        Object[] paramValues = new Object[paramTypes.length];

        for (Map.Entry<String, String[]> paramEntry : params.entrySet()) {
            String value = Arrays.toString(params.get(paramEntry.getKey())).replaceAll("\\[|\\]","")
                    .replaceAll("\\s+",",");
            if(!paramIndexMap.containsKey(paramEntry.getKey())){
                continue;
            }
            int index = paramIndexMap.get(paramEntry.getKey());

            // 允许自定义的类型转换器
            paramValues[index] = castStringValue(value, paramTypes[index]);

        }
        if(paramIndexMap.containsKey(HttpServletRequest.class.getName())){
            int index = paramIndexMap.get(HttpServletRequest.class.getName());
            paramValues[index] = request;
        }
        if(paramIndexMap.containsKey(HttpServletResponse.class.getName())){
            int index = paramIndexMap.get(HttpServletResponse.class.getName());
            paramValues[index] = response;
        }

        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
        if(result == null || result instanceof  Void){
            return null;
        }
        if(handlerMapping.getMethod().getReturnType() == LTModelAndView.class){
            return (LTModelAndView) result;
        }
        return null;


//
////
//        Method method = handlerMapping.getMethod();
//
//
//        //赋值实参列表
//        method.invoke(handlerMapping.getController(), paramValues);
    }

    private Object castStringValue(String value,Class<?> paramType) {
        if(String.class == paramType){
            return value;
        }else if(Integer.class == paramType){
            return Integer.valueOf(value);
        }else if(Double.class == paramType){
            return Double.valueOf(value);
        }else if(value != null){
            return value;
        }
        return null;
    }
}
