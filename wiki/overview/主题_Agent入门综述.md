---
type: overview
tags:
  - ai
  - agent
  - overview
summary: "Agent 是让大模型从会说走向会做的工程系统，核心由记忆、上下文、规划和工具执行组成。"
sources:
  - "raw/ai/agent/0. AI概念脉络.md"
  - "raw/ai/agent/5.Agent的记忆.md"
  - "raw/ai/agent/6.上下文工程.md"
  - "raw/ai/agent/7.Agent的核心：规划能力.md"
  - "raw/ai/agent/8.工作流 vs Agent.md"
updated: "2026-05-12"
---

# 主题：Agent入门综述

## 一句话结论

Agent 是让大模型从“会想、会说”走向“会记、会规划、会调用工具、会闭环执行”的工程系统。

## 总体框架

Agent 可以拆成四个关键模块：

- 大模型能力：意图理解、推理、文本生成、基础规划。
- 记忆系统：保存长期偏好、规则、历史摘要和关键事实。
- 上下文工程：按需加载信息，压缩历史，控制成本和噪声。
- 规划与执行：拆解目标、调用工具、检查结果、动态修正。

## 学习路径

1. 先理解 [[concepts/概念_AI_Agent|AI Agent]] 与普通大模型应用的区别。
2. 再理解 [[concepts/概念_Agent记忆|Agent 记忆]]，区分短期上下文和长期记忆。
3. 接着理解 [[concepts/概念_上下文工程|上下文工程]]，掌握如何控制输入信息。
4. 最后理解 [[concepts/概念_Agent规划能力|Agent 规划能力]] 和 [[comparisons/工作流_vs_Agent|工作流 vs Agent]]，判断什么时候该用 Agent。

## 当前知识库中的主要观点

- 大模型本体没有真正记忆，记忆主要靠工程系统实现。
- Agent 的价值不是“更会聊天”，而是能把目标转化为行动闭环。
- 上下文工程是 Agent 稳定性和成本控制的关键。
- 规划能力决定 Agent 是否能在不确定环境中继续推进。
- 选型时应避免过度设计：固定流程用工作流，动态决策用 Agent。

## 待补充问题

- 工具调用与权限控制还缺少独立概念页。
- 多 Agent 协同与子代理调度还缺少独立概念页。
- Skill / SOP 封装还缺少独立概念页。
- `raw/ai/agent/1. 什么要懂Agent.md`、`raw/ai/agent/2. 什么是AI Agent.md`、`raw/ai/agent/3. Agent的本质.md`、`raw/ai/agent/4. Agent的工具马甲.md` 当前为空，后续有内容后可继续 Ingest。

## 关联来源

- [[sources/来源_AI概念脉络|来源：AI概念脉络]]
- [[sources/来源_Agent的记忆|来源：Agent的记忆]]
- [[sources/来源_上下文工程|来源：上下文工程]]
- [[sources/来源_Agent的规划能力|来源：Agent的规划能力]]
- [[sources/来源_工作流_vs_Agent|来源：工作流 vs Agent]]
