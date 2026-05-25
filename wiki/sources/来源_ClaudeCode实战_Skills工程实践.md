---
type: source
tags:
  - ai
  - agent
  - claude-code
  - skill
  - claude.md
summary: "Claude Code 实战系列第三章：Skills 工程实践。对比 Claude.md 与 Skills 两种知识维度的设计哲学，定义 Skill 作为工程化文件夹的运作机制。"
sources:
  - "raw/ai/ClaudeCode实战/Chapter 3授人以渔，Skills工程实践.md"
updated: "2026-05-25"
---

# 来源：Claude Code 实战 — Skills 工程实践

> Claude Code 实战系列 Chapter 3，聚焦 Skills 工程化实践，从知识管理维度拆解 Claude.md 与 Skills 的本质区别。

## 核心要点

1. **知识的两维度**：Claude.md 承载常驻知识（项目通用规则），Skills 承载按需知识（专业领域 SOP）。两者加载策略、Token 成本和生效范围都不同。
2. **企业本体论类比**：Claude.md = 企业规章制度（所有人都必须遵守），Skills = 岗位 SOP（只有特定岗位执行特定任务时才用到）。
3. **Skill 是「文件夹」而非「字符串」**：Skill 是一个完整的工程化目录结构，可容纳代码库、文档、模板、可执行脚本，不是一段 prompt 或配置项。
4. **Skill 的本质是「教」而非「约束」**：通过教的方式让 Claude 内化领域运作逻辑，从被动执行工具变成领域熟手。
5. **Token 成本视角**：Claude.md 固定开销（每次全量加载），Skills 按需支付（渐进式加载）。

## 关键引文

> "Skills 集中于特定领域的知识，解决的是**知识的按需投放问题**。"

> "一个 Skill 是一个包含指令的**文件夹**，被打包成一个简单的目录结构，用来**教** Claude 如何处理特定任务或者工作流。"

## 关联页面

- [[concepts/概念_Skill系统|Skill 系统]] — 本章为 Skill 系统提供了新的认知维度
- [[comparisons/ClaudeMD_vs_Skills|Claude.md vs Skills]] — 基于本章表格的详细比较
- [[entities/项目_ClaudeCode|Claude Code 项目]] — Claude Code 的 Skill 机制实现方
