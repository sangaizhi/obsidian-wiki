---
type: comparison
tags:
  - ai
  - agent
  - claude-code
  - skill
  - claude.md
  - comparison
summary: "Claude.md 与 Skills 是 Claude Code 中两种互补的知识维度：Claude.md 承载常驻通用规则，Skills 承载按需专业领域知识。"
sources:
  - "raw/ai/ClaudeCode实战/Chapter3 授人以渔：Skills工程实践.md"
updated: "2026-05-25"
---

# 比较：Claude.md vs Skills

## 比较对象简介

- **Claude.md**：项目级配置文件，定义项目的通用规则（编码语言、缩进风格、命名规范等），每次对话全量加载，所有 Agent 共享。
- **Skills**：专业领域知识模块，按需激活，可跨项目、跨会话使用，特定 Agent 可绑定特定 Skill。

## 相同点

- 都是 Claude Code 的指令/知识载体
- 都影响 Agent 的行为和输出质量
- 都需要精心维护才能发挥最大效果

## 不同点

| 维度 | Claude.md | Skills |
|------|-----------|--------|
| **知识类型** | 常驻知识（通用规则） | 按需知识（专业领域） |
| **加载策略** | 每次全量加载 | 渐进式按需加载 |
| **生效范围** | 当前项目 | 可跨项目、跨会话 |
| **触发方式** | 任何对话自动生效 | 按需激活（Description = Router） |
| **Token 成本** | 固定开销，不论是否用到 | 按需支付，用多少付多少 |
| **典型用途** | "使用 Java 语言"、"缩进 2 空格" | 代码审查、日志诊断、日报生成 |
| **与 Agent 关系** | 所有 Agent 共享 | 可绑定特定 Agent |
| **企业本体论类比** | 企业规章制度（考勤、安全红线） | SOP 操作手册（特定岗位特定任务） |
| **修改频度** | 低频变更 | 高频迭代（Append-Mostly 维护） |
| **设计哲学** | 约束（constrain） | 教（teach），内化领域逻辑 |

## 选择建议

1. **优先 Claude.md** — 项目级、所有 Agent 都必须遵守的规则放这里。规则数量应精简，避免膨胀。
2. **专业知识用 Skills** — 特定场景、需要复杂步骤的流程封装为 Skill，按需加载，避免全量 Token 浪费。
3. **两者互补而非替代** — Claude.md 打基底，Skills 做扩展。好的项目中两者配合使用。
4. **企业落地** — 类比企业知识管理：规章制度（Claude.md）必须有，但不能太多；SOP（Skills）越丰富越强大，但需要做分层加载和 Gating。

## 关联页面

- [[concepts/概念_渐进式披露|渐进式披露]] — Skill 的三层加载模型与预算机制
- [[concepts/概念_Skill触发机制|Skill 触发机制]] — 双通道激活与 description 设计
- [[concepts/概念_Skill工程设计|Skill 工程设计]] — 目录规范与正文设计原则
- [[sources/来源_ClaudeCode实战_Skills工程实践|来源：Claude Code Skills 工程实践]] — 本章原始内容
- [[concepts/概念_Skill系统|Skill 系统]] — Skill 系统的完整概念页
- [[entities/项目_ClaudeCode|Claude Code 项目]] — 相关项目实体
- [[concepts/概念_上下文工程|上下文工程]] — 知识加载与上下文管理
