---
type: concept
tags:
  - ai
  - agent
  - skill
  - claude-code
  - trigger
  - description
  - semantic-matching
summary: "Skill 触发机制决定 Skill 能否在恰当时机被激活。双通道设计（显式调用 + 语义匹配）配合 description 三要素公式（What + When + Not for），需要防止欠触发和过触发，并区分参考型与任务型两种 Skill。"
sources:
  - "raw/ai/ClaudeCode实战/Chapter 3授人以渔，Skills工程实践.md"
updated: "2026-05-25"
---

# 概念：Skill 触发机制

## 定义

Skill 的触发机制是整个 Agent 系统的关键核心——**触发甚至比 Skill 内容本身更重要**，再好的内容如果无法成功触发，也是无效的 Skill。

## 双通道激活机制

Skill 通过两条并行的通道被激活：

### 通道一：显式调用（Explicit Invocation）

用户直接通过命令触发：

```
/skill-name [参数]
```

- 明确、直接、无歧义
- 若定义了 `argument-hint`，用户可携带参数
- 适合：有副作用、需要用户确认的操作

### 通道二：语义匹配（Semantic Matching）

Agent 深入理解用户意图后，自主判断哪个 Skill 与当前任务最契合，自动加载。

- Skill 的核心价值所在
- 对用户完全透明
- 适合：知识参考、规范查询

### 优化技巧

若 Skill 数量较多，可在拼入提示词前用小模型预过滤一轮，再将候选 Skill 打包发给大模型——用小模型降低匹配成本。

> 一般的 Agent 设计都将 Skill 信息拼接到系统提示词之后或用户消息之前。

## Description：Skill 的灵魂

语义匹配机制 **完全依赖** `description` 字段。它不是人类阅读的说明性文章，而是 Agent 决策"是否调用该 Skill"的 **唯一依据**。

### 推荐结构公式

```
description = [功能定义](What) + [触发场景](When) + [排除范围](Not for)
```

### 三步设计法

| 步骤 | 内容 | 说明 |
|------|------|------|
| **第一步**：核心能力（What） | 一句话精准概括"能做什么" | 确定基本功能定位 |
| **第二步**：触发场景（When） | `Use when user...` 句式，穷举触发词汇 | 提高语义匹配命中率 |
| **第三步**：排除范围（Not for） | 明确不适用场景 | 防止过触发（推荐但非必选） |

### 反面示例

```yaml
# 过于模糊 — Agent 无法判断使用时机
description: Helps with projects

# 过于技术化 — 缺失用户视角的触发关键词
description: 使用层次关系实现项目实体模型

# 仅描述功能 — 未界定触发场景
description: 生成API文档
```

### 正确示范

```yaml
description: Generate API documentation from Express, FastAPI, or Spring Boot source code.
  Use when user asks to "Write API docs", "document endpoints", "create OpenAPI specs",
  or mentions "Swagger". Supports route detection, request/response schema extraction,
  and authentication requirement marking.
```

> **核心原则**：description 是给大模型看的，不是给人类看的。大模型在深度语义匹配，必须在 description 中穷尽用户可能使用的表达方式。

## 过触发与欠触发

触发机制面临两种典型失效模式：

### 欠触发（Under-Triggering）

| 项目 | 说明 |
|------|------|
| **表现** | Skill 本应被调用，但没有被触发 |
| **数据** | 缺乏明确指引时，Agent 有 56% 的概率不会查看可用 Skills |
| **原因** | description 过于技术化，与用户口语化表述存在语义差距 |
| **修复** | 加入用户常用表达、同义词、口语化说法，甚至常见错误表述 |

### 过触发（Over-Triggering）

| 项目 | 说明 |
|------|------|
| **表现** | Skill 在不该调用的场景被错误激活 |
| **原因** | description 过于宽泛，包含太多高频通用词汇 |
| **修复** | 引入负向约束：`Not for...` 明确划定边界 |

### 评估标准

构建 10~20 个测试用例，覆盖两类场景：
- **应触发任务**：触发率 ≥ **90%**
- **无关任务**：误触发率 ≤ **5%**

## 两种 Skill 类型

通过 `disable-model-invocation` 字段分为两种：

| 维度 | 参考型 Skill | 任务型 Skill |
|------|------------|------------|
| **配置** | 默认行为 | `disable-model-invocation: true` |
| **核心逻辑** | 按需加载的知识库 | 受控的执行工具 |
| **触发方式** | Agent 自动匹配触发 | 用户手动 `/skill-name` 调用 |
| **description** | 注入上下文参与匹配 | 不注入上下文，仅作识别说明 |
| **使用场景** | 知识、规范、框架、标准 | 有副作用的操作（部署、提交） |

### 选择标准

判断标准：**如果 Agent 自动执行这个 Skill，最坏的情况是什么？**

- **感到紧张**（如自动提交未测试代码）→ 任务型，`disable-model-invocation: true`
- **无关紧要**（多展示一段参考文档）→ 参考型，自动加载

> "副作用"越大，控制权越要收紧。对任何可能改变系统状态、造成不可逆后果的操作，永远不要信任 Agent 的自动判断。

## 关联页面

- [[concepts/概念_渐进式披露|渐进式披露]] — description 预算如何约束触发
- [[concepts/概念_Skill工程设计|Skill 工程设计]] — SKILL.md 的正确定位
- [[concepts/概念_Skill系统|Skill 系统]] — Skill 完整体系
- [[comparisons/ClaudeMD_vs_Skills|Claude.md vs Skills]] — 知识的两维度
