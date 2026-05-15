---
type: concept
tags:
  - Spring
  - IoC
  - MVC
summary: "手写 Spring 框架用简化实现拆解 IoC、DI、MVC 九大组件和请求分发流程，帮助理解 Spring 内部结构。"
sources:
  - "raw/笔记/spring/3、手写 Spring 之顶层设计 IoC 与 DI.md"
  - "raw/笔记/spring/4、手写 Spring 之顶层设计 MVC.md"
  - "raw/笔记/spring/custom-spring.zip"
created: "2026-05-15"
updated: "2026-05-15"
---

# 概念：手写 Spring 框架

## 定义

手写 Spring 框架是通过简化版 IoC、DI、MVC 实现来理解 Spring 顶层设计的学习方式。

## IoC 与 DI 流程

- Servlet 初始化时读取配置文件。
- 将配置抽象为 BeanDefinition。
- 扫描相关类并初始化 IoC 容器。
- 实例化对象，并为后续代理保留扩展空间。
- 完成依赖注入。
- 建立 HandlerMapping 等 MVC 结构。

## MVC 组件

原始笔记列出 MVC 九大组件：MultipartResolver、LocaleResolver、ThemeResolver、HandlerMapping、HandlerAdapter、HandlerExceptionResolver、RequestToViewNameTranslator、ViewResolvers、FlashMapManager。

## 关联页面

- [[concepts/概念_Spring核心思想|Spring 核心思想]]
- [[entities/技术_Spring|Spring]]
- [[sources/来源_Spring编程与手写框架笔记|Spring 编程与手写框架笔记]]

