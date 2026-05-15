package org.linetime.customspring.spring.framework.context;

import org.linetime.customspring.spring.framework.annotation.LTAutowired;
import org.linetime.customspring.spring.framework.annotation.LTController;
import org.linetime.customspring.spring.framework.annotation.LTService;
import org.linetime.customspring.spring.framework.aop.LTJdkDynamicAopProxy;
import org.linetime.customspring.spring.framework.aop.config.LTAopConfig;
import org.linetime.customspring.spring.framework.aop.support.LTAdvisesSupport;
import org.linetime.customspring.spring.framework.beans.LTBeanWrapper;
import org.linetime.customspring.spring.framework.beans.config.LTBeanDefinition;
import org.linetime.customspring.spring.framework.beans.support.LTBeanDefinitionReader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 完成 Bean 的创建和 DI
 */
public class LTApplicationContext {

    /**
     * 配置文件路径
     */
    private String[] configLocations;


    private LTBeanDefinitionReader beanDefinitionReader;

    private Map<String, LTBeanWrapper> factoryBeanInstanceCache = new HashMap<String, LTBeanWrapper>();

    private Map<String, Object> factoryBeanObjectCache = new HashMap<String, Object>();


    private Map<String, LTBeanDefinition> beanDefinitionMap = new HashMap<String, LTBeanDefinition>();

    public LTApplicationContext(String... configLocations) {
        this.configLocations = configLocations;

        // 加载读取配置文件
        beanDefinitionReader = new LTBeanDefinitionReader(configLocations);

        // 解析配置文件，封装成 BeanDefinition
        List<LTBeanDefinition> beanDefinitions = beanDefinitionReader.loadBeanDefinitions();

        try {
            // 把 BeanDefinition 缓存起来
            doRegistBeanDefinition(beanDefinitions);

            doAutowired();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void doAutowired() {
        // 这一步，所有的 Bean 还没有实例化，还只是配置阶段
        for (Map.Entry<String, LTBeanDefinition> ltBeanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            String beanName = ltBeanDefinitionEntry.getKey();
            // 依赖注入
            getBean(beanName);
        }
    }

    private void doRegistBeanDefinition(List<LTBeanDefinition> beanDefinitions) throws Exception {
        for (LTBeanDefinition beanDefinition : beanDefinitions) {
            if (this.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + " is exists");
            }
            beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
            beanDefinitionMap.put(beanDefinition.getBeanClassName(), beanDefinition);
        }
    }

    /**
     * 获取 bean 实例，
     * bean 的实例化、DI 都是从这个方法开始的
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        // 先拿到 BeanDefinition 配置信息
        LTBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);

        // 反射实例化
        Object instance = instantiateBean(beanName, beanDefinition);

        // 封装成 BeanWrapper
        LTBeanWrapper beanWrapper = new LTBeanWrapper(instance);

        // 保存 BeanWrapper 到 IoC 容器
        factoryBeanInstanceCache.put(beanName, beanWrapper);

        // 执行依赖注入
        populateBean(beanName, beanDefinition, beanWrapper);

        return beanWrapper.getWrapperInstance();
    }

    /**
     * 完成依赖注入
     *
     * @param beanName
     * @param beanDefinition
     * @param beanWrapper
     */
    private void populateBean(String beanName, LTBeanDefinition beanDefinition, LTBeanWrapper beanWrapper) {
        // 可能会涉及到循环依赖

        // 用两个缓存，循环两次
        // 把第一次读取结果为空的 BeanDefinition,存到第一个缓存，
        // 等第一次循环之后，等第一次循环再检查第一次的缓存，在进行赋值

        Object instance = beanWrapper.getWrapperInstance();
        // 扫描所有字段
        Class<?> clazz = beanWrapper.getWrapperClass();

        if (!(clazz.isAnnotationPresent(LTController.class)
                || !clazz.isAnnotationPresent(LTService.class))) {
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(LTAutowired.class)) {
                continue;
            }
            LTAutowired autowired = field.getAnnotation(LTAutowired.class);

            // 如果用户没有自定义的 beanName,就默认根据类型注入
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }

            field.setAccessible(true);

            try {
                if (this.factoryBeanInstanceCache.get(autowiredBeanName) == null) {
                    continue;
                }
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 实例化 Bean,创建真正的实例对象
     *
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object instantiateBean(String beanName, LTBeanDefinition beanDefinition) {

        String className = beanDefinition.getBeanClassName();
        Object instance = null;
        try {
            if (this.factoryBeanObjectCache.containsKey(beanName)) {
                instance = this.factoryBeanObjectCache.get(beanName);
            } else {
                Class<?> clazz = Class.forName(className);

                instance = clazz.newInstance();

                // AOP 开始
                // 如果满足条件，直接返回 Proxy 对象
                // 1、加载 AOP 的配置文件
                LTAdvisesSupport config = instantiateAopConfig(beanDefinition);
                // 用来判断目标类是否满足条件
                config.setTargetClass(clazz);
                config.setTarget(instance);

                // 判断规则，要不要生成代理类。如果要就覆盖原生对象；如果不要，就不做任何处理
                if(config.pointCutMatch()){
                    instance = new LTJdkDynamicAopProxy(config).getProxy();
                }

                // AOP 结束

                this.factoryBeanObjectCache.put(beanName, instance);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return instance;
    }

    /**
     * 初始化 AOP， 加载 AOP 的配置文件
     * @param beanDefinition
     * @return
     */
    private LTAdvisesSupport instantiateAopConfig(LTBeanDefinition beanDefinition) {
        LTAopConfig config = new LTAopConfig();
        config.setPointCut(this.beanDefinitionReader.getConfig().getProperty("pointCut"));
        config.setAspectClass(this.beanDefinitionReader.getConfig().getProperty("aspectClass"));
        config.setAspectBefore(this.beanDefinitionReader.getConfig().getProperty("aspectBefore"));
        config.setAspectAfter(this.beanDefinitionReader.getConfig().getProperty("aspectAfter"));
        config.setAspectAfterThrow(this.beanDefinitionReader.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectAfterThrowingName(this.beanDefinitionReader.getConfig().getProperty("aspectAfterThrowingName"));
        return new LTAdvisesSupport(config);
    }

    public Object getBean(Class beanClass) {
        return getBean(beanClass.getName());
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }


    public Properties getConfig(){
        return this.beanDefinitionReader.getConfig();
    }
}
