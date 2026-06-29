---
type: concept
tags:
  - ClaudeCode
  - MultiAgent
  - SubAgent
  - Agent编排
  - 子智能体
  - task-delegation
summary: "Claude Code 多智能体以 Sub-agent 和实验性 Agent Teams 为核心，通过子智能体的独立上下文窗口、受限工具权限、任务委派和 Skills 协作，将复杂任务分而治之。"
sources:
  - "raw/知乎/2026-05-15/从Claude Code入手看Agent框架设计思路：多智能体(Multi-Agent).md"
  - "raw/ai/ClaudeCode实战/Chapter4 分而治之：子智能体与任务委派.md"
created: "2026-05-17"
updated: "2026-06-29"
---

# 概念：Claude Code 多智能体

## 定义

Claude Code 多智能体是 Claude Code 中用 Sub-agent 和 Agent Teams 处理复杂任务的架构模式。**子智能体的本质**：一个具备独立上下文窗口、受限工具权限及明确任务范围的大模型实例。不是简单增加 Agent 数量，而是通过上下文隔离、角色分工、工具权限和结果汇总来控制复杂度。

---

## 子智能体（Sub-agent）核心机制

### 三大核心价值

| 价值 | 说明 |
|------|------|
| **隔离** | 独立上下文窗口，读取的文件内容、命令输出及推理过程均不流入主对话——解决上下文污染 |
| **约束** | 工具权限白名单构建物理层面的边界，不是"请不要修改"的软建议，而是物理禁止 |
| **复用** | Markdown 定义，支持团队共享与项目迁移，符合组件化思想 |

### 定义与配置

子智能体存放在 `.claude/agents/` 目录下，由 **YAML 前置元数据** + **Markdown 正文指令** 组成：

```
.claude/agents/
├── code-reviewer.md   # 代码审查子智能体
├── test-runner.md     # 测试运行子智能体
└── log-analyzer.md    # 日志分析子智能体
```

#### YAML 元数据字段

| 字段 | 作用 | 示例 |
|------|------|------|
| **name** | 唯一标识符，系统日志/调试/调用链中的"身份证" | `code-reviewer` |
| **description** | 触发描述/路由依据，主智能体据此决策 | `"审查代码安全性与规范性"` |
| **tools** | 工具白名单，物理安全保障最小权限原则 | `[Read, Grep, Glob]` |
| **model** | 指定模型，简单任务用轻量模型控成本 | `haiku` / `sonnet` |
| **permissionMode** | `plan` = 系统级只读保障，比白名单更严格 | `plan` / `default` |
| **skills** | 预加载 Skill 知识包，启动时自动注入专业知识 | `[secure-coding]` |
| **hooks** | 配置事件钩子，特定时机自动执行检查 | — |

### 执行过程

当主智能体委派任务时，Agent 框架启动一个全新子进程：
1. 子进程拥有独立上下文窗口
2. 仅加载 System Prompt + CLAUDE.md + 子智能体定义指令 + 主智能体传递的任务描述
3. 子智能体在其隔离空间内执行，执行过程不泄露到主对话
4. 最终仅向主智能体返回结果摘要，而非全部过程细节

---

## 子智能体实战示例

### 代码审查子智能体

- **tools**：仅 `[Read, Grep, Glob]` 三项只读工具
- **定位**：严格只读审查员，严禁修改文件
- **输出**：遵循固定结构化格式，方便主智能体高效提取关键信息

### 测试运行子智能体

- **角色**：execute-only，运行测试套件并返回结果
- **约束**：禁止编辑源码和测试用例文件（`permissionMode: plan`）
- **行为**：测试失败时仅分析失败原因，不尝试修复代码

### 日志分析子智能体

- **tools**：受限 Bash + `JQ` 管线
- **能力**：提取关键信息、执行异常检测算法
- **输出**：仅返回异常摘要和根因定位，不做修改建议

---

## Skills 与子智能体的两种组合模式

### 方向A：子智能体预加载 Skill

**包含关系**：子智能体 ⊃ Skill（子智能体是主角，Skill 是"操作手册"）

- 触发方式：子智能体启动时通过 `skills` 字段预加载
- System Prompt：SubAgent.md 正文 + Skill 全量内容注入
- 典型场景：bug-fixer 预加载 secure-coding Skill——子智能体定义"身份与目标"（战略），Skill 定义"方法与标准"（战术）

### 方向B：Skill 派生子智能体

**包含关系**：Skill ⊃ 子智能体（Skill 是主角，子智能体是"执行容器"）

- 触发方式：用户 `/skill-name` → Skill 的 `context: fork` → 自动 fork 子智能体
- System Prompt：Agent 类型默认 Prompt + CLAUDE.md + SKILL.md 作为任务指令
- 典型场景：`/codebase-health-check` — Skill 定义"怎么做"，子智能体只是隔离上下文临时容器

### 职责划分原则

| 层 | 负责 | 内容 |
|----|------|------|
| **子智能体（战略）** | 身份与目标 | Who（身份）、What（任务）、Where（范围）、Output（交付） |
| **Skill（战术）** | 方法与标准 | How（流程）、With What（工具）、By What Standard（规范）、Quality（验收） |

---

## 何时启用子智能体：四维决策

| 维度 | 信号 | 子智能体的作用 |
|------|------|---------------|
| **大规模文件读取** | 需要读取大量文件或预处理海量数据 | 隔离"输入噪声"，防止主上下文被填满 |
| **高频输出生成** | 预期产生大量中间输出（测试报告、详细日志） | 充当"过滤器"，仅向主对话回传摘要 |
| **上下文完整保护** | 当前任务只是复杂任务的一环 | 剥离高消耗子任务，保护主对话空间 |
| **操作权限与安全边界** | 涉及敏感操作或需严格权限控制 | 受限沙箱环境，影响局限在局部 |

---

## Sub-agent 价值（原框架视角）

- 节省主 Agent 上下文：子 Agent 深入探索后只回传摘要。
- 并行探索：多个只读探索任务可以同时运行。
- 专业化：Explore、Plan、claude-code-guide 等子 Agent 有不同系统提示和工具权限。
- 成本控制：简单探索可路由到更快更便宜的模型。

## 适用与不适用

**适合：**
- 高度并行化、互不依赖的子任务。
- 信息量远超单上下文窗口的调研任务。
- 工具调用复杂但可以分工隔离的任务。

**不适合：**
- 需要多个 Agent 实时共享同一上下文的协同编辑。
- 强依赖链路任务，后一步必须实时等待前一步细节。
- 大多数紧耦合代码修改任务。

## Agent Teams

Agent Teams 是实验性团队机制：Team Lead 负责创建成员、分配任务和整合结果；Teammates 有独立上下文，可以通过 mailbox 直接通信，并共享任务列表。它混合了 Orchestrator-Worker、Mesh 和 Blackboard 思路。

## 关联页面

- [[entities/项目_ClaudeCode|Claude Code 项目]]
- [[concepts/概念_Skills与子智能体协作|Skills 与子智能体协作]]
- [[concepts/概念_Agent编排|Agent 编排]]
- [[concepts/概念_上下文工程|上下文工程]]
- [[concepts/概念_Skill系统|Skill 系统]]
- [[concepts/概念_ClaudeCode任务执行机制|Claude Code 任务执行机制]]
- [[sources/来源_ClaudeCode多智能体|来源：Claude Code 多智能体]]
- [[sources/来源_ClaudeCode实战_子智能体与任务委派|来源：子智能体与任务委派实战]]
