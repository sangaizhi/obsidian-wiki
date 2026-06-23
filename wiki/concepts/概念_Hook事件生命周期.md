---
type: concept
tags:
  - ai
  - agent
  - claude-code
  - hooks
  - lifecycle
  - automation
summary: "Hook 事件生命周期把 Claude Code 运行过程拆成会话、工具调用、子智能体、完成和团队协作事件，提供可编程控制点。"
sources:
  - "raw/ai/ClaudeCode实战/Chapter5 防微杜渐：Hook事件驱动自动化.md"
created: "2026-06-23"
updated: "2026-06-23"
---

# 概念：Hook 事件生命周期

## 定义

Hook 事件生命周期是 Claude Code 在运行过程中暴露的一组事件控制点。它覆盖会话启动与结束、工具调用前后、权限请求、用户输入、子智能体启动与停止、响应完成、通知、多智能体团队协作和 Git worktree 生命周期。

## 事件分组

### 会话级事件

| 事件 | 触发时机 | 主要用途 |
|------|----------|----------|
| `SessionStart` | 会话启动或恢复 | 初始化环境变量、准备工作区 |
| `SessionEnd` | 会话终止 | 清理临时资源、记录统计信息 |
| `PreCompact` | 上下文压缩前 | 备份完整对话、保留可恢复记录 |

### 工具调用事件

| 事件 | 触发时机 | 主要用途 |
|------|----------|----------|
| `PreToolUse` | 工具执行前 | allow / deny / updateInput，执行前安全门控 |
| `PostToolUse` | 工具成功后 | 追加上下文、格式化、lint、替换 MCP 输出 |
| `PostToolUseFailure` | 工具失败后 | 告警、错误归因、纠正性反馈 |
| `PermissionRequest` | 权限对话框弹出前 | 自动批准或拒绝权限申请 |
| `UserPromptSubmit` | 用户输入提交后、Claude 处理前 | 输入预处理、上下文注入 |

### 子智能体事件

| 事件 | 触发时机 | 主要用途 |
|------|----------|----------|
| `SubagentStart` | 子智能体启动 | 按子智能体类型注入规范或上下文 |
| `SubagentStop` | 子智能体完成 | 读取主会话与子会话 transcript，评估输出质量 |

### 完成与通知事件

| 事件 | 触发时机 | 主要用途 |
|------|----------|----------|
| `Stop` | Claude 完成整轮响应 | 质量门控，必要时阻止结束并要求继续修正 |
| `Notification` | 系统通知出现 | 自定义权限、空闲、认证等通知渠道 |

### 新增团队与工作区事件

| 事件 | 触发时机 | 主要用途 |
|------|----------|----------|
| `TeammateIdle` | 队友智能体即将空闲 | 多智能体团队调度 |
| `TaskCompleted` | 任务被标记完成 | 任务审计与收尾 |
| `ConfigChange` | 配置文件变更 | 配置审计与合规追踪 |
| `WorktreeCreate` | Git worktree 创建 | 自动安装依赖、初始化分支环境 |
| `WorktreeRemove` | Git worktree 删除 | 清理临时构建产物或缓存 |

## 设计要点

- 执行前事件适合做阻断和改写，执行后事件适合做反馈和审计。
- `PreToolUse` 每次工具调用前都会触发，`PermissionRequest` 只在需要权限确认时触发。
- `SubagentStop` 能拿到主会话和子会话 transcript，是复盘子智能体质量的关键入口。
- `Stop` 是质量门控入口，适合把“响应是否达标”从模型自评变成外部检查。

## 关联页面

- [[concepts/概念_ClaudeCodeHooks|Claude Code Hooks]]
- [[concepts/概念_ClaudeCode任务执行机制|Claude Code 任务执行机制]]
- [[concepts/概念_ClaudeCode多智能体|Claude Code 多智能体]]
- [[concepts/概念_Agent编排|Agent 编排]]
- [[sources/来源_ClaudeCode实战_Hook事件驱动自动化|来源：Claude Code Hook 事件驱动自动化]]
