# Wiki 索引

## 总览

- [[overview/主题_Agent入门综述|Agent入门综述]]：Agent 是让大模型从会说走向会做的工程系统，核心由记忆、上下文、规划和工具执行组成。

## 概念

- [[concepts/概念_AI_Agent|AI Agent]]：在大模型能力之上叠加记忆、上下文管理、工具调用和执行闭环的可落地智能应用。
- [[concepts/概念_Agent记忆|Agent记忆]]：通过持久化存储和按需检索，让 AI 在多次会话中保留关键上下文。
- [[concepts/概念_上下文工程|上下文工程]]：控制模型输入的信息管理策略，目标是在保证质量的同时提升稳定性并降低成本。
- [[concepts/概念_Agent规划能力|Agent规划能力]]：把目标拆成步骤、根据反馈调整路径并完成任务闭环的核心能力。
- [[concepts/概念_工具调用|工具调用与执行]]：Agent 将决策落地为实际操作的执行能力层，核心工具集为 Read/Write/Edit/Bash。
- [[concepts/概念_Skill系统|Skill 系统]]：将固定流程封装为可复用的标准化模块，使 Agent 具备自我扩展能力。
- [[concepts/概念_Agent架构模式|Agent 架构模式]]：7 种主流 Agent 架构（ReAct、Reflection、Tool Use、Planning、Multi-Agent、Memory-Augmented、Human-in-the-Loop）。
- [[concepts/概念_ManagedAgents|Managed Agents]]：Anthropic 生产级 Agent 架构，大脑与双手解耦 + Session 持久化 + 零信任沙箱。
- [[concepts/概念_Harness工程|Harness Engineering]]：把大模型纳入工程体系的控制面，通过约束、验证和恢复机制让 Agent 从玩具变成生产力。
- [[concepts/概念_FunctionCalling|Function Calling]]：LLM 通过函数声明自动调用外部工具的机制，Schema 设计与错误处理实践。
- [[concepts/概念_Agent编排|Agent 编排]]：组织、协调、管理多个 Agent 协同工作的工程方法，填补多 Agent 协同缺口。

## 比较

- [[comparisons/工作流_vs_Agent|工作流 vs Agent]]：工作流适合固定流程，Agent 适合动态决策；实践中应先工作流后 Agent，避免过度设计。

## 知识图谱

- [[overview/知识图谱|知识图谱]]：AI Agent 知识体系的完整关系图谱，展示概念间依赖、组合与决策关系。

## 实体

- [[entities/项目_OpenClaw|OpenClaw 项目]]：开源 AI Agent 平台，280K+ GitHub Stars，三层架构，支持 20+ 消息渠道。
- [[entities/插件_Claudian|Claudian 插件]]：Obsidian 侧边栏 AI 编程代理，支持行内编辑和 Plan Mode。

## 来源摘要

- [[sources/来源_AI概念脉络|来源：AI概念脉络]]：AI、生成式 AI、LLM 与 Agent 的关系。
- [[sources/来源_Agent的记忆|来源：Agent的记忆]]：大模型短期记忆、长期记忆与 RAG 检索。
- [[sources/来源_上下文工程|来源：上下文工程]]：上下文工程作为记忆管理和成本控制策略。
- [[sources/来源_Agent的规划能力|来源：Agent的规划能力]]：CoT、动态规划和决策树式推演。
- [[sources/来源_工作流_vs_Agent|来源：工作流 vs Agent]]：工作流与 Agent 的定位、场景和选型法则。
- [[sources/来源_OpenClaw橙皮书|来源：OpenClaw橙皮书]]：OpenClaw 平台的完整入门到精通参考手册。
- [[sources/来源_Agent的7种架构|来源：7 种 Agent 架构]]：ReAct、Reflection、Tool Use、Planning、Multi-Agent、Memory-Augmented、Human-in-the-Loop。
- [[sources/来源_ManagedAgents|来源：Managed Agents]]：Anthropic Managed Agents 大脑与双手解耦架构。
- [[sources/来源_CLAUDE优化指南|来源：CLAUDE.md 优化]]：根文件做薄 + 按关注点拆分 + /memory 验收。
- [[sources/来源_Skill架构优化|来源：Skill 架构优化]]：元数据瘦身、分层加载、Skill Gating、上下文压缩。
- [[sources/来源_Obsidian攻略|来源：Obsidian 攻略]]：新手第一天必做 7 件事、同步方案、AI 集成。
- [[sources/来源_Harness工程|来源：Harness Engineering]]：大模型纳入工程体系的控制面设计。
- [[sources/来源_FunctionCalling|来源：Function Calling]]：原理、翻车场景、Schema 设计、面试考点。
- [[sources/来源_企业Agent编排|来源：企业 Agent 编排]]：任务/状态/工具/上下文四大编排维度。
