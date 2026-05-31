## [2026-05-25] ingest | raw/ai/ClaudeCode实战/Chapter 3 → wiki 全链路拆解

- 新建概念页（3 个）：
  - `wiki/concepts/概念_渐进式披露.md` — 三层图书馆模型、description 预算机制（上下文窗口 2%）、静默排除、"少而精"架构原则
  - `wiki/concepts/概念_Skill触发机制.md` — 双通道激活（显式调用 + 语义匹配）、description 三要素公式（What + When + Not for）、过/欠触发诊断、参考型 vs 任务型 Skill
  - `wiki/concepts/概念_Skill工程设计.md` — 目录规范（kebab-case + SKILL.md 大写）、元数据三维度（触发/权限/运行时）、路由器思维、契约式引用、500 行法则
- 更新现有页面（4 个）：
  - `wiki/sources/来源_ClaudeCode实战_Skills工程实践.md` — 扩展为全文 5 节核心要点 + 关键引文覆盖全部章节
  - `wiki/concepts/概念_Skill系统.md` — 新增 Claude Code Skills 工程实践小节 + 关联链接
  - `wiki/comparisons/ClaudeMD_vs_Skills.md` — 关联新概念页
  - `wiki/entities/项目_ClaudeCode.md` — 关联新概念页
- 更新索引：`wiki/index.md` 新增 3 个概念条目 + 更新来源描述
- 覆盖本次 Ingest 全部 5 章节（§3.1~§3.5）

- 新建来源摘要页：
  - `wiki/sources/来源_ClaudeCode实战_Skills工程实践.md`
- 新建比较页：
  - `wiki/comparisons/ClaudeMD_vs_Skills.md` — Claude.md（常驻通用规则）vs Skills（按需专业知识），企业规章制度 vs SOP 类比
- 更新现有概念页（1 个）：
  - `wiki/concepts/概念_Skill系统.md` — 新增 Claude.md vs Skills 二维知识体系、Skill 工程化定义（文件夹 vs 字符串）、"教" vs "约束"设计哲学
- 更新实体页（1 个）：
  - `wiki/entities/项目_ClaudeCode.md` — 添加新来源引用和相关比较页链接
- 更新知识图谱：`wiki/overview/知识图谱.md` — 新增 Claude.md vs Skills 决策节点（链接 Skill → ClaudeCode），覆盖状态 32/32
- 更新索引：`wiki/index.md` 新增 1 个来源 + 1 个比较页条目

---

## [2026-05-12] ingest | raw/ai/agent → wiki Agent 知识层

- 新建来源摘要页：
  - `wiki/sources/来源_AI概念脉络.md`
  - `wiki/sources/来源_Agent的记忆.md`
  - `wiki/sources/来源_上下文工程.md`
  - `wiki/sources/来源_Agent的规划能力.md`
  - `wiki/sources/来源_工作流_vs_Agent.md`
- 新建概念页：
  - `wiki/concepts/概念_AI_Agent.md`
  - `wiki/concepts/概念_Agent记忆.md`
  - `wiki/concepts/概念_上下文工程.md`
  - `wiki/concepts/概念_Agent规划能力.md`
- 新建比较页：`wiki/comparisons/工作流_vs_Agent.md`
- 新建总览页：`wiki/overview/主题_Agent入门综述.md`
- 更新索引：`wiki/index.md`
- 跳过空来源：
  - `raw/ai/agent/1. 什么要懂Agent.md`
  - `raw/ai/agent/2. 什么是AI Agent.md`
  - `raw/ai/agent/3. Agent的本质.md`
  - `raw/ai/agent/4. Agent的工具马甲.md`

## [2026-05-14] query | 新建 wiki/overview/知识图谱.md

- 新建知识图谱页：`wiki/overview/知识图谱.md`
  - Mermaid 全景图展示概念关系
  - 概念关系详解表（8 组核心关系）
  - 信息流路径图
  - 覆盖状态矩阵（已覆盖 7 项 / 待补充 3 项）
- 更新索引：`wiki/index.md` 新增「知识图谱」条目

## [2026-05-14] ingest | raw/OpenClaw橙皮书_extracted.txt → wiki

- 新建来源摘要页：`wiki/sources/来源_OpenClaw橙皮书.md`
- 新建实体页：`wiki/entities/项目_OpenClaw.md`
- 新建概念页：
  - `wiki/concepts/概念_工具调用.md`（填补工具调用缺口）
  - `wiki/concepts/概念_Skill系统.md`（填补 Skill/SOP 缺口）
- 更新总览：`wiki/overview/主题_Agent入门综述.md` — 移除已完成的待补充项
- 更新知识图谱：`wiki/overview/知识图谱.md` — 覆盖状态更新（已覆盖 11 项 / 待补充 1 项）
- 更新索引：`wiki/index.md` 新增 3 个条目（工具调用、Skill系统、OpenClaw项目）

## [2026-05-14] ingest | raw/抖音/2026-05-14/* → wiki

- 新建来源摘要页（5 个）：
  - `wiki/sources/来源_Agent的7种架构.md`
  - `wiki/sources/来源_ManagedAgents.md`
  - `wiki/sources/来源_CLAUDE优化指南.md`
  - `wiki/sources/来源_Skill架构优化.md`
  - `wiki/sources/来源_Obsidian攻略.md`
- 新建概念页（2 个）：
  - `wiki/concepts/概念_Agent架构模式.md` — 7 种主流 Agent 架构
  - `wiki/concepts/概念_ManagedAgents.md` — Anthropic 生产级 Agent 架构
- 新建实体页：`wiki/entities/插件_Claudian.md`
- 更新现有概念页（2 个）：
  - `wiki/concepts/概念_Skill系统.md` — 新增分层加载、Gating、元数据瘦身
  - `wiki/concepts/概念_上下文工程.md` — 新增 CLAUDE.md 三步优化法最佳实践
- 更新知识图谱：`wiki/overview/知识图谱.md` — 覆盖状态更新（已覆盖 15 项 / 待补充 1 项）
- 更新索引：`wiki/index.md` 新增 6 个条目

## [2026-05-14] ingest | raw/知乎 + raw/douyin → wiki

- 新建来源摘要页（3 个）：
  - `wiki/sources/来源_Harness工程.md`
  - `wiki/sources/来源_FunctionCalling.md`
  - `wiki/sources/来源_企业Agent编排.md`
- 新建概念页（3 个）：
  - `wiki/concepts/概念_Harness工程.md` — Agent 治理与控制面
  - `wiki/concepts/概念_FunctionCalling.md` — 工具调用技术机制
- 更新索引：`wiki/index.md` 新增 6 个条目

## [2026-05-14] ingest | raw/小红书 → wiki

- 新建来源摘要页：`wiki/sources/来源_月子中心vs月嫂.md`
- 新建比较页：`wiki/comparisons/月子中心_vs_月嫂.md`
- 更新索引：`wiki/index.md` 新增「生活 > 育儿」分类及 2 个条目
- 注：此为 wiki 首个非 AI 主题的内容，索引新增「生活」分类

## [2026-05-14] ingest | raw/知乎/2026-05-14 (第二批) → wiki

- 新建来源摘要页（3 个）：
  - `wiki/sources/来源_SpecCoding实战.md`
  - `wiki/sources/来源_日志诊断Skill.md`
  - `wiki/sources/来源_HermesAgent.md`
- 新建概念页：`wiki/concepts/概念_SpecCoding.md`（规格驱动编码方法论）
- 新建实体页：`wiki/entities/项目_HermesAgent.md`（自进化 Agent）
- 更新现有概念页（3 个）：
  - `wiki/concepts/概念_Skill系统.md` — 新增 Self-Improving Skills、MCP+Skill 组合模式
  - `wiki/concepts/概念_工具调用.md` — 新增 MCP 实战案例（日志平台 MCP）
  - `wiki/concepts/概念_Harness工程.md` — 新增 Spec Coding 三层规范体系实践
- 更新知识图谱：`wiki/overview/知识图谱.md` — 新增 Spec Coding/Hermes Agent 节点，覆盖状态 22/22
- 更新索引：`wiki/index.md` 新增 5 个条目（SpecCoding、HermesAgent、3 个来源）

## [2026-05-15] ingest | raw/抖音/2026-05-15 → wiki

- 新建来源摘要页（3 个）：
  - `wiki/sources/来源_PerplexitySkill设计.md`
  - `wiki/sources/来源_Superpowers赋能.md`
  - `wiki/sources/来源_多Agent协同设计.md`
- 新建实体页：`wiki/entities/工具_Superpowers.md`（开源 TDD 编程工作流插件）
- 更新现有概念页（2 个）：
  - `wiki/concepts/概念_Skill系统.md` — 新增 Perplexity Skill 设计六步框架（Context Packaging、Progressive Loading、Description=Router、Gotchas、Eval-Driven、Append-Mostly）
  - `wiki/concepts/概念_Agent编排.md` — 新增多 Agent 协同设计（分工/通信/仲裁三维度、Supervisor 模式、消息模式、冲突策略）
- 更新知识图谱：`wiki/overview/知识图谱.md` — 新增 Perplexity Skill Design、Superpowers 节点，覆盖状态 25/25
- 更新索引：`wiki/index.md` 新增 4 个条目（Superpowers 实体 + 3 个来源）

## [2026-05-15] ingest | raw/知乎/2026-05-15 → wiki

- 新建来源摘要页：`wiki/sources/来源_ClaudeCode架构分析.md`
- 新建实体页：`wiki/entities/项目_ClaudeCode.md`（Anthropic 官方 CLI 编程 Agent）
- 更新现有概念页（2 个）：
  - `wiki/concepts/概念_工具调用.md` — 新增 Claude Code 三层工具架构设计哲学（高/中/低层搭配、渐进式信息披露）
  - `wiki/concepts/概念_上下文工程.md` — 新增 System Prompt 动态组装、CLAUDE.md system-reminder 注入机制
- 更新知识图谱：`wiki/overview/知识图谱.md` — 新增 Claude Code 节点，覆盖状态 26/26
- 更新索引：`wiki/index.md` 新增 2 个条目（ClaudeCode 实体 + 来源）

## [2026-05-15] ingest | raw/笔记 → wiki Java 后端知识层

- 按 `TheSchema.md` 边界处理：`raw/` 只读，只创建和更新 `wiki/` 页面。
- 新建总览页（1 个）：
  - `wiki/overview/主题_Java后端技术栈综述.md`
- 新建来源摘要页（9 个）：
  - `wiki/sources/来源_Java集合框架笔记.md`
  - `wiki/sources/来源_Java并发编程笔记.md`
  - `wiki/sources/来源_Java_NIO笔记.md`
  - `wiki/sources/来源_JVM笔记.md`
  - `wiki/sources/来源_Spring编程与手写框架笔记.md`
  - `wiki/sources/来源_SpringSecurity与OAuth笔记.md`
  - `wiki/sources/来源_SpringCloudConfig笔记.md`
  - `wiki/sources/来源_RabbitMQ笔记.md`
  - `wiki/sources/来源_Kafka面试题.md`
- 新建技术实体页（4 个）：
  - `wiki/entities/技术_Java.md`
  - `wiki/entities/技术_Spring.md`
  - `wiki/entities/技术_RabbitMQ.md`
  - `wiki/entities/技术_Kafka.md`
- 新建概念页（24 个）：
  - Java 基础：`概念_Java集合框架`、`概念_HashMap`、`概念_有序Map`、`概念_Java并发基础`、`概念_Java线程通信`、`概念_volatile`、`概念_synchronized`、`概念_Executor框架`、`概念_Java线程池`、`概念_Java_NIO`、`概念_JVM类加载`、`概念_JVM运行时内存`、`概念_Java垃圾回收`
  - Spring 与安全：`概念_Spring核心思想`、`概念_手写Spring框架`、`概念_SpringSecurity`、`概念_OAuth2`、`概念_SpringSocial`、`概念_SpringCloudConfig`
  - 消息中间件：`概念_RabbitMQ基础模型`、`概念_RabbitMQ可靠性投递`、`概念_RabbitMQ消费端治理`、`概念_RabbitMQ与Spring集成`、`概念_Kafka基础与高可用`
- 更新索引：`wiki/index.md` 新增「后端技术」分类，建立总览、技术实体、概念和来源摘要之间的连接。
- 跳过：空文件、`target/`、`out/`、`bin/`、`src/` 构建/源码目录、xmind/vsdx/图片等二进制资产未单独生成页面。

## [2026-05-17] ingest | raw/知乎/2026-05-15 + 2026-05-17 → wiki Agent 深水区

- 按 `TheSchema.md` 边界处理：`raw/` 只读，只创建和更新 `wiki/` 页面。
- 新建来源摘要页（5 个）：
  - `wiki/sources/来源_LLM_Agent总体框架.md`
  - `wiki/sources/来源_Agent上下文管理策略.md`
  - `wiki/sources/来源_ClaudeCode多智能体.md`
  - `wiki/sources/来源_ClaudeCode并行后台任务管理.md`
  - `wiki/sources/来源_GitHubSpecKit入门.md`
- 新建概念页（3 个）：
  - `wiki/concepts/概念_Agent训练与ChatTemplate.md`
  - `wiki/concepts/概念_ClaudeCode多智能体.md`
  - `wiki/concepts/概念_ClaudeCode任务执行机制.md`
- 新建实体页：`wiki/entities/项目_GitHubSpecKit.md`
- 更新现有页面（9 个）：
  - `wiki/concepts/概念_AI_Agent.md` — 新增 Agent 四阶段执行模型。
  - `wiki/concepts/概念_Agent架构模式.md` — 补充 ReAct、Plan-and-Solve、Reflection 与 Claude Code Sub-agent。
  - `wiki/concepts/概念_Agent规划能力.md` — 新增 Plan-and-Solve。
  - `wiki/concepts/概念_工具调用.md` — 新增 Tool Selection/Tool-Calling 与 Claude Code 并发执行机制。
  - `wiki/concepts/概念_上下文工程.md` — 新增上下文卸载、可逆压缩和缓存友好组织。
  - `wiki/concepts/概念_Agent编排.md` — 新增 Claude Code Sub-agent、Agent Teams 与协作拓扑。
  - `wiki/concepts/概念_SpecCoding.md` — 新增 GitHub Spec Kit / SDD 工作流。
  - `wiki/entities/项目_ClaudeCode.md` — 新增 Sub-agent、Agent Teams、并行与后台任务执行。
  - `wiki/overview/知识图谱.md` — 新增 Agent 训练、Claude Code 多智能体/任务执行、GitHub Spec Kit 节点，覆盖状态 31/31。
- 更新索引：`wiki/index.md` 新增 3 个概念、1 个实体和 5 个来源摘要入口。
