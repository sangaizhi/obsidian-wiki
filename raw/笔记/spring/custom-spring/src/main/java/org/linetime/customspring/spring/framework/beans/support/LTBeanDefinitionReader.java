package org.linetime.customspring.spring.framework.beans.support;

import org.linetime.customspring.spring.framework.beans.config.LTBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 读取解析配置文件，并封装成 BeanDefinition(为什么转换成 BeanDefinition,因为在 Spring 中一切皆为 Bean)
 */
public class LTBeanDefinitionReader {


    private Properties contextConfig = new Properties();

    private List<String> registryBeanClasses = new ArrayList<String>();


    public LTBeanDefinitionReader(String... configLocations) {
        // 读取配置文件
        this.doLoadConfig(configLocations[0]);

        // 扫描配置文件中配置的相关的类
        doScanner(contextConfig.getProperty("scanPackage"));

    }

    private void doScanner(String scanPackage) {
        // jar war  zip  rar
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        // 当成是一个 ClassPath 文件夹
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = scanPackage + "." + file.getName().replace(".class", "");
                registryBeanClasses.add(className);
            }

        }


    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation.replaceAll("classpath:", ""));
        try {
            contextConfig.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public List<LTBeanDefinition> loadBeanDefinitions() {
        List<LTBeanDefinition> result = new ArrayList<LTBeanDefinition>();

        try {
            for (String className : registryBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()) {
                    continue;
                }
                // 保存类对应的全类名和 beanName
                // beanName 默认是类名称首字母小写，也可以是自定义，还可以是接口注入
                // 1、beanName  是类名称首字母小写
                result.add(doCreateBeanDefinition(this.toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));

                // 2、自定义

                // 3、接口注入
                for (Class<?> i : beanClass.getInterfaces()) {
                    result.add(doCreateBeanDefinition(i.getName(), beanClass.getName()));
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return result;

    }

    private LTBeanDefinition doCreateBeanDefinition(String beanName, String beanClassName) {
        LTBeanDefinition beanDefinition = new LTBeanDefinition();
        beanDefinition.setFactoryBeanName(beanName);
        beanDefinition.setBeanClassName(beanClassName);
        return beanDefinition;
    }

    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public Properties getConfig(){
        return this.contextConfig;
    }
}
