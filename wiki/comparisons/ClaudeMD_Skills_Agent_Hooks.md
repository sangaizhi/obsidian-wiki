---
type: comparison
tags:
  - ai
  - agent
  - claude-code
  - claude-md
  - skill
  - hooks
summary: "Claude.md、Skills、Agent 和 Hooks 是 Claude Code 中四种不同层级的控制方式：常驻规范、按需工作流、任务委派、执行层强制门禁。"
sources:
  - "raw/ai/ClaudeCode实战/Chapter3 授人以渔：Skills工程实践.md"
  - "raw/ai/ClaudeCode实战/Chapter4 分而治之：子智能体与任务委派.md"
  - "raw/ai/ClaudeCode实战/Chapter5 防微杜渐：Hook事件驱动自动化.md"
created: "2026-06-23"
updated: "2026-06-23"
---

# 比较：Claude.md vs Skills vs Agent vs Hooks

## 一句话结论

Claude.md 定义长期常驻规范，Skills 封装按需工作流，Agent/Sub-agent 负责上下文隔离与任务委派，Hooks 在系统执行层强制拦截和自动化治理。四者不是替代关系，而是从“建议”到“指导”再到“委派”和“强制”的控制层级递进。

## 对比表

| 机制 | 工作层面 | 触发方式 | 主要作用 | 约束强度 |
|------|----------|----------|----------|----------|
| Claude.md | 认知层 | 会话常驻加载 | 项目规范、偏好、长期约束 | 软建议 |
| Skills | 认知层 | 显式调用 / 语义匹配 | 领域 SOP、可复用工作流 | 过程指导 |
| Agent / Sub-agent | 认知层 + 上下文隔离 | 任务委派 | 专业分工、并行探索、隔离上下文 | 分工控制 |
| Hooks | 系统执行层 | 事件自动触发 | 拦截、改写、审计、质量门控 | 硬约束 |

## 选型原则

- 项目通用规范放进 Claude.md，避免每次重复说明。
- 可复用且有步骤的专业流程做成 Skill。
- 信息量大、上下文污染重或可并行探索的任务交给 Sub-agent。
- 有安全风险、质量门槛、审计要求或环境初始化需求的横切逻辑交给 Hooks。

## 组合方式

成熟的 Claude Code 项目通常四者同时存在：

1. Claude.md 定义项目边界和编码偏好。
2. Skills 提供具体任务的操作手册。
3. Sub-agent 承接独立探索和专业任务。
4. Hooks 在关键事件点兜底，确保危险动作被阻断、质量检查被执行、执行记录可追踪。

## 关联页面

- [[comparisons/ClaudeMD_vs_Skills|Claude.md vs Skills]]
- [[concepts/概念_Skill系统|Skill 系统]]
- [[concepts/概念_ClaudeCode多智能体|Claude Code 多智能体]]
- [[concepts/概念_ClaudeCodeHooks|Claude Code Hooks]]
- [[concepts/概念_Harness工程|Harness Engineering]]
- [[sources/来源_ClaudeCode实战_Skills工程实践|来源：Claude Code Skills 工程实践]]
- [[sources/来源_ClaudeCode实战_Hook事件驱动自动化|来源：Claude Code Hook 事件驱动自动化]]
- [[entities/项目_ClaudeCode|Claude Code 项目]]
