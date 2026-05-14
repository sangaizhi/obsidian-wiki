---
type: source
tags:
  - ai
  - agent
  - managed-agents
  - anthropic
  - source
summary: "Anthropic Managed Agents 架构：通过大脑与双手解耦、Session Store 持久化和零信任沙箱实现生产级多 Agent 协作。"
sources:
  - "raw/抖音/2026-05-14/抖音-视频-20260514-ManagedAgents.md"
updated: "2026-05-14"
---

# 来源：Managed Agents 架构

## 来源信息

- **原始文件**：`raw/抖音/2026-05-14/抖音-视频-20260514-ManagedAgents.md`
- **平台**：抖音短视频
- **日期**：2026-05-14
- **链接**：https://v.douyin.com/Lug5iquIwVU/
- **主题**：Anthropic Managed Agents 架构深度解读

## 核心要点

### 什么是 Managed Agents

Anthropic 于 2026 年 4 月发布 **Claude Managed Agents**（Public Beta），是一个全托管的 Agent 平台，通过 API 让开发者在云端构建、部署生产级 AI Agent。

### 三大核心抽象

- **Session** — 持久化事件日志，可追加、可时间回溯，独立于上下文窗口
- **Harness** — 控制器，负责调用 Claude、路由工具调用、管理上下文窗口
- **Sandbox** — 沙箱执行层，提供隔离的代码执行和文件编辑环境

### 大脑与双手解耦

**解耦前的耦合问题：**
- 所有组件塞进一个容器 → 挂了就丢
- 容器挂了 → Session 丢失 → 无法调试
- Prompt Injection 可直通凭据

**解耦后的优势：**
- 容器死亡 → Harness 捕获错误 → Claude 决定重试 → 新容器初始化
- Harness 死亡 → 从 Session 日志中恢复，继续执行
- 凭据永远不在沙箱中暴露，零信任沙箱
- 组件可独立替换和演进

### SessionStore 的意义

Session 是独立于上下文窗口的持久化事件日志，支持快进、回退和重新阅读，任何被压缩的上下文都可从 Session 恢复。

### 安全设计

- 凭据绑定资源，Agent 不直接操作
- MCP 代理 + OAuth token 存在安全 Vault 中
- 零信任沙箱 — Prompt Injection 也无法访问凭据

## 关联页面

- [[concepts/概念_ManagedAgents|Managed Agents 架构]]
- [[concepts/概念_Agent架构模式|Agent 架构模式]]
- [[concepts/概念_AI_Agent|AI Agent]]
- [[concepts/概念_工具调用|工具调用与执行]]
