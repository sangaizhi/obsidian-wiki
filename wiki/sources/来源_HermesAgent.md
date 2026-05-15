---
type: source
source: "https://zhuanlan.zhihu.com/p/2035359235941384999"
author: "千问云"
created: 2026-05-14
tags:
  - source
  - 知乎
  - Hermes Agent
  - Self-Improving
  - Agent
related:
  - "[[entities/项目_HermesAgent|Hermes Agent 项目]]"
  - "[[concepts/概念_Skill系统|Skill 系统]]"
  - "[[concepts/概念_Agent记忆|Agent记忆]]"
  - "[[concepts/概念_工具调用|工具调用与执行]]"
  - "[[entities/项目_OpenClaw|OpenClaw 项目]]"
---

# 来源：Hermes Agent Self-Improving

> 深入解析 Hermes Agent 实现"Self-Improving"的源码级分析。106K+ GitHub Stars，OpenRouter 增速 +204%，Top Coding Agents 第一。

## 核心架构：三个子系统，一个闭环

Hermes Agent 在内部搭建了一套学习闭环，由三个子系统撑起：

### 1. Memory 系统 — 越用越懂你

- **两个文件**：`MEMORY.md`（环境事实、项目约定、工具怪癖，限 2200 chars）和 `USER.md`（用户偏好、沟通风格，限 1375 chars）
- **容量上限设计**：有限容量倒逼 Agent 做信息压缩，过时的自然被挤掉
- **冻结快照机制**：会话开始时捕获快照注入系统提示词，会话内不变以共享 Prefix Cache
- **声明式事实**：要求写成"User prefers concise responses"而非"Always respond concisely"
- **边界规则**："If you've discovered a new way to do something, save it as a skill." — Memory 不存操作步骤

### 2. Skill 系统 — 把做过的事变成会做的事

- **自动创建**：工具调用超过 5 次才值得创建，踩过坑再修复的经验才有价值
- **自我修补**：`fuzzy_find_and_replace` 做精确局部 patch + `_security_scan_skill()` 安全扫描 + 自动回滚
- **渐进式加载**：默认只放轻量索引（skill 名 + 一句话描述），Agent 判断相关时才加载完整内容
- **Skill Hub**：预装领域专业技能，解决冷启动问题

### 3. Nudge Engine — 定时提醒"该学习了"

- **双计数器**：Memory 按用户回合（10 回合触发）、Skill 按迭代（10 次触发）
- **后台 fork Agent**：不打扰用户的静默审查，输出重定向到 /dev/null
- **限制**：最多 8 次工具调用，review agent 自身 nudge 被禁用（防止无限递归）
- **共享 Memory**：review agent 和主 agent 共享同一份 Memory，写入直接生效

## 关键洞见

> **设计哲学分野**：OpenClaw 的 Skill 靠人喂（手写 Markdown），Hermes 的 Skill 自己长（从经验自动提炼）。当模型智能被商品化、Agent 框架被开源，真正的护城河是 Agent 在工作中积累的领域知识。

### 实战效果

K8s 部署三次会话演进：12→9→6 次工具调用，2→1→0 个错误。越用越强，而非每次都从零开始。

### 与 OpenClaw 对比

| 维度 | OpenClaw | Hermes Agent |
|------|----------|--------------|
| Skill 创建 | 手写或社区装 | 自动从经验创建 |
| Memory | 纯追加，无限膨胀 | 容量上限 + 自动压缩 |
| Skill 加载 | 全量塞入上下文 | 渐进式按需加载 |
| 学习能力 | 不会从工作中学到东西 | 每次踩坑都在加固 |

## 相关概念

- [[entities/项目_HermesAgent|Hermes Agent 项目]] — 项目实体详情
- [[concepts/概念_Skill系统|Skill 系统]] — Self-Improving Skill 的极致实践
- [[concepts/概念_Agent记忆|Agent记忆]] — Memory 子系统的理论基础
- [[entities/项目_OpenClaw|OpenClaw 项目]] — 关键对比对象
