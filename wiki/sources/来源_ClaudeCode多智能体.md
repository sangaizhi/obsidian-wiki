---
type: source
source: "https://zhuanlan.zhihu.com/p/2018114851793315554"
author: "魔法学院的Chilia"
created: 2026-05-17
tags:
  - source
  - 知乎
  - ClaudeCode
  - MultiAgent
  - SubAgent
related:
  - "[[entities/项目_ClaudeCode|Claude Code 项目]]"
  - "[[concepts/概念_ClaudeCode多智能体|Claude Code 多智能体]]"
  - "[[concepts/概念_Agent编排|Agent 编排]]"
  - "[[concepts/概念_上下文工程|上下文工程]]"
sources:
  - "raw/知乎/2026-05-15/从Claude Code入手看Agent框架设计思路：多智能体(Multi-Agent).md"
---

# 来源：Claude Code 多智能体

## 来源信息

- 原始文件：`raw/知乎/2026-05-15/从Claude Code入手看Agent框架设计思路：多智能体(Multi-Agent).md`
- 类型：知乎文章 / Claude Code 架构分析
- 主题：Sub-agent、Multi-Agent 协调模式、Agent Teams

## 核心要点

- Claude Code 的 Sub-agent 是一种极简、工业化程度较高的 Multi-Agent 实现，主要用于上下文隔离和并行探索。
- 多智能体适合高度并行、信息量超出单上下文、工具调用复杂且可分工的任务；不适合强共享上下文和强依赖实时协作任务。
- Claude Code 内置 general-purpose、Explore、Plan、statusline-setup、claude-code-guide 等不同子 Agent，权限和模型选择不同。
- Sub-agent 不递归启动更多 Agent，避免无限递归和协调复杂度失控。
- 多智能体协调模式可分为 Orchestrator-Worker、Hierarchical、Mesh、Swarm/Blackboard 等。
- Agent Teams 是实验性机制：Team Lead 分配任务，Teammates 可直接通信，并通过共享任务列表协同。

## 关联页面

- [[entities/项目_ClaudeCode|Claude Code 项目]]
- [[concepts/概念_ClaudeCode多智能体|Claude Code 多智能体]]
- [[concepts/概念_Agent编排|Agent 编排]]
- [[concepts/概念_上下文工程|上下文工程]]

