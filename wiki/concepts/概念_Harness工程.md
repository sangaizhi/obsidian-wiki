---
type: concept
tags:
  - ai
  - agent
  - harness
  - engineering
  - governance
summary: "Harness Engineering 是把大模型纳入工程体系的控制面，通过约束、验证和恢复机制让 Agent 从玩具变成生产力。"
sources:
  - "raw/知乎/2026-05-14/Harness工程详解.md"
  - "raw/知乎/2026-05-14/AI编程能力边界探索：基于 Claude Code 的 Spec Coding 项目实战｜得物技术.md"
updated: "2026-05-14"
---

# 概念：Harness Engineering

## 定义

Harness Engineering（约束工程）是一整套把大模型纳入工程体系的控制面设计。它不是 Prompt 技巧，也不是多写文档，而是围绕**如何约束非确定性引擎**的系统方法。

Harness 与 Prompt 的本质区别：

> **Prompt 是指令，Harness 是约束——前者在模型脑子里，后者在模型外面。**

## 与传统软件工程的对比

| | 传统软件工程 | Harness Engineering |
|--|------------|-------------------|
| 管理对象 | 确定性（代码逻辑） | 非确定性（概率引擎） |
| 目标 | 防止人犯错 | 约束模型不失控 |
| 工具 | 类型系统、单测、CR | 沙盒、Checkpoint、外部验证 |

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

## 好的 Harness 三要素

### 1️⃣ 前置验证（Evaluator 沙盒）

基于证据触发 Retry，而不是暴力重试。工具执行结果先经过验证再决定下一步。

### 2️⃣ 最小真相源（Spec is Truth）

唯一可靠的真相来源，确保任务跨天能无损恢复。Handoff 文档作为 Agent 的"外部持久化记忆"，每轮对话从阅读 handoff 恢复上下文开始。

### 3️⃣ 物理门禁（Checkpoint Before Execute）

破坏性操作前必须授权。执行前 Checkpoint：当前理解、下一步、风险、验证方式。

## 避坑指南

### 伪 Harness

- **"软约束"陷阱**：在 Prompt 写 5000 字 DO NOT——只是口头嘱咐，模型随时可能忽略
- **"军火库"陷阱**：塞 20 个 API 让模型自己挑，没有边界约束

### 劣质 Harness

- **"盲打"陷阱**：暴力死循环重试——模型可能为了修语法错误把架构删了
- **"官僚主义"陷阱**：强制重型文档流——浪费 Token，一变即成垃圾

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

## 偏航的 4 个信号

1. 跳过阶段目标，直接谈总目标
2. 跳过中间产物，直接要改代码
3. 用主观语气替代客观证据
4. 混淆阶段完成和全局完成

## 关联关系

Harness Engineering 与其他概念的关系：

- **Harness → Managed Agents**：Managed Agents 是 Harness 思想的具体实现（Session 持久化、Sandbox 隔离）
- **Harness → 上下文工程**：上下文装配策略、最小上下文清单是 Harness 的一部分
- **Harness → 工具调用**：Checkerpoint、Evaluator 沙盒是工具调用的安全护栏
- **Harness → Agent 编排**：编排是 Harness 在宏观层面的扩展

## 实践案例：Spec Coding 三层规范体系

Spec Coding（规格驱动编码）是 Harness 在开发流程层面的具体实践。得物技术团队通过三层规范体系实现了对 AI 编码产出的有效约束：

### 三层规范结构

| 层次 | 定位 | 解决的问题 | 对应 Harness 原则 |
|------|------|-----------|-----------------|
| **约束层** `.claude/rules/` | 告诉 AI「禁止什么、必须怎样」 | 规范真空导致的不一致 | 前置验证 + 物理门禁 |
| **示范层** `.claude/code-design/` | 告诉 AI「标准产出长什么样」 | 符合规则但不够地道的代码 | 最小真相源（标准模板作为真相源） |
| **视觉层** `.claude/ui-design/` | 告诉 AI「页面应该长什么样」 | 纯文字描述与设计意图的偏差 | 上下文装配（视觉上下文补充） |

### 实际效果

- 205 个文件保持高度一致的代码风格和命名规范
- 接口命名统一（fetch{Name}Api 格式）、目录分层被正确遵守
- 提供 HTML 设计稿后，AI 生成的 UI 与设计意图吻合度明显提升
- 规范文件只是「约束」而非「能力」— 示范层和视觉层是不可或缺的补充

详见 [[concepts/概念_SpecCoding|Spec Coding]] 和 [[sources/来源_SpecCoding实战|来源：Spec Coding 实战]]。

## 关联页面

- [[concepts/概念_ManagedAgents|Managed Agents]]
- [[concepts/概念_上下文工程|上下文工程]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_Agent编排|Agent 编排]]
- [[concepts/概念_AI_Agent|AI Agent]]
- [[sources/来源_Harness工程|来源：Harness Engineering]]
