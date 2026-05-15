package org.linetime.customspring.spring.framework.webmvc.servlet;

import org.linetime.customspring.spring.framework.annotation.LTController;
import org.linetime.customspring.spring.framework.annotation.LTRequestMapping;
import org.linetime.customspring.spring.framework.context.LTApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 委派模式
 * 负责任务调度和请求分发
 */
public class LTDispatchServlet extends HttpServlet {
    private LTApplicationContext applicationContext;

    private List<LTHandlerMapping> handlerMappings = new ArrayList<LTHandlerMapping>();

    private Map<LTHandlerMapping, LTHandlerAdapter> handlerAdapterMap = new HashMap();

    private List<LTViewResolver> viewResolvers = new ArrayList<LTViewResolver>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //6、委派,根据URL去找到一个对应的Method并通过response返回
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            try{
                processDispatchResult(req,resp,new LTModelAndView("500"));
            }catch (Exception e1){
                e1.printStackTrace();
            }
            resp.getWriter().write("500 Exception,Detail : " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        // 通过 URL 获取 HandlerMapping
        LTHandlerMapping mappingHandler = this.getHandlerMapping(req);
        if (null == mappingHandler) {
            resp.getWriter().write("404 Not Found!!!");
            this.processDispatchResult(req, resp, new LTModelAndView("404"));
            return;
        }

        // 根据一个 HandlerMapping 获取一个 HandlerAdapter
        LTHandlerAdapter ha = this.getHandlerAdapter(mappingHandler);

        // 使用HandlerAdapter解析某一个方法的形参和返回值之后，统一封装为 ModelAndView 对象
        LTModelAndView mv = ha.handler(req,resp, mappingHandler);

        // 把ModelAndView 变成一个 ViewResolver
        processDispatchResult(req, resp, mv);


    }

    private LTHandlerAdapter getHandlerAdapter(LTHandlerMapping handlerMapping) {
        if(this.handlerAdapterMap.isEmpty()){
            return null;
        }
        return this.handlerAdapterMap.get(handlerMapping);
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, LTModelAndView modelAndView) throws Exception {
        if(null == modelAndView){return;}
        if(this.viewResolvers.isEmpty()){return;}
        for (LTViewResolver viewResolver : this.viewResolvers) {
            LTView v = viewResolver.resolveViewName(modelAndView.getViewName());
            v.render(modelAndView.getModel(), request, response);
            return;
        }
    }


    private LTHandlerMapping getHandlerMapping(HttpServletRequest request) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+", "/");
        for (LTHandlerMapping mapping : this.handlerMappings) {
            Matcher matcher = mapping.getUrl().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return mapping;
        }
        return null;


    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        //初始化Spring核心IoC容器
        // 1、加载读取配置文件，扫描配置文件中配置的需要扫描的包
        // 2、解析配置文件，把扫描到的类封装到 BeanDefinition 中
        // 3、注册 Bean，把 Bean 放入IoC 容器
        // 4、实例化 IoC 中 bean, 并完成依赖注入
        applicationContext = new LTApplicationContext(config.getInitParameter("contextConfigLocation"));

        // 初始化 MVC 的组件
        // 1、初始化 HandlerMapping 组件，完成 Controller 中的 Method 与 Request 的绑定
        // 2、初始化 HandlerAdapter 组件，完成请求参数的绑定
        // 3、初始化 ViewResolver 组件，完成请求结果与返回视图的绑定
        this.initStrategies(applicationContext);


        System.out.println("LT Spring framework is init.");
    }

    /**
     * 初始化 9 大组件
     *
     * @param context
     */
    private void initStrategies(LTApplicationContext context) {

        // 初始化 handlerMapping
        initHandlerMappings(context);
        //初始化参数适配器
        initHandlerAdapters(context);
//        //初始化异常拦截器
//        initHandlerExceptionResolvers(context);
//        //初始化视图预处理器
//        initRequestToViewNameTranslator(context);
        //初始化视图转换器
        initViewResolvers(context);
//        //FlashMap管理器
//        initFlashMapManager(context);
    }

    private void initViewResolvers(LTApplicationContext context) {
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRootPath);
        for (File file : templateRootDir.listFiles()) {
            this.viewResolvers.add(new LTViewResolver(templateRoot));
        }

    }

    private void initHandlerAdapters(LTApplicationContext context) {
        for (LTHandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdapterMap.put(handlerMapping, new LTHandlerAdapter());
        }
    }

    private void initHandlerMappings(LTApplicationContext context) {
        if (context.getBeanDefinitionCount() == 0) {
            return;
        }

        for (String beanName : context.getBeanDefinitionNames()) {
            Object instance =  context.getBean(beanName);
            Class<?> clazz = instance.getClass();
            if (!clazz.isAnnotationPresent(LTController.class)) {
                continue;
            }

            //相当于提取 class上配置的url
            String baseUrl = "";
            if (clazz.isAnnotationPresent(LTRequestMapping.class)) {
                LTRequestMapping requestMapping = clazz.getAnnotation(LTRequestMapping.class);
                baseUrl = requestMapping.value();
            }

            //只获取public的方法
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(LTRequestMapping.class)) {
                    continue;
                }
                //提取每个方法上面配置的url
                LTRequestMapping requestMapping = method.getAnnotation(LTRequestMapping.class);

                // //demo//query
                String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("\\*", ".*").replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(url);
                handlerMappings.add(new LTHandlerMapping(pattern, method, instance));
                System.out.println("Mapped : " + url + "," + method);
            }

        }
    }


    //自己写，自己用
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
//        if(chars[0] > )
        chars[0] += 32;
        return String.valueOf(chars);
    }


}

