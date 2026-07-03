---
type: concept
tags:
  - ai
  - agent
  - claude-code
  - hooks
  - guardrails
  - runtime
summary: "Claude Code Hooks 是系统执行层的事件拦截机制，用于在会话、工具调用、子智能体和完成阶段强制执行安全、质量和自动化策略。"
sources:
  - "raw/ai/ClaudeCode实战/Chapter5 防微杜渐：Hook事件驱动自动化.md"
created: "2026-06-23"
updated: "2026-07-03"
---

# 概念：Claude Code Hooks

## 定义

Claude Code Hooks 是围绕 Claude Code 运行时事件的自动化拦截机制。它不再依赖 Prompt 劝说模型遵守规则，而是在系统执行层对会话、工具调用、子智能体和完成动作进行拦截、改写、补充或审计。

一句话：Claude.md 和 Skills 负责“告诉 Claude 应该怎么做”，Hooks 负责“在系统层面保证不能乱做”。

## 核心定位

Hooks 与 Claude.md、Skills、Agent 的差异在于控制层级：

- **Claude.md**：常驻项目规范，属于认知层建议。
- **Skills**：按需加载的领域工作流，属于认知层指导。
- **Agent / Sub-agent**：通过上下文隔离完成任务委派。
- **Hooks**：系统事件自动触发，具备执行前拦截和执行后反馈能力。

因此 Hooks 是 [[concepts/概念_Harness工程|Harness Engineering]] 中 Guardrails 和运行时控制的具体落点。

## 典型能力

### 执行前拦截

`PreToolUse` 在工具实际执行前触发，可直接允许、拒绝或改写工具输入。例如把危险命令改成 dry-run，或者阻止删除根目录、泄露密钥、上传 `.env` 等高风险行为。

### 执行后反馈

`PostToolUse` 在工具执行成功后触发，可通过 `additionalContext` 把 lint、格式化、审计结果反馈给 Claude，也可以对 MCP 工具输出做替换。

### 权限自动化

`PermissionRequest` 只在需要用户授权时触发，可根据策略自动批准或拒绝权限请求。它比 `PreToolUse` 更接近权限对话框层。

### 质量门控

`Stop` 在 Claude 完成响应时触发，可在输出不符合质量标准时阻止结束，强制继续修正。

### 上下文注入

`SessionStart` 可通过 `CLAUDE_ENV_FILE` 注入环境变量；`UserPromptSubmit` 可在用户输入提交后补充当前 Git 分支、环境状态或团队规范。

## 工程价值

- **安全**：把危险操作从“不要做”的软提示变成“不能做”的硬门禁。
- **一致性**：把格式化、lint、审计、环境初始化变成自动执行的固定机制。
- **可追溯**：ConfigChange、SessionEnd、SubagentStop 等事件可以记录配置变化和执行轨迹。
- **多智能体治理**：SubagentStart/Stop、TeammateIdle、TaskCompleted 让团队式 Agent 运行可被观察和管理。

## 三种处理器类型

| 类型 | 确定性 | 适用场景 | 说明 |
|------|--------|---------|------|
| **command** | 最高 | 确定性规则 | Shell 脚本执行，通过退出码决策（0=放行，2=阻止，其他=异常） |
| **prompt** | 中 | 语义判断 | 调用小模型（Haiku）单轮评估，遵循 JSON 输出格式（`{"ok": true/false}`） |
| **agent** | 最低 | 多轮验证 | 启动子智能体深度分析，最多 50 轮 |

选择原则：**能用 command 不用 prompt，能用 prompt 不用 agent**。确定性规则在速度和可靠性上永远优于大模型判断。

## 输出协议

Hook 通过 `hookSpecificOutput` 与 Claude 交互：

- `permissionDecision`：allow（绕过）/ deny（阻止）/ ask（交由用户）
- `additionalContext`：注入 Claude 上下文，构建自动反馈闭环
- `continue=false`：紧急制动，终止所有处理
- `updateInput`：PreToolUse 专属，静默修改工具参数（如危险命令加 --dry-run）

## 子智能体 Hooks

Hooks 可以通过三种方式与子智能体集成：

- **Frontmatter Hook**：在子智能体 `.md` 文件的 YAML 中定义，生命周期与子智能体绑定，精度高于全局配置
- **SubagentStart**：子智能体启动时注入上下文规范
- **SubagentStop**：读取 `agent_transcript_path` 复盘子智能体完整工作记录，验证输出质量

## 异步 Hooks

通过 `"async": true` 实现后台非阻塞执行：

- 仅 `command` 类型支持异步
- 异步 Hook **无法阻止操作**，适合日志记录、通知、后台验证
- 结果在下一轮对话中传递给 Claude

## 工程设计"三步走"

1. **全量审计**：先配置 PostToolUse + matcher:"*" 的审计日志，观察工具调用模式
2. **针对性拦截**：基于数据识别高风险操作，设计 PreToolUse 规则
3. **逐步收紧**：始终保留日志，确保误拦截可快速定位

## 使用边界

Hooks 适合处理确定性、安全性、审计性要求高的横切关注点；不适合承载大段业务知识。业务知识仍应放在 [[concepts/概念_Skill系统|Skill 系统]] 或项目文档中，Hooks 只负责在关键事件点执行可验证动作。

## 关联页面

- [[concepts/概念_Hook事件生命周期|Hook 事件生命周期]]
- [[concepts/概念_Harness工程|Harness Engineering]]
- [[concepts/概念_ClaudeCode任务执行机制|Claude Code 任务执行机制]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_Skill系统|Skill 系统]]
- [[comparisons/ClaudeMD_Skills_Agent_Hooks|Claude.md vs Skills vs Agent vs Hooks]]
- [[sources/来源_ClaudeCode实战_Hook事件驱动自动化|来源：Claude Code Hook 事件驱动自动化]]
- [[entities/项目_ClaudeCode|Claude Code 项目]]
