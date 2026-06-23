---
type: concept
tags:
  - ai
  - agent
  - harness
  - engineering
  - governance
  - runtime
summary: "Harness Engineering 是把大模型纳入工程体系的控制面，通过约束、验证和恢复机制让 Agent 从玩具变成生产力。Agent = Model + Harness。"
sources:
  - "raw/知乎/2026-05-14/Harness工程详解.md"
  - "raw/知乎/2026-05-14/AI编程能力边界探索：基于 Claude Code 的 Spec Coding 项目实战｜得物技术.md"
  - "raw/知乎/2026-06-05/万字干货：理解 Harness Engineering，看这一篇就够了.md"
  - "raw/知乎/2026-06-05/最新！万字综述Harness革命！.md"
  - "raw/ai/ClaudeCode实战/Chapter5 防微杜渐：Hook事件驱动自动化.md"
updated: "2026-06-23"
---

# 概念：Harness Engineering

## 定义

Harness Engineering（约束工程）是一整套把大模型纳入工程体系的控制面设计。它不是 Prompt 技巧，也不是多写文档，而是围绕**如何约束非确定性引擎**的系统方法。

> **Agent = Model + Harness** —— Harness 是包裹模型运行的基础设施。它把大模型的"大脑"变成了 Agent 的"身体"。

Harness 与 Prompt 的本质区别：

> **Prompt 是指令，Harness 是约束——前者在模型脑子里，后者在模型外面。**

---

## 历史必然性：30 年软件工程的演进

Harness 的出现不是偶然，而是软件工程驾驭复杂性这一主线的最新章节：

| 年代 | 里程碑 | 驾驭的复杂性 |
|------|--------|------------|
| 1994 | GOF《Design Patterns》23 种设计模式 | 对象的生命周期与协作 |
| 2002 | Martin Fowler《企业应用架构模式》、DDD | 企业架构的分层与领域边界 |
| 2010 | 微服务时代 | 分布式系统的通信与最终一致性 |
| 2017 | DDIA《数据密集型应用》 | 数据在时间和空间维度的流动 |
| **2026** | **Harness Engineering** | **首次驾驭一个不确定性的系统——智能体** |

> **核心技术本质不变：抽象 + 结构化，把复杂的东西变得可控。**

---

## Agent 工程的三次跃迁

| 阶段 | 时间 | 核心 | 局限性 |
|------|------|------|--------|
| **Prompt Engineering** | 2023 | 怎么让模型理解我们（CoT 等） | 只影响模型内部 |
| **Context Engineering** | 2024-2025 | 给模型有深度的上下文（RAG、知识库） | 目光高度不够 |
| **Harness Engineering** | 2026 | 设计可控的系统——循环策略、工具、质量审核、分发治理 | 当前前沿 |

---

## 与传统软件工程的对比

| | 传统软件工程 | Harness Engineering |
|--|------------|-------------------|
| 管理对象 | 确定性（代码逻辑） | 非确定性（概率引擎） |
| 目标 | 防止人犯错 | 约束模型不失控 |
| 工具 | 类型系统、单测、CR | 沙盒、Checkpoint、外部验证 |
| 核心能力 | 编写代码 | 设计并驾驭复杂系统 |

---

## R.E.S.T 质量模型

Agent 从"有趣玩具"变为"可靠工具"的四大目标：

| 维度 | 关键要求 |
|------|---------|
| **可靠性（Reliability）** | 失败可恢复、操作幂等、行为一致性 |
| **效率（Efficiency）** | Token/API/时间预算控制、低延迟、高吞吐 |
| **安全性（Security）** | 最小权限、沙盒执行、输入/输出过滤 |
| **可追溯性（Traceability）** | 全链路追踪、决策可解释、状态可审计 |

---

## 架构坐标系

两个坐标轴定位 Agent 架构：

- **X 轴（执行流路由）**：静态预设 ↔ 动态自主
- **Y 轴（状态与上下文）**：隐式内部 ↔ 显式外部

| 象限 | 特征 | 代表 |
|------|------|------|
| **Harness Engineering** ✅ | 模型提供意图，外部 Harness 负责状态隔离与沙盒校验 | 推荐方案 |
| 提示词驱动 | AutoGPT、原生 ReAct，模型自主性高 | 适合实验 |
| 无状态链 | 单次 API 调用，LLM 当纯函数 | 适合简单任务 |
| 传统管道 | LangChain 顺序链，外部状态管理严谨 | 适合固定流程 |

---

## 好的 Harness 三要素

### 1️⃣ 前置验证（Evaluator 沙盒）

基于证据触发 Retry，而不是暴力重试。工具执行结果先经过验证再决定下一步。

### 2️⃣ 最小真相源（Spec is Truth）

唯一可靠的真相来源，确保任务跨天能无损恢复。Handoff 文档作为 Agent 的"外部持久化记忆"，每轮对话从阅读 handoff 恢复上下文开始。

### 3️⃣ 物理门禁（Checkpoint Before Execute）

破坏性操作前必须授权。执行前 Checkpoint：当前理解、下一步、风险、验证方式。

---

## Harness 六大核心组件

1. **Agentic Loop（心脏）**：观察→思考→行动的持续循环（继承自 ReAct），所有复杂行为从简单循环中涌现
2. **Tool System（手脚）**：工具调用能力，扩展行动的物理范围
3. **Memory & Context Management**：记忆和上下文管理，Token 转化流水线
4. **Guardrails（缰绳）**：Allow / Deny / Ask 权限控制
5. **Hooks（守卫）**：关键操作的拦截检查点（如禁止上传 .env 文件）
6. **Session（会话连续性）**：运行时状态持久化机制

---

## Claude Code Hooks：执行层 Guardrails

Claude Code Hooks 是 Harness 中“物理门禁”的具体实现：Claude.md 和 Skills 仍是认知层建议，而 Hooks 在系统执行层监听事件并执行强制策略。`PreToolUse` 可以在工具执行前拒绝或改写危险输入，`PostToolUse` 可以把外部检查结果反馈给模型，`Stop` 可以作为质量门控阻止不合格响应结束。

这让“不要删除敏感文件”“提交前必须 lint”“子智能体完成后必须审计”等规则从 Prompt 约束变成运行时机制。

## 控制平面与数据平面分离

| 层 | 职责 | 特征 |
|----|------|------|
| **Control Plane（控制平面）** | 任务调度、资源配额、行为规划、策略与权限 | 可恢复、可升级、可替换 |
| **Data Plane（数据平面）** | Agent 运行实例、状态存储、记忆存储、沙盒执行 | 隔离、可丢弃、可按需启动 |

---

## 三大核心约束

1. **上下文窗口限制**：有限 Token ↔ 无限世界状态
2. **非确定性输出**：概率引擎 → 需要确定性工程包装
3. **成本与延迟的平衡**：推理开销 vs 任务完成度

## 六大设计原则

1. **为失败而设计**：异常和失败是系统常态，不是个例
2. **契约优先**：所有交互由明确 Schema/API/Event 定义
3. **默认安全**：最小权限、零信任、纵深防御
4. **决策与执行分离**：规划（Control Plane）与执行（Data Plane）解耦
5. **万物皆可度量**：每个行为、决策、资源消耗都可量化
6. **数据驱动进化**：每次运行都是学习机会

---

## 关键工程实践

### REPL 容器抽象

Harness 的本质 = 带边界控制的 REPL（Read-Eval-Print Loop）容器：

| 环节 | 对应组件 | 职责 |
|------|---------|------|
| **Read** | 上下文管理器 | 将外部世界翻译成结构化 Prompt |
| **Eval** | 调用拦截器 | 捕获意图，路由到正确工具执行器 |
| **Print** | 反馈汇编器 | 组装结构化观测结果，注入上下文 |
| **Loop** | Agentic Loop | 持续循环直到终止条件 |

### 状态分离原则

> 必须将 LLM 严格视为**无状态的计算单元（CPU）**，所有跨轮次状态存储在 Harness 控制的外部状态管理器中。

### Token 转化流水线

1. 信息源收集 → 2. 相关性排序 → 3. 压缩与摘要 → 4. 预算分配 → 5. 模板组装

> **关键思想：把注意力管理变成外部工程问题，把有限窗口留给真正重要的信息。**

---

## Harness 解决五大落地难题

| 卡点 | Harness 应对 |
|------|-------------|
| 无限循环 | 循环边界 + 步数限制 + 终止条件 |
| 上下文爆炸 | Token 预算 + 压缩/摘要 + 相关性排序 |
| 权限失控 | Guardrails（Allow/Deny/Ask）+ 最小权限 |
| 质量不可控 | Evaluator 沙盒 + 外部验证 + 反思机制 |
| 成本不透明 | 可观测性指标 + 配额/预算控制 |

---

## 避坑指南

### 伪 Harness

- **"软约束"陷阱**：在 Prompt 写 5000 字 DO NOT——只是口头嘱咐，模型随时可能忽略
- **"军火库"陷阱**：塞 20 个 API 让模型自己挑，没有边界约束

### 劣质 Harness

- **"盲打"陷阱**：暴力死循环重试——模型可能为了修语法错误把架构删了
- **"官僚主义"陷阱**：强制重型文档流——浪费 Token，一变即成垃圾

---

## 8 阶段 SOP

| 阶段 | 控制动作 |
|------|---------|
| ① 目标收敛 | 先读文档，复述需求，先纠偏再放行 |
| ② 状态恢复 | 读 Spec/Handoff，用外部真相源恢复 |
| ③ 上下文装配 | 只给索引，按需补充 |
| ④ 任务分块 | 只做一段，只批准当前轮次 |
| ⑤ 链路设计 | 判断模式，定路线不改 Prompt |
| ⑥ 执行前校准 | Checkpoint，对齐后 Approval |
| ⑦ 外部验证 | 基于日志、测试，用证据决策 |
| ⑧ 回写交接 | 回写偏差，留干净恢复点 |

---

## 偏航的 4 个信号

1. 跳过阶段目标，直接谈总目标
2. 跳过中间产物，直接要改代码
3. 用主观语气替代客观证据
4. 混淆阶段完成和全局完成

---

## 工程师能力转型：从码农到系统工程师

> **工程师永远不会失业，但码农可能会失业。**

| 码农 | 工程师 |
|------|--------|
| 单纯写代码的人 | 设计并驾驭复杂系统的人 |
| 被 Agent 替代 | 驾驭 Agent |
| 关注实现细节 | 关注架构、抽象、可控性 |

**核心能力：** 理解系统复杂性、抽象和结构化思维、驾驭不确定性。越在 AI 时代，对 IT 系统的深层理解越有价值。

---

## 当前 Harness 生态格局

| 定位 | 代表 | 特点 |
|------|------|------|
| **纵深型（深度工程）** | Claude Code | Harness 的 Number One，上下文管理极强 |
| **开源竞争者** | Codex（OpenAI） | 开源，GPT 极强，适合做 review |
| **开源平替** | OpenCode | 免费，可配 DeepSeek，简洁好用 |
| **横向型（自动化运营）** | OpenClaw、Hermes | 多平台集成（WhatsApp、飞书） |
| **IDE 类** | Cursor、Windsurf | 编码 IDE |

---

## 实践案例：Spec Coding 三层规范体系

Spec Coding（规格驱动编码）是 Harness 在开发流程层面的具体实践：

| 层次 | 定位 | 解决的问题 |
|------|------|-----------|
| **约束层** `.claude/rules/` | 告诉 AI「禁止什么、必须怎样」 | 规范真空导致的不一致 |
| **示范层** `.claude/code-design/` | 告诉 AI「标准产出长什么样」 | 符合规则但不够地道的代码 |
| **视觉层** `.claude/ui-design/` | 告诉 AI「页面应该长什么样」 | 纯文字描述与设计意图的偏差 |

详见 [[concepts/概念_SpecCoding|Spec Coding]]。

---

## 关联关系

- **Harness → Managed Agents**：Managed Agents 是 Harness 思想的具体实现（Session 持久化、Sandbox 隔离）
- **Harness → 上下文工程**：上下文装配策略、最小上下文清单是 Harness 的一部分
- **Harness → 工具调用**：Checkerpoint、Evaluator 沙盒是工具调用的安全护栏
- **Harness → Agent 编排**：编排是 Harness 在宏观层面的扩展
- **Harness → Agent 演进类比**：Harness 是 Runtime Engineering 阶段的核心方法论
- **Harness → Claude Code Hooks**：Hooks 是执行层 Guardrails 与事件驱动控制点

## 关联页面

- [[concepts/概念_ManagedAgents|Managed Agents]]
- [[concepts/概念_上下文工程|上下文工程]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_Agent编排|Agent 编排]]
- [[concepts/概念_AI_Agent|AI Agent]]
- [[concepts/概念_Agent演进类比|Agent 演进类比]]
- [[concepts/概念_SOUL|SOUL 主动性配置]]
- [[concepts/概念_SpecCoding|Spec Coding]]
- [[concepts/概念_ClaudeCodeHooks|Claude Code Hooks]]
- [[concepts/概念_Hook事件生命周期|Hook 事件生命周期]]
- [[sources/来源_Harness工程|来源：Harness Engineering]]
- [[sources/来源_ClaudeCode实战_Hook事件驱动自动化|来源：Claude Code Hook 事件驱动自动化]]
