---
type: source
tags:
  - agent
  - runtime
  - architecture
  - control-plane
  - harness
  - system-engineering
summary: 从系统工程视角分析Agent系统的演进——Agent正在重走OS和Cloud Runtime的老路。核心论点包括：context window不是runtime、harness会腐化、append-only session log类似event sourcing、brain与hands是control plane/data plane分离、tools与sandbox正在变成disposable runtime。
sources:
  - "https://zhuanlan.zhihu.com/p/2037479092090622773"
  - "Anthropic《Scaling Managed Agents: Decoupling the brain from the hands》"
updated: 2026-06-05
---

# Agent系统正在重新走一遍OS和Cloud Runtime的老路

> 原文作者：acodespace（阿里巴巴） | 2026-05-11 · 对Anthropic论文的读后感

## 核心要点

- **问题形状已从prompt变为runtime**：Agent工程经历了三个阶段跃迁——早期关注prompt工程，中期关注workflow orchestration，现在核心问题变成了runtime：状态放哪里？失败怎么恢复？上下文满了什么能丢？任务跑几小时/几天怎么续？
- **Harness会腐化且最危险的是「过期的正确性」**：harness会慢慢堆满某一代模型/工具/部署环境的假设。今天好用的prompt trick、context reset策略，模型升级后可能变成干扰——"曾经救过线上所以没人敢删，等底层模型变了就变成看不见的阻力"。
- **Context window不是runtime**：上下文窗口只是当前调用能看到的一小块工作集，不能承担持久状态。正确做法是把完整session当成append-only log（类似event sourcing），模型需要什么再从log构造view。
- **Brain与Hands是Control Plane与Data Plane的分离**：brain（模型+harness）负责决策编排，hands（sandbox/tool/MCP）负责真执行——两层生命周期不同，混在一起短期省事长期全是耦合。"K8s不会让每个pod自己定全局调度，agent系统也在进入这个阶段。"
- **Tools与Sandbox正在变成disposable runtime**：不是"家"而是"工位"——需要时provision、坏了就丢、污了就换。设计目标应是靠runtime恢复任务，而不是抢救某个容器。
- **Interface比Implementation更值钱**：稳定的接口（session/execute/wake/provision）才是长期资产——"模型、harness、工具协议、sandbox都会换，押注具体实现会被拖住，押注稳定接口下层可以演进"。
- **Agent infra已进入系统工程阶段**：long-running reliability、可观测性、评价体系都还没被吃透，但未来竞争会从prompt engineering滑回调度、隔离、一致性、恢复、观测、权限、评估这些老派系统工程问题。

## 关键引文

> "context window is not runtime。模型上下文更像CPU cache、或进程当前映射进来的页：影响很大，但不能承担持久状态。"

> "harness里最危险的不一定是bug，而是过期的正确性——它曾经对过、救过线上，所以没人敢删；等底层模型变了，它就变成看不见的阻力。"

> "单个进程重要，但OS更重要；单个服务重要，但cloud runtime更重要；单个模型当然重要，但谁能定义那层稳定的运行时抽象，谁才可能在搭下一代基础设施的地基。"

> "Agent未必停在'更会聊天的应用'，而可能变成一种runtime workload。"

## 关联页面

- [[概念_上下文工程]] — context engineering正在变成runtime engineering的一部分
- [[概念_Agent架构模式]] — control plane与data plane的架构分层
- [[概念_Agent编排]] — orchestration越来越像control plane
- [[概念_AI_Agent]] — Agent从chatbot到runtime workload的范式转变
- [[概念_ClaudeCode任务执行机制]] — Claude Code中的session管理、compact、状态持久化实践
- [[来源_Harness详解]] — Harness Engineering作为运行时抽象的详细阐述
- [[来源_Harness革命]] — Harness的历史必然性与30年软件工程演进
