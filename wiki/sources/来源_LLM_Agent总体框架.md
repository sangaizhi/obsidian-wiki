---
type: source
source: "https://zhuanlan.zhihu.com/p/2000210358820946463"
author: "魔法学院的Chilia"
created: 2026-05-17
tags:
  - source
  - 知乎
  - LLM-Agent
  - ReAct
  - Planning
  - ToolUse
related:
  - "[[concepts/概念_AI_Agent|AI Agent]]"
  - "[[concepts/概念_Agent架构模式|Agent 架构模式]]"
  - "[[concepts/概念_Agent规划能力|Agent 规划能力]]"
  - "[[concepts/概念_工具调用|工具调用与执行]]"
  - "[[concepts/概念_Agent训练与ChatTemplate|Agent 训练与 Chat Template]]"
sources:
  - "raw/知乎/2026-05-15/万字长文解读LLM Agent：总体框架、经典论文与实践.md"
---

# 来源：LLM Agent 总体框架

## 来源信息

- 原始文件：`raw/知乎/2026-05-15/万字长文解读LLM Agent：总体框架、经典论文与实践.md`
- 类型：知乎文章 / Agent 综述
- 主题：LLM Agent 基本概念、经典论文和训练实践

## 核心要点

- 文章将 Agent 概括为 **LLM ×（规划 + 记忆 + 工具）**，强调工具让模型获得外部知识和真实操作能力。
- Agent 执行链路可拆成 Task-Planning、Tool Selection、Tool-Calling、Response Generation 四个阶段。
- ReAct 奠定了 Thought → Action → Observation 的推理与行动结合范式。
- Plan-and-Solve 强调先规划再执行，并在执行失败时动态重规划。
- Reflection/Reflexion 将环境反馈转为文字反思，形成自我纠错闭环。
- 实践侧关注 Agent 训练数据构建、工具调用轨迹、答案校验和 chat template 对齐。

## 关键主张

- 工具是让 LLM 从静态生成走向实时知识获取和外部操作的接口。
- Agent 的核心不是单次回答，而是持续的规划、调用工具、观察反馈和修正。
- 训练和推理格式必须与模型 chat template 对齐，否则工具调用能力会被格式漂移破坏。

## 关联页面

- [[concepts/概念_AI_Agent|AI Agent]]
- [[concepts/概念_Agent架构模式|Agent 架构模式]]
- [[concepts/概念_Agent规划能力|Agent 规划能力]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_Agent训练与ChatTemplate|Agent 训练与 Chat Template]]

