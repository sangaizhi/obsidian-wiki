package org.linetime.customspring.spring.framework.aop.support;

import org.linetime.customspring.spring.framework.aop.aspect.LTAdvice;
import org.linetime.customspring.spring.framework.aop.config.LTAopConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 切面工具类
 */
public class LTAdvisesSupport {

    private LTAopConfig config;

    private Class targetClass;

    private Object target;

    private Pattern pointCutClassPattern;

    private Map<Method, Map<String, LTAdvice>> methodCache;

    public LTAdvisesSupport(LTAopConfig config) {
        this.config = config;
    }

    /**
     * 解析类信息
     */
    private void parse() {


        // 把 Spring 的切面表达式转换成 Java 可识别的正则
        // public .* org.linetime.customspring.demo.service..*Service..*(.*)
        // public .* org\.linetime\.customspring\.demo\.service\..*Service\..*\(.*\)
        String pointCut = config.getPointCut().replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");

        // 切面表达式分成三段
        // 1、 方法的修饰符和返回值
        // 2、方法的类名
        // 3、方法的名称和形参列表

        // 用来匹配类名的正则表达式result = " org\.linetime\.customspring\.demo\.service\..*Service\.\"
        // public .* org\.linetime\.customspring\.demo\.service\..*Service
        String pointCutForClassNameRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClassNameRegex.substring(pointCutForClassNameRegex.lastIndexOf(" ") + 1));

        methodCache = new HashMap<Method, Map<String, LTAdvice>>();

        // 切面表达式的正则匹配
        Pattern pointCutPattern = Pattern.compile(pointCut);

        try {
            Class aspectClass = Class.forName(this.config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<String, Method>();
            for (Method method : aspectClass.getDeclaredMethods()) {
                aspectMethods.put(method.getName(), method);
            }

            for (Method method : this.targetClass.getDeclaredMethods()) {
                String methodString = method.toString(); // public java.lang.String org.linetime.customspring.demo.service.impl.QueryService.query(java.lang.String)
                if (methodString.contains("throws")) {
                    methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pointCutPattern.matcher(methodString);

                if (matcher.matches()) {
                    Map<String, LTAdvice> advices = new HashMap<String, LTAdvice>();
                    if (!(null == config.getAspectBefore() || "".equals(config.getAspectBefore()))) {
                        advices.put("before", new LTAdvice(aspectClass.newInstance(), aspectMethods.get(config.getAspectBefore())));
                    }
                    if (!(null == config.getAspectAfter() || "".equals(config.getAspectAfter()))) {
                        advices.put("after", new LTAdvice(aspectClass.newInstance(), aspectMethods.get(config.getAspectAfter())));
                    }
                    if (!(null == config.getAspectAfterThrow() || "".equals(config.getAspectAfterThrow()))) {
                        LTAdvice advice = new LTAdvice(aspectClass.newInstance(), aspectMethods.get(config.getAspectAfterThrow()));
                        advice.setThrowName(config.getAspectAfterThrowingName());
                        advices.put("afterThrow", advice);
                    }
                    methodCache.put(method, advices);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 根据一个目标代理类方法，获取其对那个的的所有通知
     *
     * @param method
     * @param o
     * @return
     */
    public Map<String, LTAdvice> getAdvice(Method method, Object o) throws Exception {
        Map<String, LTAdvice> cache = methodCache.get(method);
        if (null == cache) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());

            cache = methodCache.get(m);

            this.methodCache.put(m, cache);
        }
        return cache;
    }

    /**
     * 给 ApplicationContext IoC 中对象初始化时条用，决定要不要生成代理类的逻辑
     *
     * @return
     */
    public boolean pointCutMatch() {
        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    public void setTargetClass(Class<?> clazz) {
        this.targetClass = clazz;
        this.parse();
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTarget(Object instance) {
        this.target = instance;
    }

    public Object getTarget() {
        return target;
    }

    private boolean isIgnoreMethod(String method) {
        return false;
    }
}
