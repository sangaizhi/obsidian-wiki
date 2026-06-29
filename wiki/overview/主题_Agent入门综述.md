---
type: overview
tags:
  - ai
  - agent
  - overview
summary: "Agent 是让大模型从会说走向会做的工程系统，核心由记忆、上下文、规划和工具执行组成。2026年，Harness Engineering 成为决定 Agent 成败的关键。"
sources:
  - "raw/ai/agent/0. AI概念脉络.md"
  - "raw/ai/agent/5.Agent的记忆.md"
  - "raw/ai/agent/6.上下文工程.md"
  - "raw/ai/agent/7.Agent的核心：规划能力.md"
  - "raw/ai/agent/8.工作流 vs Agent.md"
  - "raw/知乎/2026-06-05/最新！万字综述Harness革命！.md"
  - "raw/知乎/2026-06-05/AI Agent 入门指南（四）：Memory 记忆机制综述.md"
updated: "2026-06-29"
---

# 主题：Agent入门综述

## 一句话结论

Agent 是让大模型从"会想、会说"走向"会记、会规划、会调用工具、会闭环执行"的工程系统。2026 年，当模型能力进入高原期，**Harness Engineering** 成为决定 Agent 系统成败的关键——比拼的不再是模型本身，而是驾驭模型的外围工程体系。

## 核心公式

> **Agent = Model（大脑）+ Harness（驾驭系统）= 从野马到千里马**

## 总体框架

Agent 可以拆成六个关键模块：

- **大模型能力**：意图理解、推理、文本生成、基础规划
- **记忆系统**：保存长期偏好、规则、历史摘要和关键事实
- **上下文工程**：按需加载信息，压缩历史，控制成本和噪声（Offload/Retrieve/Reduce/Isolate/Cache 五大策略）
- **规划与执行**：拆解目标、调用工具、检查结果、动态修正
- **多 Agent 协作**：Sub-agent 隔离上下文 + Agent Teams 多智能体协同
- **Harness 控制面**：约束、验证、沙盒、恢复——确保非确定性引擎不失控

## 学习路径

1. 先理解 [[concepts/概念_AI_Agent|AI Agent]] 与普通大模型应用的区别
2. 再理解 [[concepts/概念_Agent记忆|Agent 记忆]]，区分短期上下文和长期记忆（2.1万字综述）
3. 接着理解 [[concepts/概念_上下文工程|上下文工程]]，掌握五大策略族和六大 Agent 压缩方案对比
4. 理解 [[concepts/概念_Agent规划能力|Agent 规划能力]] 和 [[sources/来源_工作流_vs_Agent|工作流 vs Agent]]
5. 深入 [[concepts/概念_Harness工程|Harness Engineering]]，理解 R.E.S.T 质量模型、8 阶段 SOP、避坑指南
6. 了解 [[concepts/概念_ManagedAgents|Managed Agents]]（Anthropic 生产级架构）和 [[concepts/概念_Agent演进类比|Agent 架构演进类比]]
7. 实战参考：[[concepts/概念_SpecCoding|Spec Coding]]、[[concepts/概念_SOUL|SOUL 主动性配置]]、[[concepts/概念_ClaudeCode任务执行机制|Claude Code 任务执行]]

## 当前知识库中的主要观点

- 大模型本体没有真正记忆，记忆主要靠工程系统实现
- Agent 的价值不是"更会聊天"，而是能把目标转化为行动闭环
- 上下文工程是 Agent 稳定性和成本控制的关键——六大 Agent 各有不同的压缩哲学
- **2026 年转折点：比拼从"模型"转向"Harness"**——相同模型，不同 Harness，性能差异巨大
- Agent 架构演变正在重走 OS 和 Cloud Runtime 的老路：控制面/数据面分离、disposable sandbox
- 多 Agent 编排从简单的 Sub-agent 到复杂的 Agent Teams、GAN-like 生成-对抗架构
- Harness 最危险的不是 bug，而是**过期的正确性**——曾经救过线上所以没人敢删

## 关联来源

- [[sources/来源_AI概念脉络|来源：AI概念脉络]]
- [[sources/来源_Agent的记忆|来源：Agent的记忆]]
- [[sources/来源_上下文工程|来源：上下文工程]]
- [[sources/来源_Agent的规划能力|来源：Agent的规划能力]]
- [[sources/来源_工作流_vs_Agent|来源：工作流 vs Agent]]
- [[sources/来源_Harness工程|来源：Harness Engineering]]
- [[sources/来源_Harness革命综述|来源：Harness 革命综述]]
- [[sources/来源_Memory记忆机制|来源：Memory 记忆机制综述]]