---
type: concept
tags:
  - ai
  - agent
  - skill
  - sub-agent
  - claude-code
  - architecture
summary: "Skills 与子智能体的两种原子组合模式：方向A（子智能体通过 skills 字段预加载 Skill，子智能体包含 Skill）与方向B（Skill 通过 context: fork 派生子智能体，Skill 包含子智能体）。核心区别在于包含关系决定了 System Prompt 控制权和知识注入方式。"
sources:
  - "raw/ai/ClaudeCode实战/Chapter4 分而治之：子智能体与任务委派.md"
updated: "2026-06-11"
---

# 概念：Skills 与子智能体协作

## 定义

Skills 与子智能体是 Claude Code 两大核心能力域，分别解决"专业知识按需加载"和"任务隔离分解"。两者的组合存在两种**原子模式**，核心区别在于**包含关系**——谁包含谁，决定了知识注入方式和 System Prompt 控制权。

## 问题起源

子智能体（如 `bug-fixer`）虽然配置了工具，也知道需要修复的根因，但它**缺少领域专业知识**：
- 不知道项目的代码规范格式
- 不知道如何处理异常
- 不知道如何命名变量和方法

通用规范可写入 `CLAUDE.md`，但**具体场景的规则属于 Skills 范畴**。这就是两者必须协作的根本原因。

## 两种原子模式

### 方向A：子智能体预加载 Skill

**包含关系**：子智能体 → 包含 → Skill

| 维度 | 说明 |
|------|------|
| **角色定位** | 子智能体是执行者，Skill 是其"操作手册" |
| **触发方式** | 子智能体启动时通过 `skills` 字段预加载 |
| **System Prompt** | SubAgent.md 正文 + Skill 全量内容注入 |
| **使用场景** | 流水线中需要特定领域知识的角色 |

**示例：改进版 bug-fixer**

```yaml
# .claude/agents/bug-fixer.md
---
name: bug-fixer
description: 基于定位结果修复bug，遵循团队安全编码规范
tools: [Read, Grep, Glob, Edit, Write, Bash]
skills:
  - secure-coding    # 预加载安全编码Skill
---
```

子智能体启动时，`secure-coding` Skill 的全量内容（空值防御、错误处理、安全编码规范）被注入其 System Prompt，bug-fixer 在修复过程中自动遵循这些规范。

### 方向B：Skill 派生子智能体

**包含关系**：Skill → 包含 → 子智能体

| 维度 | 说明 |
|------|------|
| **角色定位** | Skill 是触发器，子智能体是隔离执行环境 |
| **触发方式** | 用户 `/skill-name` → Skill 的 `context: fork` → 自动 fork 子智能体 |
| **System Prompt** | Agent 类型默认 Prompt + CLAUDE.md + SKILL.md 作为任务指令 |
| **使用场景** | 需要隔离执行、不污染主对话上下文的任务 |

**示例：codebase-health-check**

```yaml
# .claude/skills/codebase-health-check/SKILL.md
---
name: codebase-health-check
context: fork          # 自动 fork 子智能体
agent: general-purpose # 子智能体类型
allowed-tools: [Read, Grep, Glob]
---
```

用户执行 `/codebase-health-check` → Skill 自动 fork 一个 `general-purpose` 子智能体，在隔离上下文中执行健康检查，只向主对话返回结论。

## 核心区别对比

| 维度 | 方向A（子智能体包含Skill） | 方向B（Skill包含子智能体） |
|------|--------------------------|--------------------------|
| **包含关系** | 子智能体 ⊃ Skill | Skill ⊃ 子智能体 |
| **触发者** | 主智能体委派子智能体 | 用户调用 Skill |
| **Skill 作用** | 作为子智能体的知识库 | 作为子智能体的启动器 |
| **System Prompt 控制权** | 子智能体定义文件为主 | Skill 正文为主 |
| **知识注入方式** | 预加载（skills 字段） | 任务指令（SKILL.md 正文） |
| **隔离性** | 子智能体天然隔离 | 通过 fork 实现隔离 |
| **典型场景** | 流水线中的专业角色 | 独立的审查/检查任务 |

## 选择指南

| 问题 | 选择 |
|------|------|
| 知识是角色的长期配置？ | 方向A（子智能体预加载） |
| 知识是任务的临时需求？ | 方向B（Skill 派生） |
| 需要独立的上下文隔离？ | 方向B（context: fork） |
| 需要与其他子智能体组成流水线？ | 方向A（子智能体编排） |

> 两种模式不互斥。一个复杂的 Agent 系统中可以同时使用两种模式：方向B 启动隔离任务，方向A 为流水线角色注入专业知识。

## 关联页面

- [[concepts/概念_Skill触发机制|Skill 触发机制]] — context: fork 即方向B的实现
- [[concepts/概念_Skill工程设计|Skill 工程设计]] — skills 字段元数据与 context 配置
- [[concepts/概念_渐进式披露|渐进式披露]] — Skill 内容注入子智能体 = 知识传递
- [[concepts/概念_ClaudeCode多智能体|Claude Code 多智能体]] — 子智能体基础机制
- [[concepts/概念_Skill实战案例|Skill 实战案例]] — 完整 Skill 设计
- [[entities/项目_ClaudeCode|Claude Code 项目]]
