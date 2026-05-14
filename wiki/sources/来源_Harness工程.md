---
type: source
tags:
  - ai
  - agent
  - harness
  - engineering
  - source
summary: "Harness Engineering 是一整套把大模型纳入工程体系的控制面：真相源、执行边界、能力接入、观测调试与可验证交付。"
sources:
  - "raw/知乎/2026-05-14/Harness工程详解.md"
updated: "2026-05-14"
---

# 来源：Harness Engineering

## 来源信息

- **原始文件**：`raw/知乎/2026-05-14/Harness工程详解.md`
- **平台**：知乎专栏
- **日期**：2026-05-14
- **链接**：https://zhuanlan.zhihu.com/p/2035659924911421217
- **主题**：AI Agent Harness Engineering——让大模型从玩具变成生产力

## 核心要点

### 什么是 Harness Engineering

Harness 不是 Prompt 技巧，也不是多写几份文档。它是**一整套把大模型纳入工程体系的控制面**：真相源、执行边界、能力接入、观测调试、可验证交付。

| | 传统软件工程 | Harness Engineering |
|--|------------|-------------------|
| 管理对象 | 确定性（代码逻辑） | 非确定性（概率引擎） |
| 目标 | 防止人犯错 | 约束模型不失控 |
| 工具 | 类型系统、单测、CR | 沙盒、Checkpoint、外部验证 |

### 好的 Harness 三要素

1. **前置验证（Evaluator 沙盒）** — 基于证据触发 Retry
2. **最小真相源（Spec is Truth）** — 任务跨天能无损恢复
3. **物理门禁（Checkpoint Before Execute）** — 破坏前必须授权

### 避坑指南

**伪 Harness：**
- "软约束"陷阱 — 在 Prompt 写 5000 字 DO NOT，只是口头嘱咐
- "军火库"陷阱 — 塞 20 个 API 让模型自己挑，没有边界约束

**劣质 Harness：**
- "盲打"陷阱 — 暴力死循环重试
- "官僚主义"陷阱 — 强制重型文档流，浪费 Token

### 8 阶段 SOP

目标收敛 → 状态恢复 → 上下文装配 → 任务分块 → 链路设计 → 执行前校准 → 外部验证 → 回写交接

### 核心结论

> 大模型已经够强，可以参与研发交付；但没有 Harness，它充其量是个高级玩具。
> 程序员的核心价值正在从"亲手写代码"转向"定义目标、卡住边界、掌控节奏、验收结果"。

## 关联页面

- [[concepts/概念_Harness工程|Harness Engineering]]
- [[concepts/概念_ManagedAgents|Managed Agents]]
- [[concepts/概念_上下文工程|上下文工程]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_AI_Agent|AI Agent]]
