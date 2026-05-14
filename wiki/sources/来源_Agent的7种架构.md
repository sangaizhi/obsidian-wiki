---
type: source
tags:
  - ai
  - agent
  - architecture
  - source
summary: "Agent 的 7 种主流架构：ReAct、Reflection、Tool Use、Planning、Multi-Agent、Memory-Augmented、Human-in-the-Loop。"
sources:
  - "raw/抖音/2026-05-14/抖音-视频-20260514-Agent的7种主流架构.md"
updated: "2026-05-14"
---

# 来源：Agent 的 7 种主流架构

## 来源信息

- **原始文件**：`raw/抖音/2026-05-14/抖音-视频-20260514-Agent的7种主流架构.md`
- **平台**：抖音短视频（约 7-10 分钟）
- **日期**：2026-05-14
- **链接**：https://v.douyin.com/ZqZ6B9P1PdM/
- **主题**：AI Agent 架构全景图

## 核心要点

### Agent 核心公式
> Agent = LLM + Planning（规划） + Memory（记忆） + Tools（工具）

### 7 种主流架构

| 架构 | 核心思想 | 适合场景 | 代表框架 |
|------|---------|---------|---------|
| **ReAct** | 推理→行动→观察循环 | 通用任务 | LangChain、AutoGPT |
| **Reflection** | 自我检查与修正 | 代码/写作/数学 | Reflexion、CRITIC |
| **Tool Use** | 调用外部工具/API | 需要实时信息的任务 | Function Calling / MCP |
| **Planning** | 拆解→规划→执行 | 复杂多步骤任务 | BabyAGI、TaskWeaver |
| **Multi-Agent** | 多角色分工协作 | 大型项目 | AutoGen、CrewAI、ChatDev |
| **Memory-Augmented** | 短期+长期记忆 | 需要跨会话知识 | MemGPT、Mem0 |
| **Human-in-the-Loop** | 关键节点人工介入 | 安全敏感场景 | — |

### 关键趋势

- 单一架构很难解决所有问题，实际落地往往是**多种架构融合**
- 2025-2026 年 Agent 正在从实验走向生产
- MCP 协议标准化是 Agent 生态爆发的关键拐点
- 最被低估的是 Context Engineering（上下文工程）
- Agent 落地的最大挑战是**可观测性**和**安全性**

## 关联页面

- [[concepts/概念_Agent架构模式|Agent 架构模式]]
- [[concepts/概念_AI_Agent|AI Agent]]
- [[concepts/概念_Agent规划能力|Agent 规划能力]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_Agent记忆|Agent 记忆]]
- [[concepts/概念_上下文工程|上下文工程]]
