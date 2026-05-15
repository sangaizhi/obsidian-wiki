---
type: source
source: "https://zhuanlan.zhihu.com/p/2014805541709447594"
author: "魔法学院的Chilia（哥伦比亚大学 理学硕士）"
created: 2026-05-15
tags:
  - source
  - 知乎
  - Claude Code
  - 架构
  - SystemPrompt
  - 工具定义
related:
  - "[[entities/项目_ClaudeCode|Claude Code 项目]]"
  - "[[concepts/概念_工具调用|工具调用与执行]]"
  - "[[concepts/概念_上下文工程|上下文工程]]"
  - "[[concepts/概念_Skill系统|Skill 系统]]"
---

# 来源：Claude Code 架构分析

> 通过逆向工程与公开资料，深入分析 Claude Code 的 System Prompt 组织、工具定义与调用设计、CLAUDE.md 注入机制等基础架构。12450 字，创作者「魔法学院的Chilia」。

## 核心洞见

### System Prompt 组织

Claude Code 的 system prompt 不是单一字符串，而是 **动态拼接的多段 text block**，由 110+ 个独立片段文件组成：

```
system: [
  {type: "text", text: "计费头信息..."},         // 可缓存
  {type: "text", text: "角色定义..."},            // 可缓存
  {type: "text", text: "主要 system prompt..."}   // 动态组装
]
```

多 text block 的好处：灵活拼接 + 对缓存友好（前几个 block 不变时可复用缓存）。

### CLAUDE.md 误区澄清

**普遍误区**：CLAUDE.md 被拼接在 system prompt 里。

**实际机制**：CLAUDE.md **从不进入 system prompt**，而是以 `<system-reminder>` 形式在 user message 中动态附加。每次新对话或压缩后重新注入。

原因：system prompt 会话创建时固定，修改则缓存失效；CLAUDE.md 可能动态变更，用 user turn 注入更灵活。

```
User Message 结构：
├── <system-reminder> Skills 列表
├── <system-reminder> CLAUDE.md 拼接内容 + 当前日期
└── 用户真实提问
```

压缩会话时，system-reminder 会重新注入，因此 CLAUDE.md 不会丢失。

### 工具全分类

| 类别 | 工具 |
|------|------|
| Shell 执行 | Bash |
| 文件操作 | Read, Write, Edit, Glob, Grep, NotebookEdit |
| 子 Agent | Agent |
| 用户交互 | AskUserQuestion |
| 计划模式 | EnterPlanMode, ExitPlanMode |
| 定时任务 | CronCreate, CronDelete, CronList |
| 任务管理 | TaskCreate, TaskGet, TaskUpdate, TaskList, TaskOutput, TaskStop |
| 网页相关 | WebFetch, WebSearch |
| Skill | Skill |
| Worktree | EnterWorktree, ExitWorktree |

### 工具设计哲学：高/中/低层搭配

核心原则：**使用频率 × 成功率** 的权衡。

- 已有通用 `Bash` 工具，但仍单独实现 `Grep` → 搜索是高频动作，Bash 调用 grep 容易记错参数格式
- 已有 `Bash`，但仍单独实现 `WebFetch` → 封装 curl/wget/解析/编码等复杂步骤

> "你要给 Agent 提供与其能力形状匹配的工具。那你怎么知道它到底擅长什么？去观察、读它的输出、做实验。你要学会'像 Agent 一样看问题'。" — Thariq (Anthropic)

### 渐进式信息披露

工具调用的渐进式设计：不一次性给模型所有信息，而是提供搜索工具让模型自主决定探索路径——更接近人类开发者的工作方式。

## 相关页面

- [[entities/项目_ClaudeCode|Claude Code 项目]] — 项目实体详情
- [[concepts/概念_工具调用|工具调用与执行]] — 工具全分类与设计哲学
- [[concepts/概念_上下文工程|上下文工程]] — System Prompt 组织与 CLAUDE.md 注入
- [[concepts/概念_Agent记忆|Agent记忆]] — Auto Memory 系统设计
