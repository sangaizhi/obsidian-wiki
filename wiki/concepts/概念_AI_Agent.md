---
type: concept
tags:
  - ai
  - agent
summary: "AI Agent 是在大模型能力之上叠加记忆、上下文管理、工具调用和执行闭环的可落地智能应用。"
sources:
  - "raw/ai/agent/0. AI概念脉络.md"
  - "raw/ai/agent/8.工作流 vs Agent.md"
  - "raw/知乎/2026-05-15/万字长文解读LLM Agent：总体框架、经典论文与实践.md"
updated: "2026-05-17"
---

# 概念：AI Agent

## 定义

AI Agent 是基于大模型的可落地应用。大模型负责理解、推理、生成和初步规划，Agent 框架负责记忆、上下文管理、工具调用、执行控制和风险闭环。

一句话：大模型像“大脑”，Agent 框架像“手脚”和“执行系统”。

## 关键能力

- 意图理解：听懂用户真正想完成什么。
- 规划能力：把目标拆成可执行步骤，并根据反馈调整。
- 长期记忆：通过文件、数据库或记忆库保存关键上下文。
- RAG 检索：按当前问题取回相关记忆或知识，而不是全量投喂。
- 上下文工程：控制输入内容，让模型只看当前必要信息。
- 工具调用：把模型的决策落到实际操作。
- 多轮循环：执行、检查、修正，再继续下一步。

## 执行阶段

更工程化的 Agent 框架可以拆成四个阶段：

1. **Task-Planning**：理解目标并拆解任务。
2. **Tool Selection**：根据任务选择合适工具。
3. **Tool-Calling**：生成参数并执行工具调用。
4. **Response Generation**：结合观察结果生成最终答复或进入下一轮。

ReAct、Plan-and-Solve、Reflection/Reflexion 等经典范式，本质上都是围绕这四个阶段调整推理、执行和反馈的位置。

## 使用场景

- 用户需求不确定、无法提前穷举流程。
- 业务逻辑复杂，需要动态查询知识库或工具。
- 输入是非结构化问题，需要边理解边行动。

## 边界

固定、重复、线性的流程优先使用工作流。Agent 的价值在于处理动态决策，不应被用来包装所有自动化任务。

## 关联页面

- [[concepts/概念_Agent记忆|Agent 记忆]]
- [[concepts/概念_上下文工程|上下文工程]]
- [[concepts/概念_Agent规划能力|Agent 规划能力]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_Agent训练与ChatTemplate|Agent 训练与 Chat Template]]
- [[工作流_vs_Agent|工作流 vs Agent]]
- [[overview/主题_Agent入门综述|Agent 入门综述]]
- [[sources/来源_LLM_Agent总体框架|来源：LLM Agent 总体框架]]
