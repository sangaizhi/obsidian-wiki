---
type: concept
tags:
  - ai
  - agent
  - architecture
  - runtime
  - evolution
  - system-engineering
summary: "Agent 系统正在从 Prompt Engineering 走向 Runtime Engineering，其架构演进呈现出类似 OS 和 Cloud Runtime 的模式——控制面/数据面分离、append-only log、disposable sandbox 等。"
sources:
  - "raw/知乎/2026-06-05/Agent 系统正在重新走一遍 OS 和 Cloud Runtime 的老路.md"
updated: "2026-06-05"
---

# 概念：Agent 架构演进类比（OS / Cloud Runtime）

## 定义

Agent 系统的架构演进正在重演操作系统和 Cloud Runtime 的发展路径。问题的核心已从「怎么让模型更聪明」（Prompt Engineering）转向「长期任务如何可靠执行」（Runtime Engineering）。Agent 不再是 chatbot，而是一种 **runtime workload**——需要调度、隔离、一致性、恢复、观测、权限、评估等系统工程能力。

## 演进三阶段

| 阶段 | 关注焦点 | 关键问题 |
|------|---------|---------|
| **Prompt Engineering** | 怎么让模型更会拆任务、更稳地调工具 | 提示词技巧 |
| **Workflow Orchestration** | 文件读写、命令执行、API、测试反馈、失败重试 | 编排复杂度 |
| **Runtime Engineering** | workflow 跑在哪、状态放哪、失败怎么恢复、凭证属于谁 | 系统状态模型 |

## 十大核心洞察

### 1. Context Window 不是 Runtime

> Context window is not runtime.

模型上下文更像 CPU cache 或进程当前映射进来的页——影响很大，但不能承担持久状态。若把 context 当 session，恢复能力基本是假的——恢复的往往是被压缩、转述、可能偏掉的故事版本。

### 2. Harness 会腐化（Assumptions Rot）

Harness 容易堆满对某一代模型、某一代工具、某种部署环境的假设补丁。最危险的不是 bug，而是**过期的正确性**——它曾经对过、救过线上，没人敢删；等底层模型变了，就变成看不见的阻力。

**结论：Harness 不该被当成地基，它更像策略层、可替换的 control loop；真正该稳定下来的是更低层的 runtime abstraction。**

### 3. Append-Only Session Log = Event Sourcing

不只存当前状态，而存导致状态的事件序列：
- 可 replay、可 diff
- 不同版本 harness 可对同一条 session 做对比
- evaluator 可直接消费 log，不只依赖最终结果
- **把「记忆」从模型里拿出来**——runtime 存事实，模型在某个 view 上推理

### 4. Brain（Control Plane）与 Hands（Data Plane）分离

| 层 | 类比 | 职责 |
|----|------|------|
| **Brain (模型 + Harness)** | Control Plane | 决策、编排、选工具、解释反馈、定下一步 |
| **Hands (Sandbox / Tool / MCP)** | Data Plane | 真执行、跑命令、访问资源、搬数据、产生副作用 |

**生命周期不同**：control plane 要可恢复、可升级、可替换；data plane 要隔离、可丢、可按需起。

### 5. Tools 与 Sandbox 正在变成 Disposable Runtime

- Sandbox 不是家、不是记忆、不是长期状态——是某次执行的工位
- 需要时 provision，坏了就丢，污了就换
- 设计目标：靠 runtime 恢复任务，而不是抢救某个容器
- 凭证不直接进 sandbox，网络按能力授权

### 6. Interface 比 Implementation 更重要

| Interface（类比 OS syscall） | Implementation（类比底层实现） |
|------------------------------|-------------------------------|
| `execute` | 容器、VM、MCP、浏览器、客户VPC |
| `session` | 具体存储方案 |
| `wake` | 恢复策略 |
| `provision` | 调度器 |

**接口稳定，implementation 才能乱换。**未来模型、harness、工具协议、sandbox 类型都会换，platform 押注具体实现会被拖住，押注稳定接口下层可以演进。

### 7. Long-Running 是真正难点

Demo 很顺，但 long-running 系统不一样——中间模型失败、工具超时、sandbox 崩、网络抖、权限过期、用户插话、需求变、上下文压缩。系统不能次次从头来。

这些本质上是 **distributed systems** 的老问题，只是入口换成了 agent。

### 8. 观测与评价需要 Runtime 层面的能力

- 传统服务：logs、metrics、traces
- Agent 系统：token、tool calls、session events、中间决策、compaction 前后丢了什么、某条 harness 策略对成功率的影响
- 不能只最终扔给另一个模型打个分

### 9. Mental Model 转变：从模型出发 → 从 Runtime 出发

以前从模型能力出发（模型越强 Agent 越强），现在从 runtime 出发：

1. 状态模型是什么？
2. 控制面与执行面怎么分？
3. 失败恢复靠什么？
4. 哪些策略可插拔？
5. 哪些接口要长期稳定？
6. 什么绝不能只活在上下文窗口里？
7. 什么绝不能只塞进 sandbox？

### 10. 趋势收束

- context window 不是 runtime
- harness 不该变成历史补丁博物馆
- session log 会越来越像系统事实源
- tools 与 sandbox 会越来越 disposable
- orchestration 会越来越像 control plane
- interface 会比 implementation 更值钱
- agent 可能变成一种 **runtime workload**

## 类比总结

| OS / Cloud Runtime 概念 | Agent 对应 |
|-------------------------|-----------|
| 进程调度 | Agent Loop / Orchestration |
| CPU Cache | Context Window |
| 虚拟内存/页表 | Context Engineering（状态→Token 映射） |
| Event Sourcing / WAL | Append-Only Session Log |
| Control Plane / Data Plane | Brain / Hands |
| Disposable Container (Cattle) | Disposable Sandbox |
| Syscall | Execute / Session / Wake / Provision |
| 观测 (Metrics/Traces/Logs) | Token/Tool Calls/Session Events |
| 恢复/检查点 | Session State Recovery |

> **核心判断：** 单个模型当然重要，但当模型够多、任务够长、工具够复杂时，谁能定义那层**稳定的运行时抽象**，谁才可能在搭下一代基础设施的地基。Agent infra 已经进入系统工程阶段。

## 关联页面

- [[concepts/概念_Harness工程|Harness Engineering]]
- [[concepts/概念_ManagedAgents|Managed Agents]]
- [[concepts/概念_上下文工程|上下文工程]]
- [[concepts/概念_Agent记忆|Agent 记忆]]
- [[concepts/概念_Agent编排|Agent 编排]]
- [[concepts/概念_MCP|MCP 协议]]
