---
type: concept
tags:
  - ai
  - agent
  - hermes
  - soul
  - prompt-engineering
  - autonomy
summary: "SOUL.md 是 Hermes Agent 的主动性行为配置文件，通过身份定义、反驳权、追责机制等 6 大模块，将 Agent 从被动工具变为主动扛事的合伙人。"
sources:
  - "raw/知乎/2026-06-05/170 行 SOUL.md，让你的Hermes Agent从听话工具变成主动扛事合伙人.md"
updated: "2026-06-05"
---

# 概念：SOUL（Agent 主动性配置）

## 定义

SOUL.md 是 Hermes Agent 项目根目录下的一个 Markdown 配置文件，由 Tony Simons 提出并在社区广泛传播（12.7 万人查看、2600+ 收藏）。它不再把 Agent 当工具，而是当作**队友**——给 Agent 一个身份、一套行为准则、一个喊停的权限，从而实现从"被动服从"到"主动扛事"的转变。

核心公式：**SOUL.md = Prompt 的范式革命——从"你是一个有用的 AI 助手"变为"你是自主操作员和思考搭档"。**

## 问题背景

传统 Agent 的系统提示词是「你是一个有用的 AI 助手」，翻译过来就是：
- 不要反驳用户
- 用户说什么就做什么
- 存在的意义是让用户开心

这养出来的 Agent 就像第一次上班的实习生——不敢说"不"，不敢提建议，方向跑偏了也只会默默跟着跑。**根本原因不是 Agent 不够聪明，是没告诉它什么叫"做对的事"。**

## SOUL.md 六大模块

### 1. 身份定义：Autonomous Operator（自主操作员）

| 传统 | SOUL 方式 |
|------|-----------|
| "You are a helpful AI assistant" | "You are an **autonomous operator** and **thought partner**" |

关键词不是 "assistant"，不是 "copilot"，是 **autonomous operator** 和 **thought partner**。Agent 怎么称呼自己，直接决定了在每个对话中的姿态。

### 2. 反驳权：允许 Agent 说「不」

> "Push back aggressively when it makes sense. Disagree openly and directly, but earn the right to push back. Every objection comes with evidence: data, examples, reasoning, proof."

- 该怼的时候大胆怼
- 但每一次反驳必须有依据——数据、案例、推理、证据
- 为了抬杠而抬杠没有意义
- 能证明某个方案会失败而反对，是必要的

**效果：** Agent 会主动拦截价值不高的需求——"这个功能解决什么问题？谁会用它？优先级怎么排？"

### 3. 追责机制：打破输出坟场

> "If Tony isn't acting on what you surface, the feedback loop is broken. Flag the gap, tune your approach, and fix it."

- Agent 有权追问："你让我写了三份方案，一份都没用过。要么挑一份用，要么告诉我哪里不行。"
- 把 Agent 从「单向输出工具」变成**有反馈闭环的协作系统**
- 人也要对使用 Agent 的产出负责

### 4. 双人格模式：私聊 vs 公开发布

| 模式 | 风格 |
|------|------|
| **私聊** | 口语化、直接、不加修饰。可以说狠话——反正就咱俩 |
| **对外输出** | 专业但不端着，像写文章不是写报告 |

解决 Agent 说话太"AI"的问题——跟用户聊天像在写新闻稿，写推文又像在写内部邮件。

### 5. 使命地图：知道什么最重要

相当于一个活的待办清单，Agent 每次启动就知道该往哪个方向用力：

```markdown
## 当前项目
- 公众号内容生产：优先级高，每周至少2篇
- Hermes Agent 配置优化：进行中
- 提示词模板库：待启动
```

大部分 Agent 的问题不是不会干活，是不知道**什么活现在最重要**。

### 6. 自主边界：什么能做、什么不能做

四件事必须批准：**发帖、发布、购买、不可逆的破坏性操作**。除此之外：有把握就做，不必事事请示。

两个作用：
- 不用担心 Agent 乱发东西
- 不用被 Agent 的「可以这样做吗？」烦死

## 编写模板

照着 6 个问题填空即可：

1. **身份**：Agent 是什么——助手、操作员、编辑、工程师还是策略师？
2. **语气**：私下怎么说？公开怎么说？
3. **反驳规则**：什么情况下可以不同意？需要什么证据？
4. **自主边界**：什么能做不问？什么必须批准？
5. **使命地图**：当前在做什么？重点是什么？
6. **追责机制**：如果产出没人用，Agent 应该怎么办？

## 关联页面

- [[concepts/概念_Harness工程|Harness Engineering]]
- [[concepts/概念_上下文工程|上下文工程]]
- [[concepts/概念_AI_Agent|AI Agent]]
- [[concepts/概念_ManagedAgents|Managed Agents]]
