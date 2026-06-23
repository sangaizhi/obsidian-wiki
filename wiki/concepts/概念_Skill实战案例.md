---
type: concept
tags:
  - ai
  - agent
  - skill
  - claude-code
  - code-review
  - git-commit
  - practical
summary: "两个完整的 Skill 实战案例：代码审查 Skill（参考型，三级优先：安全→性能→质量，每项含等级/位置/建议四要素）和智能提交 Skill（任务型，disable-model-invocation + !`command` 动态注入 + allowed-tools 安全围栏）。"
sources:
  - "raw/ai/ClaudeCode实战/Chapter3 授人以渔：Skills工程实践.md"
updated: "2026-05-31"
---

# 概念：Skill 实战案例

## 案例一：代码审查 Skill（参考型）

### 设计目标

按团队标准执行结构化代码审查，自动触发，按优先级顺序检查。

### 审查约定

| 原则 | 要求 |
|------|------|
| **优先级** | 安全问题 > 性能问题 > 代码质量 |
| **反馈** | 必须提供具体修改建议，严禁仅指出问题不给方案 |
| **分级** | 每个问题均标注严重等级（Critical / Major / Minor） |

### 目录结构

```
code-reviewing/
├── SKILL.md                       # 核心审查流程与标准
└── reference/
    └── security-level-guide.md    # 详细等级判定标准
```

### SKILL.md 审查流程设计

**第一优先级：安全审查**（立即报告）
- SQL 注入风险：直接拼接 SQL 字符串、未使用参数化查询
- XSS 漏洞：未转义的用户输入直接输出到 HTML
- 敏感信息硬编码：密码、密钥、Token、数据库连接字符串
- 权限验证缺陷：缺失认证中间件、越权访问逻辑

**第二优先级：性能问题**
- N+1 查询：循环内频繁查询数据库
- 索引缺失：高频查询字段未建立索引
- 重复计算：循环内可提升至循环外的不变量
- 内存泄露风险：未关闭的连接、持续增长的缓存

**第三优先级：代码质量**
- 函数过长：超过 50 行且无合理理由
- 命名不规范：变量或函数命名含义不清
- 错误处理缺失：空 catch 块、异常被静默吞掉
- 代码重复：违反 DRY 原则

**输出格式规范** — 每个问题包含四要素：

| 要素 | 说明 |
|------|------|
| 严重等级 | Critical / Major / Minor |
| 问题描述 | 具体阐述问题所在 |
| 文件位置 | `file_path:line_number` |
| 修改建议 | 提供具体的代码修正方案 |

> 若未发现任何问题，明确回复"通过审查"并简述已检查的主要方面。

---

## 案例二：智能提交 Skill（任务型）

### 设计目标

自动化 git 提交流程，由于操作具有副作用（直接修改代码仓库历史），必须设置为**任务型**，由用户手动触发。

### 关键设计点

| 设计点 | 实现 | 原因 |
|--------|------|------|
| **安全控制** | `disable-model-invocation: true` | 强制禁用模型自动调用，防止意外触发 |
| **动态参数** | `$ARGUMENTS` | 支持用户直接指定提交信息或留空触发自动生成 |
| **上下文注入** | `!`command`` | 执行时自动注入 `git status` 和 `git diff` |
| **成本优化** | `model: deepseek-v4-flash` | 轻量模型，提交操作主要依赖规则而非复杂推理 |
| **安全围栏** | `allowed-tools` 严格限定 | 仅允许 `git status/git add/git commit/git diff` |

### Commit Message 规范

```
feat: add user authentication with JWT
```

- 类型前缀：`feat:` / `fix:` / `docs:` / `refactor:` / `test:` / `chore:`
- 首行最多 72 字符
- 简洁但具有描述性

### 参比型 vs 任务型对比

| 维度 | 代码审查 Skill | 智能提交 Skill |
|------|---------------|---------------|
| **类型** | 参考型 | 任务型 |
| **触发** | Agent 自动匹配 | 用户手动 `/skill-name` |
| **副作用** | 无（仅输出审查结果） | 有（修改 git 历史） |
| **安全策略** | 宽松（Read/Grep/Glob） | 严格（allowed-tools 限定 git 子命令） |
| **模型选择** | 默认 | 轻量模型（降低成本） |

## 关联页面

- [[concepts/概念_Skill触发机制|Skill 触发机制]] — 参考型 vs 任务型的副作用原则
- [[concepts/概念_Skill动态上下文|Skill 动态上下文]] — !`command` 注入机制
- [[concepts/概念_Skill设计模式|Skill 设计模式]] — 两种案例的模式组合分析
- [[concepts/概念_Skill测试与迭代|Skill 测试与迭代]] — 如何验证 Skill 质量
