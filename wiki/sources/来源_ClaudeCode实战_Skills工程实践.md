---
type: source
tags:
  - ai
  - agent
  - claude-code
  - skill
  - claude.md
  - progressive-disclosure
  - trigger
  - engineering
summary: "Claude Code 实战系列第三章：Skills 工程实践。对比 Claude.md 与 Skills 两种知识维度，拆解 Skill 目录结构与元数据设计，详解渐进式披露三层模型与触发机制，阐述 SKILL.md 正文的路由器思维与工程设计原则。"
sources:
  - "raw/ai/ClaudeCode实战/Chapter 3授人以渔，Skills工程实践.md"
updated: "2026-05-25"
---

# 来源：Claude Code 实战 — Skills 工程实践

> Claude Code 实战系列 Chapter 3（全文 5 节，约 260 行），聚焦 Skills 工程化实践，从知识管理维度到工程设计细节，全链路拆解。

## 核心要点

1. **知识的两维度**（§3.1）：Claude.md = 企业规章制度（常驻知识，全量加载），Skills = 岗位 SOP（按需知识，渐进式加载）。从"约束"到"教"的设计哲学转变。
2. **SKILL 工程化结构**（§3.2）：Skill 是一个文件夹而非字符串——可容纳代码库、文档、模板、脚本。目录名 kebab-case，SKILL.md 必须全大写。元数据分三维度：触发机制（name/description/argument-hint）、权限控制（disable-model-invocation/user-invocable/allowed-tools/model）、运行时环境（context/agent/hooks）。
3. **渐进式披露**（§3.3）：图书馆三层模型（编目→目录→精读），对应 description 常驻 → SKILL.md 按需 → reference/templates 按需。description 预算为上下文窗口的 2%，所有 Skill 平分。超出预算的 Skill 被静默排除。
4. **触发机制**（§3.4）：双通道激活（显式调用 + 语义匹配）。description 三要素公式（What + When + Not for）。过触发与欠触发的诊断修复。参考型 vs 任务型两种 Skill 基于副作用原则的选择标准。
5. **SKILL.md 正文设计**（§3.5）：路由器思维（正文仅含路由表，详细知识外置）。契约式引用（明确触发时机 + 资源位置 + 预期产出）。500 行法则（约 2000~3000 Token 的合理开销上限）及重构对策。

## 关键引文

> "Skills 集中于特定领域的知识，解决的是**知识的按需投放问题**。"

> "一个 Skill 是一个包含指令的**文件夹**，被打包成一个简单的目录结构，用来**教**大模型如何处理特定任务或者工作流。Skill 绝不仅仅是一段 Prompt 或者一个简单的配置项。"

> "description 是给大模型看的，而非人类读者。大模型阅读 description 是在进行深度的语义匹配。"

> "SKILL.md 是路由器，不是知识仓库。文件自身仅包含核心流程与路由表，而详细的知识内容则分散存储于被引用的文件中。"

> ""副作用"越大，控制权越要收紧。对于任何可能改变系统状态、造成不可逆后果的操作，永远不要信任 Claude 的自动判断。"

> "500 行代码约为 2000~3000 Tokens，是单个 Skill 激活后合理的上下文开销。若超过 500 行，就意味着我们将'参考资料'和'路由指令'混淆了，需要立即重构。"

## 关联页面

- [[concepts/概念_渐进式披露|渐进式披露]] — §3.3 三层加载模型与预算机制
- [[concepts/概念_Skill触发机制|Skill 触发机制]] — §3.4 双通道激活与 description 设计
- [[concepts/概念_Skill工程设计|Skill 工程设计]] — §3.2 + §3.5 目录规范、元数据、路由器思维
- [[concepts/概念_Skill系统|Skill 系统]] — 跨来源的 Skill 完整知识体系
- [[comparisons/ClaudeMD_vs_Skills|Claude.md vs Skills]] — §3.1 知识两维度的详细比较
- [[entities/项目_ClaudeCode|Claude Code 项目]] — Claude Code 的 Skill 机制实现方
