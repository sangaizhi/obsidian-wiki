---
type: concept
concept: Spec Coding
created: 2026-05-14
tags:
  - concept
  - methodology
  - workflow
  - Claude Code
related:
  - "[[concepts/概念_Harness工程|Harness Engineering]]"
  - "[[concepts/概念_Skill系统|Skill 系统]]"
  - "[[concepts/概念_工具调用|工具调用与执行]]"
  - "[[concepts/概念_Agent编排|Agent 编排]]"
sources:
  - "[[sources/来源_SpecCoding实战|来源：Spec Coding 实战]]"
---

# Spec Coding（规格驱动编码）

> 在写代码之前先写规格文档，通过结构化工作流消除 AI 不确定性的开发方法论。核心思想是「规格先行的渐进式编码」。

## 工作流

每个功能变更经历五个阶段：

1. **Proposal** — 明确为什么做、怎么做，避免实现完才发现方向不对
2. **Design** — 设计方案和架构决策
3. **Specs** — 详细规格定义（输入/输出/边界条件）
4. **Tasks** — 将规格拆解为可执行的任务分组
5. **Code** — AI 基于蓝图自主编码

## 三层规范体系

Spec Coding 在实践中衍生出三层协同规范体系：

### 第一层：约束层（.claude/rules/）
告诉 AI「禁止什么、必须怎样」。7 个规范文件覆盖 TypeScript、命名、注释、Lint、样式、页面结构、API 接口。

### 第二层：示范层（.claude/code-design/）
告诉 AI「标准产出长什么样」。预置完整标准代码模板（列表页、表单页、抽屉组件等），AI 生成新页面时直接参照。

### 第三层：视觉层（.claude/ui-design/）
告诉 AI「页面应该长什么样」。HTML 设计稿可直接在浏览器预览，AI 读取结构样式信息辅助生成。

## 核心价值

- **减少返工**：在 proposal 阶段明确方向和做法
- **可审计**：每个 Change 的完整决策链留有记录
- **消除信息孤岛**：MCP 直连接口文档和 PRD 文档
- **规范一致性**：205 个文件保持高度一致的代码风格

## 与相关概念的关系

- [[concepts/概念_Harness工程|Harness Engineering]] — Spec Coding 是 Harness 在开发流程层面的具体实践，通过规范体系约束 AI 产出
- [[concepts/概念_Skill系统|Skill 系统]] — 示范层代码模板可演化为 Skill，实现规范的可复用
- [[concepts/概念_FunctionCalling|Function Calling]] — MCP 工具在 Spec Coding 中用于消除信息断层（接口文档直连等）

## 已知局限

- 复杂排障场景效果受限（多根因互相掩盖、隐性行为无文档时超出 AI 推断能力）
- 规范文件只是「约束」而非「能力」— 需要示范层和视觉层补充
- AI 可能使用训练数据中的废弃 API，需通过报错信息触发修正
