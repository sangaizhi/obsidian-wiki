---
type: concept
tags:
  - ClaudeCode
  - 工具调用
  - 并行执行
  - 后台任务
summary: "Claude Code 任务执行机制通过流式工具并行、并发安全调度、后台输出文件和任务通知降低 Agent 执行延迟并保持可追踪。"
sources:
  - "raw/知乎/2026-05-15/Claude Code的并行、后台执行、任务管理与一些误区.md"
created: "2026-05-17"
updated: "2026-05-17"
---

# 概念：Claude Code 任务执行机制

## 定义

Claude Code 任务执行机制描述工具调用如何并行启动、如何判断并发安全、如何后台运行，以及如何把任务状态和输出重新送回模型。

## 流式工具并行

Claude Code 不等模型整条消息输出完才执行工具，而是在 Anthropic SSE 流中监听 `content_block_stop`。当某个工具调用的 JSON 参数完整后，就立即提交执行。这样工具执行时间可与模型继续生成后续 token 的时间重叠。

## 并发安全调度

并发安全不是按工具类型静态决定，而是由 `isConcurrencySafe(input)` 按具体输入判断。Read、Glob、Grep 等只读工具通常可并行；写文件、修改状态或有副作用的 Bash 命令需要独占执行。

## 后台执行

并发决定“一次启动几个”，后台决定“是否阻塞主对话”。后台任务可能来自显式 `run_in_background: true`、前台超时转后台，或用户快捷键把前台任务转后台。

## 输出与通知

- Bash 后台任务将 stdout/stderr 实时追加到 `.output` 文件。
- Sub-agent 后台任务的 `.output` 可指向 JSONL 会话记录。
- 任务完成时 `<task-notification>` 会携带 task-id、output-file、status 和 summary。
- 新实践倾向于直接 Read output 文件，而不是依赖已废弃的 TaskOutput。

## 关联页面

- [[entities/项目_ClaudeCode|Claude Code 项目]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_ClaudeCode多智能体|Claude Code 多智能体]]
- [[concepts/概念_Agent编排|Agent 编排]]
- [[sources/来源_ClaudeCode并行后台任务管理|来源：Claude Code 并行后台任务管理]]

