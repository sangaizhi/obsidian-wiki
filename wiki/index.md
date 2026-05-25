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
- [[concepts/概念_SpecCoding|Spec Coding]]：规格驱动编码方法论，在写代码之前先写规格文档，通过结构化工作流消除 AI 不确定性。
- [[concepts/概念_Agent训练与ChatTemplate|Agent 训练与 Chat Template]]：从训练样本、工具轨迹和 Chat Template 角度解释 Agent 能力如何被构造。
- [[concepts/概念_ClaudeCode多智能体|Claude Code 多智能体]]：Claude Code Sub-agent、Agent Teams 与多智能体协作边界。
- [[concepts/概念_ClaudeCode任务执行机制|Claude Code 任务执行机制]]：并行工具调用、后台任务、任务通知和输出文件的执行机制。

## 比较

- [[工作流_vs_Agent|工作流 vs Agent]]：工作流适合固定流程，Agent 适合动态决策；实践中应先工作流后 Agent，避免过度设计。
- [[ClaudeMD_vs_Skills|Claude.md vs Skills]]：常驻通用规则 vs 按需专业领域知识，企业规章制度 vs SOP 操作手册的类比。

## 知识图谱

- [[overview/知识图谱|知识图谱]]：AI Agent 知识体系的完整关系图谱，展示概念间依赖、组合与决策关系。

## 后端技术

### 总览

- [[overview/主题_Java后端技术栈综述|Java 后端技术栈综述]]：以 Java 基础、Spring 框架、RabbitMQ/Kafka 消息中间件组织 raw/笔记 新资料。

### 技术实体

- [[entities/技术_Java|Java]]：后端基础技术栈，覆盖集合、并发、NIO、JVM 等核心模块。
- [[entities/技术_Spring|Spring]]：Java 后端应用框架体系，覆盖 IoC/DI、MVC、Security、Social 和 Cloud Config。
- [[entities/技术_RabbitMQ|RabbitMQ]]：基于 AMQP 的消息队列中间件，覆盖路由、可靠投递、消费治理和 Spring 集成。
- [[entities/技术_Kafka|Kafka]]：高吞吐分布式日志与消息系统，覆盖 Topic、Partition、副本、索引、offset 与消费组。

### Java 基础

- [[concepts/概念_Java集合框架|Java 集合框架]]：用 List、Set、Map 三类抽象组织常见容器。
- [[concepts/概念_HashMap|HashMap]]：基于哈希桶数组，通过链表和红黑树处理哈希冲突的 Map 实现。
- [[concepts/概念_有序Map|有序 Map]]：通过自然顺序或 Comparator 排序，TreeMap 提供红黑树有序映射。
- [[concepts/概念_Java并发基础|Java 并发基础]]：线程生命周期、并发并行、启动终止和共享状态治理。
- [[concepts/概念_Java线程通信|Java 线程通信]]：volatile、synchronized、等待通知和 join 等线程协作方式。
- [[concepts/概念_volatile|volatile]]：保证共享变量可见性的轻量级同步机制。
- [[concepts/概念_synchronized|synchronized]]：通过对象监视器保护共享数据临界区访问。
- [[concepts/概念_Executor框架|Executor 框架]]：将任务提交与线程调度解耦的 Java 并发执行抽象。
- [[concepts/概念_Java线程池|Java 线程池]]：通过线程复用、任务队列和拒绝策略控制并发执行。
- [[concepts/概念_Java_NIO|Java NIO]]：以 Buffer、Channel、Selector 为核心的非阻塞 IO 编程模型。
- [[concepts/概念_JVM类加载|JVM 类加载]]：类从字节码进入虚拟机到完成初始化的阶段与加载器体系。
- [[concepts/概念_JVM运行时内存|JVM 运行时内存]]：程序计数器、虚拟机栈、堆、方法区等运行时区域。
- [[concepts/概念_Java垃圾回收|Java 垃圾回收]]：可达性判断、回收算法和垃圾收集器组合。

### Spring 与安全

- [[concepts/概念_Spring核心思想|Spring 核心思想]]：OOP、BOP、AOP、IoC、DI/DL 和模块化架构。
- [[concepts/概念_手写Spring框架|手写 Spring 框架]]：用简化实现拆解 IoC、DI、MVC 九大组件和请求分发流程。
- [[concepts/概念_SpringSecurity|Spring Security]]：通过过滤器链和拦截器组织 Web 请求认证、身份上下文和授权。
- [[concepts/概念_OAuth2|OAuth2]]：第三方应用不获取用户密码也能获得有限资源访问权限的授权协议。
- [[concepts/概念_SpringSocial|Spring Social]]：将 OAuth2 第三方登录流程封装进 Spring Security 过滤器链。
- [[concepts/概念_SpringCloudConfig|Spring Cloud Config]]：用 Config Server 和 Client 做微服务集中配置管理。

### 消息中间件

- [[concepts/概念_RabbitMQ基础模型|RabbitMQ 基础模型]]：Exchange、Queue、Binding、Message、VirtualHost、Channel 等消息流转元素。
- [[concepts/概念_RabbitMQ可靠性投递|RabbitMQ 可靠性投递]]：Confirm、Return、消息落库、备份交换机等降低丢失风险。
- [[concepts/概念_RabbitMQ消费端治理|RabbitMQ 消费端治理]]：ACK/NACK、限流、TTL、死信队列和幂等消费。
- [[concepts/概念_RabbitMQ与Spring集成|RabbitMQ 与 Spring 集成]]：Spring AMQP、RabbitTemplate、监听容器和消息转换器。
- [[concepts/概念_Kafka基础与高可用|Kafka 基础与高可用]]：Topic、Partition、Broker、Segment、索引、Consumer Group 和副本同步。

### 来源摘要

- [[sources/来源_Java集合框架笔记|来源：Java 集合框架笔记]]：List、Set、Map、HashMap、HashSet、SortedMap 与 TreeMap。
- [[sources/来源_Java并发编程笔记|来源：Java 并发编程笔记]]：线程、通信、volatile、synchronized、Executor 与线程池。
- [[sources/来源_Java_NIO笔记|来源：Java NIO 笔记]]：Buffer、Channel、Selector 与非阻塞 IO。
- [[sources/来源_JVM笔记|来源：JVM 笔记]]：类加载、类加载器、运行时数据区、垃圾回收算法与收集器。
- [[sources/来源_Spring编程与手写框架笔记|来源：Spring 编程与手写框架笔记]]：Spring 思想、IoC/DI、MVC 与手写框架。
- [[sources/来源_SpringSecurity与OAuth笔记|来源：Spring Security 与 OAuth 笔记]]：Security 过滤器、拦截器、OAuth2 与 Spring Social。
- [[sources/来源_SpringCloudConfig笔记|来源：Spring Cloud Config 笔记]]：集中配置的使用场景与基础原理。
- [[sources/来源_RabbitMQ笔记|来源：RabbitMQ 笔记]]：AMQP 模型、路由、可靠投递、消费治理和 Spring 集成。
- [[sources/来源_Kafka面试题|来源：Kafka 面试题]]：Topic、Partition、Broker、副本、索引、offset 与消费组。

## 实体

- [[entities/项目_OpenClaw|OpenClaw 项目]]：开源 AI Agent 平台，280K+ GitHub Stars，三层架构，支持 20+ 消息渠道。
- [[entities/项目_HermesAgent|Hermes Agent 项目]]：自进化 AI Agent，106K+ GitHub Stars，实现 Memory/Skill/Nudge Engine 三子系统闭环。
- [[entities/项目_ClaudeCode|Claude Code 项目]]：Anthropic 官方 CLI 编程 Agent，动态 System Prompt + 三层工具架构。
- [[entities/项目_GitHubSpecKit|GitHub Spec Kit 项目]]：规格驱动开发工具流，用 constitution/spec/plan/tasks 将意图细化为实现。
- [[entities/工具_Superpowers|Superpowers 工具]]：开源 AI 编程工作流插件，强制执行 TDD，适配 Claude Code/Codex/Gemini 等。
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
- [[sources/来源_SpecCoding实战|来源：Spec Coding 实战]]：得物技术基于 Claude Code 的规格驱动编码实战复盘。
- [[sources/来源_日志诊断Skill|来源：日志诊断 Skill]]：MCP 日志平台 + Skill 模式实现 Bug 定位全自动闭环。
- [[sources/来源_HermesAgent|来源：Hermes Agent Self-Improving]]：Agent 自我进化闭环的三个子系统和设计哲学。
- [[sources/来源_PerplexitySkill设计|来源：Perplexity Skill 设计]]：Perplexity 论文解读，Skill 本质是上下文封装，六步设计框架。
- [[sources/来源_Superpowers赋能|来源：Superpowers 赋能]]：开源 AI 编程工作流插件，强制执行 TDD，子代理驱动开发。
- [[sources/来源_多Agent协同设计|来源：多 Agent 协同设计]]：分工 + 通信 + 仲裁三维度，AI PM 面试实战。
- [[sources/来源_ClaudeCode架构分析|来源：Claude Code 架构分析]]：万字长文分析 System Prompt 组织、工具设计哲学、CLAUDE.md 注入机制。
- [[sources/来源_LLM_Agent总体框架|来源：LLM Agent 总体框架]]：Agent = LLM × Planning × Memory × Tools，覆盖 ReAct、Plan-and-Solve 与 Reflection。
- [[sources/来源_Agent上下文管理策略|来源：Agent 上下文管理策略]]：context rot、上下文卸载、可逆压缩、子 Agent 隔离和 KV cache 策略。
- [[sources/来源_ClaudeCode多智能体|来源：Claude Code 多智能体]]：Sub-agent 使用边界、内置子 Agent 与 Agent Teams 协作模型。
- [[sources/来源_ClaudeCode并行后台任务管理|来源：Claude Code 并行后台任务管理]]：并行工具调用、后台执行、任务通知和 TaskOutput 误区。
- [[sources/来源_ClaudeCode实战_Skills工程实践|来源：Claude Code Skills 工程实践]]：Claude.md vs Skills，Skill 的工程化定义与"教" vs "约束"设计哲学。
- [[sources/来源_GitHubSpecKit入门|来源：GitHub Spec Kit 入门]]：GitHub Spec Kit 的 SDD 工作流、适用场景与 Vibe Coding 对比。

## 生活

### 育儿

- [[月子中心_vs_月嫂|月子中心 vs 月嫂]]：月子中心省心专业，月嫂灵活便宜，异地家庭优先选月子中心。
- [[sources/来源_月子中心vs月嫂|来源：月子中心 vs 月嫂]]：大冰关于月子中心与月嫂选择的建议。
