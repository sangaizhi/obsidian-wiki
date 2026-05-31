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
  - "[[concepts/概念_渐进式披露|渐进式披露]]"
  - "[[concepts/概念_Skill触发机制|Skill 触发机制]]"
  - "[[concepts/概念_Skill工程设计|Skill 工程设计]]"
  - "[[concepts/概念_Agent记忆|Agent记忆]]"
  - "[[concepts/概念_Harness工程|Harness Engineering]]"
  - "[[concepts/概念_ClaudeCode多智能体|Claude Code 多智能体]]"
  - "[[concepts/概念_ClaudeCode任务执行机制|Claude Code 任务执行机制]]"
  - "[[comparisons/ClaudeMD_vs_Skills|Claude.md vs Skills]]"
  - "[[entities/插件_Claudian|Claudian 插件]]"
sources:
  - "[[sources/来源_ClaudeCode架构分析|来源：Claude Code 架构分析]]"
  - "[[sources/来源_ClaudeCode多智能体|来源：Claude Code 多智能体]]"
  - "[[sources/来源_ClaudeCode并行后台任务管理|来源：Claude Code 并行后台任务管理]]"
  - "[[sources/来源_ClaudeCode实战_Skills工程实践|来源：Claude Code Skills 工程实践]]"
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

### Sub-agent 与 Agent Teams

Claude Code 的 Sub-agent 用独立上下文执行高信息量或专业化子任务，主会话只接收最终结果。它适合并行代码库探索、长文档分析和工具权限隔离；不适合需要实时共享上下文的紧耦合协作。

Agent Teams 在此基础上加入 Team Lead、Teammates、mailbox、broadcast 和共享任务列表，让多 Agent 协作更接近可调度团队。

### 并行与后台任务执行

Claude Code 的并行执行依赖流式工具块和安全性判断：工具块结束即可启动可执行工具，但是否并发取决于工具是否互相安全。后台任务则通过输出文件、通知和状态查询降低主上下文压力。

## 相关来源

- [[sources/来源_ClaudeCode架构分析|来源：Claude Code 架构分析]] — 原始文章
- [[sources/来源_ClaudeCode多智能体|来源：Claude Code 多智能体]] — Sub-agent、Agent Teams 与协作模式
- [[sources/来源_ClaudeCode并行后台任务管理|来源：Claude Code 并行后台任务管理]] — 并行工具调用、后台执行和任务管理
- [[sources/来源_ClaudeCode实战_Skills工程实践|来源：Claude Code Skills 工程实践]] — Claude.md vs Skills，Skill 工程化定义
- [[concepts/概念_工具调用|工具调用与执行]] — Claude Code 的工具设计哲学
- [[concepts/概念_ClaudeCode多智能体|Claude Code 多智能体]] — 多 Agent 机制拆解
- [[concepts/概念_ClaudeCode任务执行机制|Claude Code 任务执行机制]] — 并行与后台任务机制
- [[entities/项目_OpenClaw|OpenClaw 项目]] — 同类项目对比
- [[entities/项目_HermesAgent|Hermes Agent 项目]] — 同类项目对比
