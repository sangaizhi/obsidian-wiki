---
type: entity
entity: 项目
name: Claude Code
created: 2026-05-15
tags:
  - entity
  - project
  - Claude Code
  - Agent框架
  - Anthropic
related:
  - "[[concepts/概念_工具调用|工具调用与执行]]"
  - "[[concepts/概念_上下文工程|上下文工程]]"
  - "[[concepts/概念_Skill系统|Skill 系统]]"
  - "[[concepts/概念_Agent记忆|Agent记忆]]"
  - "[[concepts/概念_Harness工程|Harness Engineering]]"
  - "[[entities/插件_Claudian|Claudian 插件]]"
sources:
  - "[[sources/来源_ClaudeCode架构分析|来源：Claude Code 架构分析]]"
---

# Claude Code

> Anthropic 官方 CLI 编程 Agent，当前 Vibe Coding 界用户体感最舒服的框架之一。v2.1.76 版本分析。

## 架构特色

### System Prompt 动态组装

由 110+ 个独立片段文件根据运行时环境动态组合，使用 Anthropic Messages API 的多 text block 格式，对缓存友好。

### CLAUDE.md 注入机制

- 三级层次：用户级（`~/.claude/`）→ 项目级（仓库根目录）→ 模块级（子目录）
- **不在 system prompt 中**，而是以 `<system-reminder>` 形式在 user message 中动态注入
- 压缩会话后重新注入，不会丢失

### 工具系统

10 大类工具，遵循高/中/低层合理搭配原则：

| 层次 | 示例 | 设计目的 |
|------|------|---------|
| **高层** | WebFetch, Agent, Skill | 封装复杂步骤，模型只需提供输入 |
| **中层** | Read, Write, Edit, Glob, Grep | 高频文件操作，比 Bash 更稳定 |
| **低层** | Bash | 万能工具，覆盖无专用工具的场景 |

### Auto Memory 系统

- 持久存储位置：`memory/` 目录，跨会话保留
- MEMORY.md 保持 ≤200 行，可链接到详细文件
- 保存：稳定模式、架构决策、问题解决方案
- 不保存：临时信息、未核实猜测

## 相关来源

- [[sources/来源_ClaudeCode架构分析|来源：Claude Code 架构分析]] — 原始文章
- [[concepts/概念_工具调用|工具调用与执行]] — Claude Code 的工具设计哲学
- [[entities/项目_OpenClaw|OpenClaw 项目]] — 同类项目对比
- [[entities/项目_HermesAgent|Hermes Agent 项目]] — 同类项目对比
