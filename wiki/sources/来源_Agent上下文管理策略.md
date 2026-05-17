---
type: source
source: "https://zhuanlan.zhihu.com/p/2012088406826562496"
author: "魔法学院的Chilia"
created: 2026-05-17
tags:
  - source
  - 知乎
  - 上下文工程
  - Agent
  - ClaudeCode
related:
  - "[[concepts/概念_上下文工程|上下文工程]]"
  - "[[concepts/概念_Agent记忆|Agent 记忆]]"
  - "[[concepts/概念_Agent编排|Agent 编排]]"
  - "[[entities/项目_ClaudeCode|Claude Code 项目]]"
sources:
  - "raw/知乎/2026-05-15/万字长文解析Agent框架中的上下文管理策略.md"
---

# 来源：Agent 上下文管理策略

## 来源信息

- 原始文件：`raw/知乎/2026-05-15/万字长文解析Agent框架中的上下文管理策略.md`
- 类型：知乎文章 / Agent 框架设计
- 主题：上下文工程、短期记忆、外部存储、摘要、隔离和缓存

## 核心要点

- 上下文工程目标是用最少、信号最强的 token 集合，最大化模型输出质量。
- Agent 长时间运行会累积工具观察结果，导致上下文膨胀、成本上升和 context rot。
- 可逆压缩优先于有损摘要：将长输出、网页、文档和日志卸载到文件系统，只在上下文保留路径。
- Just-in-time 检索让 Agent 通过 grep、jq、find 等命令按需探索，避免复杂 RAG 管线的维护成本。
- 摘要化是最后手段，应保留完整 JSONL 历史以便必要时恢复精确细节。
- 多智能体架构通过上下文隔离让子 Agent 承担大量搜索和工具输出，只把摘要回传主 Agent。
- KV Cache 要求稳定 prefix，Claude Code 通过追加式上下文、确定性序列化和缓存断点降低延迟和成本。

## 关联页面

- [[concepts/概念_上下文工程|上下文工程]]
- [[concepts/概念_Agent记忆|Agent 记忆]]
- [[concepts/概念_Agent编排|Agent 编排]]
- [[entities/项目_ClaudeCode|Claude Code 项目]]

