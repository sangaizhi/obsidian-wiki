---
type: source
tags:
  - ai
  - claude-code
  - prompt-engineering
  - context-engineering
  - source
summary: "CLAUDE.md 三步优化法：根文件压到 60 行以内，按关注点拆分到 .claude/rules/，用 /memory 逐条验收。"
sources:
  - "raw/抖音/2026-05-14/抖音-视频-20260514-CLAUDE优化指南.md"
updated: "2026-05-14"
---

# 来源：CLAUDE.md 优化指南

## 来源信息

- **原始文件**：`raw/抖音/2026-05-14/抖音-视频-20260514-CLAUDE优化指南.md`
- **平台**：抖音短视频
- **日期**：2026-05-14
- **视频ID**：7638645584525643018
- **主题**：CLAUDE.md 优化方法

## 核心要点

### 问题

CLAUDE.md 增长到 200 行以上时：上下文窗口被大量规则占用、规则间互相冲突、AI 反而变笨。

### 三步优化法

**第一步：根文件做薄（< 60 行）**
只保留项目概览、核心约束、技术栈概述。具体编码风格、团队流程等移到 rules 目录。

**第二步：按关注点拆分到 `.claude/rules/`**
```
.claude/rules/
├── coding-style.md
├── testing.md
├── git-commit.md
├── architecture.md
├── review.md
├── dependencies.md
└── memory/
```

**第三步：用 `/memory` 逐条验收**
验证每条规则是否被 AI 正确理解，检查是否与项目实际工作流一致。

### 核心观点

> CLAUDE.md 写到几百行，再加东西只会让它越用越蠢。

这是 **Context Engineering** 的最佳实践：根文件是精炼的索引，rules 是按需加载的模块。

## 关联页面

- [[concepts/概念_上下文工程|上下文工程]]
- [[concepts/概念_Skill系统|Skill 系统]]
- [[overview/知识图谱|知识图谱]]
