# Perplexity：你这样写 Skill，就是造垃圾

> 分析时间：2026-05-15
> 来源：[抖音视频](https://v.douyin.com/1Zgix-kvv5A/) · 创作者：慢学AI
> 原始论文：[Designing, Refining, and Maintaining Agent Skills at Perplexity](https://research.perplexity.ai/articles/designing-refining-and-maintaining-agent-skills-at-perplexity)
> 发布时间：2026-05-13 · 点赞：15.5 万

---

## 视频概述

精读 Perplexity 官方论文《Designing, Refining, and Maintaining Agent Skills at Perplexity》第二期。

**第一期核心观点：** Skill 首先是一笔"上下文税"——每个加载的 Skill 都占用上下文窗口。

**第二期核心追问：** 既然每个 Skill 都要收税，到底要怎么写，才能让 Skill 的收益大于税收？

Perplexity 总结了一套六步方法论。

---

## Perplexity 的 Skill 设计六步框架

### ① 理解 Skill 的本质：Context Packaging

> ❌ 传统误区：Skill = 把文档放进prompt
> ✅ 正确定义：Skill = 面向模型的运行时上下文模块

**对比传统软件工程：**

| 传统系统 | Agent 系统 |
|---------|-----------|
| Function = 能力 | **Skill = 能力载体** |
| Module = 代码组织 | **Context = 资源** |
| API Route = 接口 | **Description = Router** |
| Unit Test = 质量保障 | **Eval = 行为保障** |

Perplexity 认为：**Skill 的本质是"上下文封装（Context Packaging）"**，目标不是给人看的，而是给模型提供正确的运行时上下文。

### ② 从 Prompt Engineering 升级到 Context Engineering

**传统 Prompt Engineering 的问题：**
- 所有内容一次性塞进 System Prompt
- 导致 Context 膨胀、Attention 分散、指令冲突、路由不稳定

**Context Engineering 的核心思想：** 上下文应该像**内存**一样被管理。

### ③ Progressive Loading（渐进式加载）——最重要的设计

Perplexity 将 Skill 分成三层：

```
第一层：Skill Index           (< 100 tokens)
├── name: "React UI Skill"
├── description: "Load when user asks for dashboard-style React admin UI"
└── 作用：Skill Routing（决定是否加载）

第二层：SKILL.md              (~ 5000 tokens)  
├── 工作流
├── 约束条件
├── Gotchas（避坑指南）
├── 示例
└── 作用：真正加载时读取核心规则

第三层：Heavy Assets           (按需读取)
├── references/
├── examples/  
├── assets/
└── 作用：不会一开始进入上下文
```

### ④ Description = Router（最重要的一句话）

**错误写法：** `This skill helps with React UI.`（面向人，描述功能）

**正确写法：** `Load when user asks for dashboard-style React admin UI.`（面向路由，描述触发条件）

> Description 不是文档简介，是 Skill 触发器

### ⑤ Skill Tree：分层路由

**反面案例：** Perplexity 曾尝试 1945 个税法 Skill 平铺——路由效果极差。

**解决方案：** 像 B-Tree 一样分层

```
Tax
├── Federal
├── State
└── International
```

核心：模型无法稳定从大量候选中选择，必须**分层检索、多级召回**。

### ⑥ Gotchas > 流程

Perplexity 提出一个关键洞察：

> **模型通常知道流程，但不知道坑。**

例如：
- ❌ 不要修改 migration 文件
- ❌ 先 dry-run
- ❌ 不要覆盖 generated code
- ❌ React Server Component 不支持 xxx
- ❌ Windows 环境下某 API 会失败

这些"反例/Gotchas"才是 Skill 最有价值的内容，因为它们直接决定稳定性、安全性和工程正确性。

### ⑦ Eval-Driven Development

**Skill 开发第一步不是写 Skill，是先写 Eval。**

| Eval 类型 | 作用 |
|-----------|------|
| Routing Eval | 是否正确加载 |
| File Read Eval | 是否读取正确文件 |
| Progressive Loading Eval | 是否按需加载 |
| End-to-End Eval | 最终任务质量 |

原因：**Agent 的问题是行为问题，而不是代码问题。**

### ⑧ Append-Mostly 维护模型

> **Skill 不是频繁改规则，而是持续追加失败经验。**

就像工程经验沉淀：每次踩坑就追加一条 Gotcha。这也是 OpenClaw `self-improving` skill 中 learnings 机制的设计根源。

---

## 与之前视频的关联

### 与鲁大猿系列（Superpowers/Claude Skill）

| 对比维度 | 鲁大猿 | 慢学AI |
|---------|-------|-------|
| 视角 | 开发者/实操 | 研究者/论文精读 |
| Skill目录结构 | 一致（SKILL.md + scripts + references） | 一致 + 更强调三层渐进加载 |
| Description | 强调命中率 | 强调路由触发条件 |
| 测试 | 没太强调 | **Eval-Driven 是核心** |
| 维护 | 生产部署 | **Append-Mostly** |

### 与 Nova-AI（多Agent协同设计）

两者底层是相通的：
- 多 Agent 的 **Supervisor 模式** = 主 Agent 做路由（类似 Description is Router）
- Skill 的 **Progressive Loading** = 消息传递中的按需通信
- **Eval-Driven** = 冲突解决的规则化

### 与 OpenClaw 的共鸣

这套方法论和 OpenClaw 的 Skill 系统几乎完美对齐：
- Skill Index ↔ ClawHub 的 skill 注册
- SKILL.md 结构（工作流+约束+示例）完全一致
- Progressive Loading ↔ OpenClaw 的 skill 按需注入
- Append-Mostly ↔ `self-improving-agent` 的 learnings 机制
- Description is Router ↔ skill 的 trigger 设计

---

## AI 延伸思考

### 为什么这是 Agent 工程的里程碑？

Perplexity 这篇文章最大的贡献不是"教你怎么写 prompt"，而是：

> **展示了 Agent Engineering 正在演化成一种新的软件工程。**

类比映射：

| Agent 世界 | 传统系统 |
|-----------|---------|
| Context | 内存 |
| Skill | 模块 |
| Description | 路由器 |
| Eval | 测试 |
| Progressive Loading | 虚拟内存 |
| Attention | CPU Cache |

### 来自实战的建议

1. **先别急着写 SKILL.md，先把 Description 写好**
2. **每个 Gotcha 都值得写，比流程描述重要 10 倍**
3. **第一个 Eval 是 Routing Eval**——先确保描述能准确触发
4. **100 token 的 Index 设计是强制自己思考"这个 Skill 到底什么时候用"**
5. **别让 Skill 超过 5000 token**，超过就拆

---

## 标签

`#Skill` `#Perplexity` `#ContextEngineering` `#Agent` `#AI架构` `#PromptEngineering` `#上下文管理` `#Eval` `#慢学AI`

---

## 总结

Perplexity 的 Skill 设计方法论可以用一句话概括：

> **把上下文当资源管理：分层、按需、路由、测试、沉淀。**

每个 Skill 都要收"上下文税"，只有做到**精准触发 + 渐进加载 + 行为修正**，才能让收益大于税收。

**未来 Agent 系统的竞争力，很可能不再只是模型本身，而是谁能更高效地组织上下文。**
