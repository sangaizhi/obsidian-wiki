---
type: source
source: "https://research.perplexity.ai/articles/designing-refining-and-maintaining-agent-skills-at-perplexity"
author: "慢学AI（抖音解读）"
created: 2026-05-15
tags:
  - source
  - 抖音
  - Perplexity
  - Skill设计
  - ContextEngineering
related:
  - "[[concepts/概念_Skill系统|Skill 系统]]"
  - "[[concepts/概念_上下文工程|上下文工程]]"
  - "[[entities/项目_OpenClaw|OpenClaw 项目]]"
---

# 来源：Perplexity Skill 设计方法论

> Perplexity 官方论文《Designing, Refining, and Maintaining Agent Skills at Perplexity》的精读解读。核心观点：Skill 的本质是"上下文封装（Context Packaging）"，目标是给模型提供正确的运行时上下文。

## 核心洞见

### Skill = 上下文税

每个加载的 Skill 都占用上下文窗口。Skill 设计的目标是让收益大于税收——做到**精准触发 + 渐进加载 + 行为修正**。

### Six-Step 设计框架

| 步骤 | 核心思想 |
|------|---------|
| ① Context Packaging | Skill 不是文档，是面向模型的运行时上下文模块 |
| ② Context Engineering | 上下文像内存一样管理，而非全部塞入 System Prompt |
| ③ Progressive Loading | 三层加载：Index (<100t) → SKILL.md (~5000t) → Heavy Assets |
| ④ Description = Router | Description 不是功能简介，是路由触发器 |
| ⑤ Skill Tree | 分层路由，像 B-Tree 一样组织，避免平铺 |
| ⑥ Gotchas > Process | 模型知道流程，但不知道坑——避坑指南比流程更有价值 |

### Eval-Driven Development

Skill 开发第一步不是写 Skill，是先写 Eval：

- **Routing Eval** — 是否正确加载
- **File Read Eval** — 是否读取正确文件
- **Progressive Loading Eval** — 是否按需加载
- **End-to-End Eval** — 最终任务质量

### Append-Mostly 维护

Skill 不是频繁改规则，而是持续追加失败经验。每次踩坑就追加一条 Gotcha。

### 反面案例

Perplexity 曾尝试 1945 个税法 Skill 平铺——路由效果极差。解决方案是分层路由（Federal / State / International）。

## 与 OpenClaw 对齐

| Perplexity | OpenClaw |
|-----------|---------|
| Skill Index | ClawHub skill 注册 |
| Progressive Loading | skill 按需注入 |
| Append-Mostly | self-improving skill learnings |
| Description is Router | skill trigger 设计 |

## 相关页面

- [[concepts/概念_Skill系统|Skill 系统]] — 核心概念
- [[concepts/概念_上下文工程|上下文工程]] — Context Engineering 理论基础
- [[entities/项目_OpenClaw|OpenClaw 项目]] — 对齐参考
