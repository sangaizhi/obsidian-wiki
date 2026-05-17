---
type: source
source: "https://zhuanlan.zhihu.com/p/2021181226682253905"
author: "魔法学院的Chilia"
created: 2026-05-17
tags:
  - source
  - 知乎
  - ClaudeCode
  - 并行执行
  - 后台任务
related:
  - "[[entities/项目_ClaudeCode|Claude Code 项目]]"
  - "[[concepts/概念_ClaudeCode任务执行机制|Claude Code 任务执行机制]]"
  - "[[concepts/概念_工具调用|工具调用与执行]]"
sources:
  - "raw/知乎/2026-05-15/Claude Code的并行、后台执行、任务管理与一些误区.md"
---

# 来源：Claude Code 并行后台任务管理

## 来源信息

- 原始文件：`raw/知乎/2026-05-15/Claude Code的并行、后台执行、任务管理与一些误区.md`
- 类型：知乎文章 / Claude Code 运行机制分析
- 主题：并行工具执行、后台执行、任务状态和日志读取

## 核心要点

- 单消息多工具并行允许 Agent 在一条 assistant message 中发出多个 tool call，并发执行但按原始顺序返回结果。
- Claude Code 的 StreamingToolExecutor 在流式输出中监听 `content_block_stop`，工具参数完整后立即启动执行，隐藏工具延迟。
- 并发安全由 `isConcurrencySafe(input)` 动态判断：只读工具可并行，写操作需要独占执行。
- 后台执行和并发执行是两个独立维度：并发决定数量，后台决定是否阻塞主对话。
- 后台任务输出会写入 `.output` 文件；Sub-agent 后台任务的 `.output` 可指向 JSONL 会话记录符号链接。
- `<task-notification>` 在后台任务完成时通知模型，避免无意义轮询。
- Claude Code 中有两类 Task：UI 层 Todo 管理工具，以及真正管理后台命令/Sub-agent 的后台任务工具。

## 关联页面

- [[entities/项目_ClaudeCode|Claude Code 项目]]
- [[concepts/概念_ClaudeCode任务执行机制|Claude Code 任务执行机制]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_Agent编排|Agent 编排]]

