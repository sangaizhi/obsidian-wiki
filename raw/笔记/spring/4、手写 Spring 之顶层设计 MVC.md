## MVC 九大组件
 
#### 1、MultipartResolver  
    多文件上传组件
#### 2、LocaleResolver  
    本地语言环境
#### 3、ThemeResolver  
    主题模板处理器
#### 4、HandlerMapping  
    保存 Url 映射关系
#### 5、HandlerAdapter 
    动态参数适配器
#### 6、HandlerExceptionResolver  
    异常拦截器
#### 7、RequestToViewNameTranslator 
    视图提取器，从Request 中获取 viewName
#### 8、ViewResolvers  
    视图转换器，模板引擎
#### 9、FlashMapManager   
    参数缓存器

## Spring MVC 核心组件执行流程
 HandlerMapping    ------>    HandlerAdapter    ------>    ModelAndView    ------>    ViewResolver    ------>    View