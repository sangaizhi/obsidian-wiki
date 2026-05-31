---
type: concept
tags:
  - ai
  - agent
  - skill
  - claude-code
  - engineering
  - directory-structure
  - frontmatter
summary: "Skill 的工程设计涵盖目录规范（kebab-case + SKILL.md 大写）、元数据三维度（触发/权限/运行时）、SKILL.md 正文的正确定位（路由器思维、契约式引用、500行法则）。"
sources:
  - "raw/ai/ClaudeCode实战/Chapter 3授人以渔，Skills工程实践.md"
updated: "2026-05-25"
---

# 概念：Skill 工程设计

## 定义

Skill 工程设计关注 Skill 的工程化规范——从目录结构、元数据定义到 SKILL.md 正文的设计哲学，确保 Skill 在不同环境、不同工具链中都能被正确识别和高效执行。

## 目录结构规范

一个标准 Skill 的工程化目录：

```
my-skill/
├── SKILL.md          # 【核心骨架】必需。文件名必须全大写，精确匹配
├── scripts/          # 【手脚】可选。可执行脚本，让 Skill 能主动"做事"
│   └── analyze.py
├── reference/        # 【记忆库】可选。按需加载的参考文档
│   └── patterns.md
└── templates/        # 【模具】可选。输出模板，确保格式统一
    └── report.md
```

### 命名规范

| 元素 | 规范 | 说明 |
|------|------|------|
| **目录名** | kebab-case（全小写 + 短横线 + 数字） | 最多 64 字符，禁止空格/下划线/大写/首尾横线/连续横线 |
| **SKILL.md** | 必须全大写 | `skill.md`、`Skill.md`、`SKILL.MD` 均无效，加载器做精确字符串匹配 |
| **禁止 README.md** | 不可放在 Skill 目录内 | 部分 Agent 会读取目录下所有 Markdown，引入噪音。README 放在父目录 |

> 命名规范的主要目的是抹平操作系统差异，保证 Skill 在任何环境、任何工具链中都能无损识别。

## 前置元数据（Frontmatter）

SKILL.md 顶部的 YAML 定义了 Skill 的身份标识与行为边界，分为三大维度：

### 一、触发机制

| 字段 | 说明 | 长度限制 |
|------|------|---------|
| `name` | 唯一标识符，建议与目录名一致，省略则用目录名 | 最多 64 字符 |
| `description` | 触发描述，Agent 决策调用的唯一依据 | 最多 1024 字符。省略则用 SKILL.md 正文第一段 |
| `argument-hint` | 参数提示，在 `/` 菜单中显示 | — |

### 二、权限控制

| 字段 | 说明 | 默认值 |
|------|------|--------|
| `disable-model-invocation` | 禁止 Agent 自动调用，需手动 `/skill-name` | `false` |
| `user-invocable` | 是否在 `/` 菜单显示，设为 `false` 则隐藏但 Agent 仍可自动调用 | `true` |
| `allowed-tools` | 工具白名单，精确控制 Skill 可用的工具及权限范围 | — |
| `model` | 指定执行模型，简单任务建议用低成本快速模型 | — |

### 三、运行时环境

| 字段 | 说明 |
|------|------|
| `context` | 设为 `fork` 时，在隔离子智能体中执行，不污染对话上下文 |
| `agent` | 子智能体类型，当 `context=fork` 时生效，可选自定义 Agent |
| `hooks` | 生命周期事件钩子，仅在 Skill 激活状态下生效（如 `PreToolUse`） |

### 完整示例

```yaml
---
name: alert-log-analysis
description: Use when receiving alerts via WeWorkChat that require automated root-cause analysis
argument-hint: "[namespace] [output format]"
disable-model-invocation: true
user-invocable: false
allowed-tools:
  - Read
  - Grep
  - Glob
  - Write
  - Bash(python:*)
model: deepseek-v4-flash
context: fork
agent: Explore
hooks:
  PreToolUse:
    - matcher: Bash
      hooks:
        - commands: echo "$TOOL_INPUT" >> audit.log
---
```

## SKILL.md 正文设计

### 路由器思维

**SKILL.md 是路由器，不是知识仓库。** 正文只需包含核心流程与路由表，详细知识分散存储于被引用文件。

> 一个常见误区：将所有信息写进 SKILL.md 正文。正确做法是将其定位为路由器——文件自身仅含核心流程与路由表，详情存于 reference/、templates/、scripts/。

**核心技巧：构建"快速参考表"**——以极低 Token 向 Agent 清晰指引关键维度的路由条件：

```markdown
## 快速参考

| 分析类型                  | 触发关键词          | 参考资源                      |
| ------------------------- | ------------------- | ----------------------------- |
| 收入分析（Revenue）        | 收入、营收、销售额   | reference/revenue.md          |
| 成本分析（Cost）           | 成本、费用、支出     | reference/costs.md            |
| 盈利分析（Profitability）  | 利润、毛利率、净利率 | reference/profitability.md    |
```

结构化表格比逐行扫描正文的匹配效率更高。

### 契约式引用

引用辅助文件时，不能只罗列路径。必须建立"契约"——明确三个核心要素：

| 要素 | 含义 | 反例 |
|------|------|------|
| **触发时机**（何时加载） | 在什么条件下引用 | 弱引用：`参考 reference/revenue.md 获取详细内容` |
| **资源位置**（去哪儿找） | 精确的文件路径 | — |
| **预期产出**（获取何物） | 文件内容能提供什么 | — |

**契约式引用范式：**

```markdown
## 收入分析
当用户询问关于收入增长、平均收入或者收入组成时：
参考 `reference/revenue.md` 获取计算公式和行业标准。
```

> 这一设计与子智能体流水线中的"交接契约"一脉相承：下游消费者不仅需要知道上游的位置，更必须明确上游能提供什么。

### 500行法则

**为什么是 500 行？** 约 2000~3000 Tokens，是单个 Skill 激活后合理的上下文开销。与 System Prompt 及会话历史累加后，总 Token 数仍可维持可控。

超过 500 行的重构对策：

| 重构信号 | 对策 |
|----------|------|
| 大段公式或规范说明 | 移至 `reference/` 目录 |
| 多个完整示例（单个超过 30 行） | 移至 `examples/` 目录 |
| 多个输出模板 | 移至 `templates/` 目录 |
| 可独立执行的逻辑 | 封装为 `scripts/` 脚本 |
| 多个平行的功能模块 | 拆分为多个独立 Skill |

> 超过 500 行 = "参考资料"和"路由指令"混淆了，需要立即重构。

## 作用域与优先级

Skill 文件可部署在不同层级，每层对应特定的生效范围：

| 位置 | 生效范围 | 用途 |
|------|---------|------|
| 企业配置中心 | 全员生效 | 强制执行的企业级开发规范与安全策略 |
| `~/.claude/skills/<name>/` | 个人所有项目 | 个人编码习惯、通用工具集、跨项目辅助脚本 |
| `<project>/.claude/skills/<name>/` | 仅限当前项目 | 项目特有的工作流、业务逻辑定制、团队协作规范 |
| Plugin 内置资源 | Plugin 启用时 | 社区共享的能力包、特定框架的专用指令集 |

### 优先级

```
企业策略 > 个人配置（~/.claude/） > 项目配置（.claude/） > Plugin 内置
```

- **企业策略**：最高权限，强制执行全局安全与合规策略
- **个人配置**：满足开发者的个人习惯
- **项目配置**：专为特定项目服务

### 版本控制

将 Skill 目录纳入项目版本控制，是实现**团队知识零成本共享**的最优解：
- **即插即用**：团队成员克隆项目后，相关 Skill 自动生效
- **同步演进**：Skill 跟随代码一同更新

## 关联页面

- [[concepts/概念_Skill触发机制|Skill 触发机制]] — 元数据如何驱动触发
- [[concepts/概念_渐进式披露|渐进式披露]] — 三层加载模型的设计基础
- [[concepts/概念_Skill系统|Skill 系统]] — Skill 完整体系
- [[comparisons/ClaudeMD_vs_Skills|Claude.md vs Skills]] — Skill 在 Claude Code 知识体系中的定位
