---
type: concept
tags:
  - ai
  - agent
  - architecture
  - react
  - multi-agent
summary: "Agent 架构是 Agent 内部的工作模式设计，7 种主流架构从简单到复杂覆盖不同场景，实际落地往往是多种架构的融合。"
sources:
  - "raw/抖音/2026-05-14/抖音-视频-20260514-Agent的7种主流架构.md"
  - "raw/知乎/2026-05-15/万字长文解读LLM Agent：总体框架、经典论文与实践.md"
  - "raw/知乎/2026-05-15/从Claude Code入手看Agent框架设计思路：多智能体(Multi-Agent).md"
updated: "2026-05-17"
---

# 概念：Agent 架构模式

## 定义

Agent 架构（Architecture）是指 Agent 内部的工作模式设计——即 LLM 如何与环境交互、如何调用工具、如何管理记忆、如何处理反馈的整体结构设计。

> Agent = LLM + Planning（规划） + Memory（记忆） + Tools（工具）

## 7 种主流架构

### 1️⃣ ReAct（推理+行动）

最经典的 Agent 架构，由论文 *ReAct: Synergizing Reasoning and Acting in Language Models* 提出。

- **三部曲**：Think（思考）→ Act（行动）→ Observe（观察）
- **逻辑**：LLM 每一轮先推理当前状态，再行动调用工具，然后将观察结果放回上下文
- **优点**：简单直观，是 LangChain、AutoGPT 等框架的基础
- **缺点**：长任务下上下文膨胀严重，模型容易"跑偏"
- [[sources/来源_Agent的7种架构]]

### 2️⃣ Reflection（反思）

Agent 对自己的输出进行自我检查和修正。

- **Self-Critique**：对自己说"我上一步的回答有什么问题？"
- **Self-Refine**：基于批评改进输出，循环 2-3 轮
- **外部评分器**：用专门的评估模型/函数打分
- **适用**：代码生成、写作、数学推理等质量要求高的任务
- **代表**：Reflexion、CRITIC

### 3️⃣ Tool Use（工具调用）

Agent 通过 Function Calling（OpenAI）或 Tool Use（Anthropic）协议调用外部工具。

- **核心能力**：Web Search、Code Interpreter、File I/O、API 调用
- **标准化趋势**：MCP（Model Context Protocol）正在成为行业标准
- **关键**：工具描述（Schema）写得越精准，LLM 调用准确率越高
- 详见 [[concepts/概念_工具调用]]

### 4️⃣ Planning（规划与任务分解）

将复杂目标拆解为可执行的子任务序列。

- **Plan-and-Execute**：先生成完整计划→逐步执行→动态调整
- **Task Decomposition**：大任务→小任务
- **Tree-of-Thought（ToT）**：同时探索多条推理路径
- **挑战**：LLM 规划能力有上限，超长任务容易局部最优
- **代表**：BabyAGI、TaskWeaver
- 详见 [[concepts/概念_Agent规划能力]]

### 5️⃣ Multi-Agent（多智能体协作）

多个 Agent 分工协作，各司其职完成复杂任务。

- **辩论模式**：两个 Agent 扮演不同立场互相辩论
- **评审模式**：一个写代码，另一个审查
- **分层模式**：Orchestrator Agent 管理多个 Worker Agent
- **优势**：互相制衡提升输出质量
- **挑战**：Token 消耗大、调试困难、可能死循环
- **代表**：AutoGen（Microsoft）、CrewAI、ChatDev
- 详见 [[concepts/概念_ManagedAgents]]

### 6️⃣ Memory-Augmented（记忆增强）

让 Agent 拥有短期记忆和长期记忆，避免"失忆"。

- **短期记忆**：上下文窗口中的对话历史
- **长期记忆**：向量数据库、知识图谱，可跨会话持久化
- **工作记忆**：当前任务最相关的信息片段
- **关键技术**：RAG、向量数据库、记忆压缩与摘要
- **代表**：MemGPT、Mem0
- 详见 [[concepts/概念_Agent记忆]]

### 7️⃣ Human-in-the-Loop（人在回路）

在关键环节引入人类判断，保证安全与质量。

- **高危操作前确认**：删除文件、发送邮件、付款
- **关键决策审核**：Agent 生成方案→人确认后执行
- **边界模糊时求助**：Agent 无法确定时主动询问
- **价值**：在自动化效率与安全性之间取得平衡

## 架构选择指南

| 场景 | 推荐架构 | 理由 |
|------|---------|------|
| 通用问答 | ReAct | 简单直接，够用 |
| 代码生成 | ReAct + Reflection | 自检提升质量 |
| 需要查信息 | Tool Use + Memory | 实时检索+持久化 |
| 复杂多步骤 | Planning + Tool Use | 拆解执行 |
| 大型项目 | Multi-Agent | 分工协作 |
| 安全敏感 | Human-in-the-Loop | 人工兜底 |

## 关键趋势

- **融合是常态**：实际 Agent 系统往往是多种架构的组合
- 2025-2026 年 Agent 正在从实验走向生产
- 落地最大挑战是**可观测性**和**安全性**
- 详见 [[overview/知识图谱]]

## 经典范式补充

- **ReAct**：把 Thought/Action/Observation 串成循环，适合需要边查边做的任务。
- **Plan-and-Solve**：先由 Planner 生成计划，再由 Executor 执行；复杂环境中还需要动态重规划。
- **Reflection/Reflexion**：引入评价器或自我反思，把错误经验写回后续行动。
- **Claude Code Sub-agent**：把主上下文之外的探索任务交给子 Agent，典型用途是并行调研、代码库扫描和专业工具隔离。

## 关联页面

- [[concepts/概念_AI_Agent|AI Agent]]
- [[concepts/概念_ManagedAgents|Managed Agents]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_Agent记忆|Agent 记忆]]
- [[concepts/概念_Agent规划能力|Agent 规划能力]]
- [[concepts/概念_上下文工程|上下文工程]]
- [[concepts/概念_ClaudeCode多智能体|Claude Code 多智能体]]
- [[concepts/概念_Agent训练与ChatTemplate|Agent 训练与 Chat Template]]
- [[sources/来源_Agent的7种架构|来源：7种架构]]
- [[sources/来源_ManagedAgents|来源：Managed Agents]]
- [[sources/来源_LLM_Agent总体框架|来源：LLM Agent 总体框架]]
- [[sources/来源_ClaudeCode多智能体|来源：Claude Code 多智能体]]
