---
type: source
source: "https://zhuanlan.zhihu.com/p/2015365442928140835"
author: "SmartCode得物技术"
created: 2026-05-14
tags:
  - source
  - 知乎
  - SpecCoding
  - Claude Code
  - 实践
related:
  - "[[concepts/概念_SpecCoding|Spec Coding]]"
  - "[[concepts/概念_Harness工程|Harness Engineering]]"
  - "[[concepts/概念_Skill系统|Skill 系统]]"
  - "[[entities/插件_Claudian|Claudian 插件]]"
---

# 来源：Spec Coding 实战

> 得物技术团队基于 Claude Code 的 Spec Coding（规格驱动编码）深度实战复盘。10 天、2.5 万行代码、2,754 次工具调用，从 0 到 1 完成企业级中后台项目。

## 核心数据

| 指标 | 数值 |
|------|------|
| 项目周期 | 10 天（4 个阶段） |
| 生成代码 | 25,546 行 |
| 工具调用 | 2,754 次 |
| 指令数量 | 217 条 |
| 会话文件 | 109 个 .jsonl |
| 规范文件 | 7 条 CLAUDE.md rules |
| 代码一致性 | 205 个文件高度一致 |

## 关键洞见

### Spec Coding 工作流

在写代码之前先写规格文档，每个功能变更经历：Proposal → Design → Specs → Tasks → Code 五个阶段。核心价值在于减少返工、可审计、让 AI 聚焦当前步骤。

### 三层规范体系

1. **约束层（.claude/rules/）**：告诉 AI「禁止什么、必须怎样」— 7 个规范文件（TS、命名、注释、Lint、样式、页面结构、API 接口）
2. **示范层（.claude/code-design/）**：告诉 AI「标准产出长什么样」— 6 组标准代码模板（pro-table、pro-form、drawer 等）
3. **视觉层（.claude/ui-design/）**：告诉 AI「页面应该长什么样」— HTML 设计稿可直接预览

### MCP 消除信息断层

- 接口文档直连 MCP：21 次调用读取 39 个 API 定义
- 飞书云文档直读 MCP：直接读取 PRD 文档
- 效果：6 个接口零联调返工，单日交付 3 个完整模块（提效 3 倍）

### AI 失效模式

- **规范真空**：没有规范时 AI 产生不一致代码
- **信息孤岛**：缺少接口文档、设计稿等上下文时 AI 频繁猜测
- **任务目标模糊**：复杂任务没有分步规格时 AI 偏离方向
- **隐性行为**：依赖内部源码的运行时行为超出 AI 推断能力

### 复杂排障案例

构建环境问题 4 小时排查揭示 AI 边界：AI 每次分析都正确，但问题有多根因互相掩盖、隐性行为无文档、本地无法复现等结构性特征，超出 AI 的信息范围和反馈机制。

## 相关概念

- [[concepts/概念_SpecCoding|Spec Coding]] — 规格驱动编码方法论
- [[concepts/概念_Harness工程|Harness Engineering]] — 规范体系属于 Harness 的控制面设计
- [[concepts/概念_Skill系统|Skill 系统]] — 示范层可演化为 Skill
- [[entities/项目_OpenClaw|OpenClaw 项目]] — 对比参考
