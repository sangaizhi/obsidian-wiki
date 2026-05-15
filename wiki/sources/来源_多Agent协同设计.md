---
type: source
source: "https://v.douyin.com/e54RRaY8JS4/"
author: "Nova-AI产品经理"
created: 2026-05-15
tags:
  - source
  - 抖音
  - 多Agent
  - 协同
  - 架构
related:
  - "[[concepts/概念_Agent编排|Agent 编排]]"
  - "[[concepts/概念_Agent架构模式|Agent 架构模式]]"
  - "[[concepts/概念_ManagedAgents|Managed Agents]]"
  - "[[concepts/概念_Harness工程|Harness Engineering]]"
---

# 来源：多 Agent 协同设计详解

> AI PM 面试实战讲解：多 Agent 协同设计的三大核心问题。核心框架：**多 Agent 不是堆数量，是分工 + 通信 + 仲裁三件事。**

## 三大设计维度

### ① 分工：Supervisor 模式

| 角色 | 类比 | 职责 |
|------|------|------|
| **主 Agent** | 项目经理 | 只调度，不干活 |
| **子 Agent** | 专科员工 | 只做一件事，做到精 |

**真实案例**：教育产品 4-Agent 架构（路由分发 → 答疑/批改/推荐），月活百万级。

### ② 消息通信：3 种模式

| 模式 | 适用场景 | 限制 |
|------|---------|------|
| **共享内存** | 小 demo | ≤ 3 个 Agent |
| **消息总线** | 生产环境首选 | LangGraph / CrewAI 同款 |
| **状态机** | 合规场景 | 金融/医疗/法律 |

### ③ 冲突解决：3 种策略

| 策略 | 适用场景 |
|------|---------|
| **主 Agent 仲裁** | 业务场景默认 |
| **投票机制** | 代码评审/内容审核 |
| **优先级规则** | 安全 Agent 否决一切 |

## 业界框架对照

| 视频观点 | 对应业界实现 |
|---------|------------|
| Supervisor 模式 | LangGraph `StateGraph` + `Supervisor` 节点 |
| 消息总线 | CrewAI `Process.sequential` / `Process.hierarchical` |
| 共享内存 | AutoGen `ConversableAgent` 即时对话 |
| 状态机 | LangGraph `StateGraph` 状态流转控制 |

## 可补充维度

- **数量级考量**：3个以内共享内存，10+ 必须消息总线，50+ 需分层路由
- **容错设计**：子 Agent 超时/垃圾数据的重试降级策略
- **可观测性**：多 Agent 调试比单 Agent 难一个数量级，需完整 trace 链路
- **成本控制**：每个 Agent 调用 LLM 成本线性累加

## 相关页面

- [[concepts/概念_Agent编排|Agent 编排]] — 多 Agent 协同是编排的核心子领域
- [[concepts/概念_Agent架构模式|Agent 架构模式]] — Multi-Agent 是 7 种架构之一
- [[concepts/概念_ManagedAgents|Managed Agents]] — 生产级多 Agent 架构参考
