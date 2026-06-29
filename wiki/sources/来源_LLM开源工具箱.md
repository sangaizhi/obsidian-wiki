---
type: source
source: "https://zhuanlan.zhihu.com/p/2041886052420409269"
author: "何宇峰（月之暗面 Kimi AI Agent 工程师）"
created: 2026-06-05
tags:
  - source
  - 知乎
  - tool
  - llm
  - open-source
  - prompt-engineering
  - cost-tracking
related:
  - "[[entities/工具_LLM开源工具箱|LLM 开源工具箱]]"
  - "[[concepts/概念_Skill系统|Skill 系统]]"
  - "[[concepts/概念_上下文工程|上下文工程]]"
  - "[[entities/项目_ClaudeCode|Claude Code 项目]]"
---

# 来源：做 LLM 应用半年，我给自己攒了一套开源工具箱

> 作者：何宇峰（月之暗面 Kimi AI Agent 工程师）｜ 2026-05-24

## 背景

作者在做 LLM 应用一年过程中，反复被小问题折腾——找开源 issue、写 AI 规则文件、调 prompt 效果、追踪 API 花费、批量处理数据——于是每个痛点做成了一个工具，全部开源。

## 五件套工具

### GitSense — 找开源贡献机会
- 扫描 GitHub 热门仓库的 open issue，用 LLM 分析与个人技能的匹配度（1-10 分打分）
- Repo Radar：评估仓库的维护活跃度、review 节奏、issue 质量、PR merge 倾向
- 安装：`pip install gitsense-radar`

### RuleForge — 自动生成 AI 助手规则
- 扫描代码库，检测 20+ 种语言和 30+ 种框架，自动生成 CLAUDE.md 或 .cursorrules
- 非通用模板，根据项目实际使用情况生成
- 安装：`pip install ruleforge`

### PromptDiff — Prompt 的语义 Diff
- 两个版本 prompt 跑同一组测试用例，用 embedding cosine similarity 做语义比较
- LLM-as-judge 分类改进和退步，支持 JSON 接 CI
- 安装：`pip install "promptdiff[semantic]"`

### TokenTracker — LLM 花费追踪
- 改一行 import 即可追踪所有 LLM 调用的 token 和花费
- 数据存本地 SQLite，零配置，不上传云端
- 安装：`pip install tokentracker`

### BatchLLM — 批量调 LLM
- CSV 丢进去，每行用 LLM 处理，自动并发 + 自动重试 + 断点恢复
- 支持 `batchllm estimate` 预估费用
- 安装：`pip install batchllm`

## 使用链路

```
GitSense（找 issue） → RuleForge（配规则） → PromptDiff（调 prompt） → BatchLLM（跑评测） → TokenTracker（盯成本）
```

## 关键引文

> "每个问题都不大，但每次遇到都浪费时间。于是一个一个做成了工具，全部开源。pip install 一行装好，解决一个痛点。"

> "工具是副产品，但用起来比主项目还频繁。"

## 关联页面

- [[entities/工具_LLM开源工具箱|LLM 开源工具箱]] — 五件套工具详细介绍
- [[concepts/概念_Skill系统|Skill 系统]] — RuleForge 自动生成的 .cursorrules / CLAUDE.md 属于 Skill 上下文
- [[concepts/概念_上下文工程|上下文工程]] — PromptDiff 做 prompt 语义 diff 属于上下文工程范畴
- [[entities/项目_ClaudeCode|Claude Code 项目]] — CoreCoder 是 Claude Code 的核心提炼重写
