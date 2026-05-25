---
type: comparison
tags:
  - ai
  - agent
  - workflow
  - comparison
summary: "工作流适合固定流程，Agent 适合动态决策；实践中应先工作流后 Agent，避免过度设计。"
sources:
  - "raw/ai/agent/8.工作流 vs Agent.md"
  - "raw/ai/agent/7.Agent的核心：规划能力.md"
updated: "2026-05-12"
---

# 工作流 vs Agent

## 一句话结论

固定流程优先工作流，动态决策再引入 Agent。非必要，不 Agent。

## 对比表

| 维度 | 工作流 | Agent |
| --- | --- | --- |
| 定位 | 固定流水线 | 智能决策者 |
| 执行逻辑 | 步骤预设，线性稳定 | 根据目标、环境和反馈动态调整 |
| 优势 | 稳定、可控、成本低、容易观测 | 适应不确定需求，能处理非结构化问题 |
| 风险 | 覆盖不了长尾变化 | 成本更高，不确定性更强，调试更复杂 |
| 适用场景 | 自动周报、固定审批、定时同步 | 电商客服、复杂问答、动态工具调用 |

## 选择法则

- 标准化、可固化的业务逻辑，优先使用工作流。
- 用户需求多变、流程无法提前穷举时，引入 Agent。
- 先用工作流覆盖 80% 常规需求，再为长尾场景渐进叠加 Agent。
- 不要为了“更 AI”而把固定流程强行改造成 Agent。

## 关联页面

- [[concepts/概念_AI_Agent|AI Agent]]
- [[concepts/概念_Agent规划能力|Agent 规划能力]]
- [[sources/来源_工作流_vs_Agent|来源：工作流 vs Agent]]

