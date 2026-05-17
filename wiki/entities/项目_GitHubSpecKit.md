---
type: entity
entity: 项目
name: GitHub Spec Kit
created: 2026-05-17
updated: 2026-05-17
tags:
  - entity
  - project
  - GitHub
  - SpecKit
  - SDD
related:
  - "[[concepts/概念_SpecCoding|Spec Coding]]"
  - "[[entities/项目_ClaudeCode|Claude Code 项目]]"
sources:
  - "[[sources/来源_GitHubSpecKit入门|来源：GitHub Spec Kit 入门]]"
---

# GitHub Spec Kit

## 基本信息

GitHub Spec Kit 是面向 AI 驱动软件开发的规格驱动开发工具和流程脚手架。它的目标是把开发从一次性大 Prompt 的 Vibe Coding，转为意图、规格、计划、任务和实现逐步收敛的工程流程。

## 核心理念

- Intent first：先定义做什么和为什么做，再讨论技术实现。
- Rich specs：用结构化规格和检查清单约束 AI。
- Multi-step refinement：通过多阶段逐步细化，而不是一次性让 AI 写完整功能。
- Model-agnostic control：可与不同 Agent 或模型协同，不绑定单一技术栈。

## 工作流

1. `/constitution`：建立项目原则，生成 `.specify/memory/constitution.md`。
2. `/specify`：描述功能需求和验收标准，生成 `spec.md`。
3. `/clarify`：可选，用问题澄清模糊需求。
4. `/plan`：基于规格和宪法生成技术方案、数据模型、API 契约等。
5. `/analyze`：可选，检查 spec/plan 等工件间的矛盾和遗漏。
6. `/tasks`：把方案拆成可执行任务清单。
7. `/implement`：按任务逐步实现代码。

## 适用场景

适合新项目、复杂功能、团队协作、生产级原型和遗留系统现代化。不适合简单 bug 修复、小范围 UI 微调，或仍在摸索方向的探索式开发。

## 关联页面

- [[concepts/概念_SpecCoding|Spec Coding]]
- [[entities/项目_ClaudeCode|Claude Code 项目]]
- [[sources/来源_GitHubSpecKit入门|来源：GitHub Spec Kit 入门]]

