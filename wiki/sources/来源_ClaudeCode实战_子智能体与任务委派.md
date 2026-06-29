---
type: source
source: "raw/ai/ClaudeCode实战/Chapter4 分而治之：子智能体与任务委派.md"
created: 2026-06-29
tags:
  - source
  - ai
  - agent
  - claude-code
  - sub-agent
  - multi-agent
  - task-delegation
  - context-engineering
related:
  - "[[concepts/概念_ClaudeCode多智能体|Claude Code 多智能体]]"
  - "[[concepts/概念_Skills与子智能体协作|Skills 与子智能体协作]]"
  - "[[concepts/概念_Skill系统|Skill 系统]]"
  - "[[concepts/概念_上下文工程|上下文工程]]"
  - "[[concepts/概念_Agent编排|Agent 编排]]"
  - "[[entities/项目_ClaudeCode|Claude Code 项目]]"
---

# 来源：Claude Code 实战 — Chapter 4 分而治之：子智能体与任务委派

> 来源：`raw/ai/ClaudeCode实战/Chapter4 分而治之：子智能体与任务委派.md`

## 核心要点

- **子智能体本质**：一个具备独立上下文窗口、受限工具权限及明确任务范围的大模型实例。主智能体按需启动并传递任务描述，子智能体在隔离上下文中执行，仅返回结果摘要。
- **三大核心价值**：隔离（独立上下文不污染主对话）、约束（工具权限白名单物理保障最小权限）、复用（Markdown 定义，可团队共享、项目迁移）。
- **定义方式**：`./claude/agents/` 目录下的 Markdown 文件，由 YAML 前置元数据（name/description/tools/model/permissionMode/skills/hooks）+ Markdown 正文指令组成。
- **6 种属性**：name（唯一标识符）、description（路由依据）、tools（工具白名单，物理安全边界）、model（按复杂度选模型、控成本）、permissionMode（plan=系统级只读保障）、skills/hooks（预加载知识与事件钩子）。
- **3 个实战示例**：代码审查子智能体（只读 3 工具 + 结构化输出）、测试运行子智能体（execute-only角色 + 禁止编辑源码和测试用例）、日志分析子智能体（受限 Bash + JQ 管线 + 异常检测算法）
- **Skills 与子智能体两种组合模式**：
  - **方向A**：子智能体预加载 Skill（子智能体是主角，Skill 是"操作手册"，通过 skills 字段注入 System Prompt）
  - **方向B**：Skill 派生子智能体（Skill 是主角，通过 context: fork 创建隔离执行容器，SKILL.md 作为任务指令）
- **职责划分原则**：子智能体定义"身份与目标"（战略），Skill 定义"方法与标准"（战术）。规划与执行分离赋予框架强大的复用能力。
- **启用决策四维度**：大规模文件读取（隔离输入噪声）、高频输出生成（过滤只回传摘要）、上下文完整保护（保护主对话空间）、操作权限与安全边界（受限沙箱隔离）

## 关键引文

> "子智能体拥有独立的上下文窗口，读取的文件内容、命令执行输出以及中间的推理过程，均严格保留在自身的上下文空间内，绝不会流至主对话。"

> "工具权限约束不是简单的'请不要修改文件'这类被忽视的建议，而是构建了物理层面的边界。"

> "子智能体定义'身份与目标'，Skill 定义'方法与标准'——这种规划与执行的分离机制，赋予了 Agent 框架强大的复用能力。"

## 关联页面

- [[concepts/概念_ClaudeCode多智能体|Claude Code 多智能体]] — 子智能体机制的总体框架
- [[concepts/概念_Skills与子智能体协作|Skills 与子智能体协作]] — 两种原子模式的深度对比
- [[concepts/概念_Skill系统|Skill 系统]] — Skills 定义与加载机制
- [[concepts/概念_上下文工程|上下文工程]] — 上下文隔离是 Context Engineering 的 Isolate 策略
- [[concepts/概念_Agent编排|Agent 编排]] — 子智能体编排（顺序/并行/流水线/AgentTeam）
- [[entities/项目_ClaudeCode|Claude Code 项目]]