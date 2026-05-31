---
type: concept
tags:
  - ai
  - agent
  - skill
  - claude-code
  - dynamic-context
  - shell-injection
  - variables
summary: "Skill 通过 $ARGUMENTS 变量和 !`command` 动态 Shell 注入机制，在执行时自动填充上下文数据（分支名、提交记录、文件变更），大幅减少 Agent 的探索性工具调用，需配合 allowed-tools 构建安全围栏防止命令注入。"
sources:
  - "raw/ai/ClaudeCode实战/Chapter3 授人以渔，Skills工程实践.md"
updated: "2026-05-31"
---

# 概念：Skill 动态上下文

## 定义

Skill 的动态上下文能力通过两种机制实现：**$ARGUMENTS 变量**实现参数化，**!`command` 语法**实现运行时 Shell 数据注入。两者结合让 Skill 在激活时自动携带精准的上下文信息，大幅减少 Agent 的探索性工具调用。

## $ARGUMENTS 变量系统

Skill 支持位置参数变量，将用户输入动态嵌入 SKILL.md 正文：

| 变量 | 说明 |
|------|------|
| `$ARGUMENTS` | 所有参数的完整字符串 |
| `$ARGUMENTS[0]` | 第一个参数（索引从 0 开始） |
| `$ARGUMENTS[1]` | 第二个参数 |
| `$0`、`$1`、`$2` | 位置参数的简写形式 |

**示例：** 当用户执行 `/migrate-component SearchBar React Vue` 时，`$ARGUMENTS` = `"SearchBar React Vue"`，可构建为 `Migrate the SearchBar component from React to Vue. Preserve all existing behavior and tests.`

## !`command` 动态 Shell 注入

这是 Skills 系统中最具威力的特性。`!`command`` 语法允许在将 SKILL.md 发送给大模型**之前**，先在 Shell 环境中执行指定命令，并将输出直接内联替换到 Prompt 中。

### 工作机制

```markdown
Current branch:
!`git branch --show-current`

Recent commits:
!`git log origin/main..HEAD --oneline 2>/dev/null || echo "No commits"`

Files changed:
!`git diff --stat origin/main 2>/dev/null || git diff --stat HEAD~3`
```

当用户执行 `/pr-create "Add auth"` 时，大模型实际收到的 Prompt 已经是填充后的：

```
Current branch:
feature/auth

Recent commits:
a1b2c3d Add JWT middleware
d4e5f6g Add login endpoints

Files changed:
src/auth/middleware.ts | 45 +++
src/auth/login.ts     | 82 +++
2 files changed, 127 insertions(+)
```

### 执行顺序

大模型处理 `!`command`` 时遵循严格顺序：**先替换 $ARGUMENTS 变量，再执行 Shell 命令**。

### 效果对比

| 维度 | 未启用 | 使用 `!`command`` |
|------|--------|-------------------|
| 启动时的上下文 | 空白，需多轮对话探索 | 已注入关键信息 |
| 首次响应的工具调用次数 | 3~5 次（Agent 收集信息） | 1~2 次（直接执行行动） |
| Token 消耗 | 高 | 低 |
| 响应速度 | 慢 | 快 |
| 结果一致性 | 低（存在信息遗漏风险） | 高（固定注入相同信息） |

## 安全：Shell 注入风险

由于 `$ARGUMENTS` 会直接拼接到 Shell 命令中，存在命令注入攻击风险。**任何使用 `!`command`` 语法的 Skill，必须通过 `allowed-tools` 配置严格限制可执行的命令范围。**

```yaml
allowed-tools:
  - Bash(git status:*)
  - Bash(git add:*)
  - Bash(git commit:*)
  - Bash(git diff:*)
```

> 安全围栏原则：明确"不能做什么"比定义"能做什么"更能保障系统安全。

## 关联页面

- [[concepts/概念_Skill工程设计|Skill 工程设计]] — 元数据中的 allowed-tools 安全配置
- [[concepts/概念_Skill触发机制|Skill 触发机制]] — 显式调用与 argument-hint
- [[concepts/概念_Skill实战案例|Skill 实战案例]] — 智能提交 Skill 中的完整应用
- [[concepts/概念_Skill系统|Skill 系统]]
