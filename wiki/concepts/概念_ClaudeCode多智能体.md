---
type: concept
tags:
  - ClaudeCode
  - MultiAgent
  - SubAgent
  - Agent编排
summary: "Claude Code 多智能体以 Sub-agent 和实验性 Agent Teams 为核心，通过上下文隔离、权限分工和结果汇总提升复杂任务处理能力。"
sources:
  - "raw/知乎/2026-05-15/从Claude Code入手看Agent框架设计思路：多智能体(Multi-Agent).md"
created: "2026-05-17"
updated: "2026-05-17"
---

# 概念：Claude Code 多智能体

## 定义

Claude Code 多智能体是 Claude Code 中用 Sub-agent 和 Agent Teams 处理复杂任务的架构模式。它不是简单增加 Agent 数量，而是通过上下文隔离、角色分工、工具权限和结果汇总来控制复杂度。

## Sub-agent 价值

- 节省主 Agent 上下文：子 Agent 深入探索后只回传摘要。
- 并行探索：多个只读探索任务可以同时运行。
- 专业化：Explore、Plan、claude-code-guide 等子 Agent 有不同系统提示和工具权限。
- 成本控制：简单探索可路由到更快更便宜的模型。

## 适用与不适用

适合：

- 高度并行化、互不依赖的子任务。
- 信息量远超单上下文窗口的调研任务。
- 工具调用复杂但可以分工隔离的任务。

不适合：

- 需要多个 Agent 实时共享同一上下文的协同编辑。
- 强依赖链路任务，后一步必须实时等待前一步细节。
- 大多数紧耦合代码修改任务。

## Agent Teams

Agent Teams 是实验性团队机制：Team Lead 负责创建成员、分配任务和整合结果；Teammates 有独立上下文，可以通过 mailbox 直接通信，并共享任务列表。它混合了 Orchestrator-Worker、Mesh 和 Blackboard 思路。

## 关联页面

- [[entities/项目_ClaudeCode|Claude Code 项目]]
- [[concepts/概念_Agent编排|Agent 编排]]
- [[concepts/概念_上下文工程|上下文工程]]
- [[concepts/概念_ClaudeCode任务执行机制|Claude Code 任务执行机制]]
- [[sources/来源_ClaudeCode多智能体|来源：Claude Code 多智能体]]

