---
type: source
tags:
  - claude-code
  - agent
  - harness
  - architecture
  - system-prompt
  - memory
  - context
summary: 基于Claude Code源码泄露事件（npm打包误传.map文件导致51万行核心代码公开），深入剖析Claude Code的技术原理。涵盖四层架构设计、ReAct+Tool-Use循环、运行时动态System Prompt构造、四类型记忆系统（CLAUDE.md）、五步上下文压缩机制。
sources:
  - "https://www.zhihu.com/question/1910264205526962733/answer/2039659438911513042"
updated: 2026-06-05
---

# Claude Code技术原理深度剖析

> 原文作者：小林coding | 基于Claude Code源码泄露事件（npm .map文件误传，51万行核心代码公开）

## 核心要点

- **四层分层架构**：引擎层（大脑，只做协调+分发+决策，不含业务逻辑）、工具层（40+工具，每个强制声明只读/破坏性/并发安全性三个安全属性——"每一把刀都有刀鞘"）、服务层（共享基础设施）、集成层（MCP协议、API Gateway等外部对接）。新增能力只需新增工具，引擎层不用改。
- **Agent核心循环**：不是预定义流程图，而是"感知-决策-行动"自主循环。模型每次看到当前上下文后自主判断下一步——这是Agent与ChatBot/Copilot的本质区别。Claude Code在ReAct基础上加入了优化：并行执行多个只读工具，有副作用的串行独占。
- **System Prompt是运行时动态组装的**：不是写死的字符串——工具描述从每个工具的`prompt()`方法动态汇总、MCP指令注入、Skill索引注入、环境信息注入、ToolSearch提示追加。禁用某个工具后其描述也会同步消失。
- **四类型记忆系统（CLAUDE.md）**：记四类（用户偏好、项目约定、重要决策及原因、常用命令模板），不记四类（临时信息、可自动获取的信息、会过期的事实、个人敏感信息）。存储为索引+独立文件，用Sonnet当秘书做语义召回，支持并行预取优化。
- **上下文窗口五步压缩**：大结果存磁盘 → 砍掉远古消息 → 裁剪老工具输出 → 读时投影 → 全量摘要。不是简单截断，而是按信息"保质期"分级处理。
- **Git安全协议**：对force push、`reset --hard`等危险操作必须用户显式授权才能执行。环境信息注入让模型有基本运行时感知（平台/日期/工作目录）。

## 关键引文

> "Claude Code 80%的代码根本不是在搞什么黑科技让AI更聪明，而是在死磕'可靠性'。"

> "Harness Engineering说白了就是认清现实：与其天天'做法'祈求大模型变聪明，不如老老实实给这匹野马套上缰绳，用系统去约束它的行为。"

> "每一把刀都有刀鞘，从出厂就配好了安全机制——每个工具的只读/破坏性/并发属性是类型系统强制要求的，漏了任何一个代码就编译不过。"

## 关联页面

- [[项目_ClaudeCode]] — Claude Code项目总览
- [[概念_ClaudeCode多智能体]] — 子Agent生成与多Agent协作
- [[概念_ClaudeCode任务执行机制]] — Agent循环与工具执行详解
- [[概念_上下文工程]] — 五步压缩策略的上下文工程实践
- [[概念_FunctionCalling]] — Tool-Use循环的底层机制
- [[概念_Skill系统]] — Skill调用与系统提示词中Skill索引的注入
- [[来源_ClaudeCode源码详解]] — 两万字源码核心机制详解（本文的补充深入）
- [[来源_ClaudeCode源码重现]] — CoreCoder：950行Python重现核心设计
- [[来源_Harness详解]] — Harness Engineering作为工程实践的系统性阐述
