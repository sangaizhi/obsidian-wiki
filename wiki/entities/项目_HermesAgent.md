---
type: entity
entity: 项目
name: Hermes Agent
created: 2026-05-14
tags:
  - entity
  - project
  - Agent
  - Self-Improving
  - OpenSource
related:
  - "[[entities/项目_OpenClaw|OpenClaw 项目]]"
  - "[[concepts/概念_Skill系统|Skill 系统]]"
  - "[[concepts/概念_Agent记忆|Agent记忆]]"
  - "[[concepts/概念_工具调用|工具调用与执行]]"
sources:
  - "[[sources/来源_HermesAgent|来源：Hermes Agent Self-Improving]]"
  - "[[sources/来源_SOUL|来源：SOUL — Hermes Agent 人格化]]"
  - "[[sources/来源_OpenClaw与Hermes架构|来源：OpenClaw 与 Hermes 架构对比]]"
  - "[[sources/来源_Hermes7步优化|来源：Hermes 7 步 Token 优化]]"
---

# Hermes Agent

> 实现「Self-Improving」闭环的开源 AI Agent。106K+ GitHub Stars，OpenRouter Top Coding Agents 排名第一，增速 +204%。

## 核心特色

Hermes Agent 的核心创新在于**自我进化闭环**：Agent 干完活后自动将踩坑经验提炼成可复用的 Skill，下次遇到同类问题直接调用。用得越久，能力越强。

### 与 OpenClaw 的设计分野

| 维度 | Hermes Agent | OpenClaw |
|------|-------------|----------|
| Skill 创建 | 自动从经验创建（5+ 工具调用触发） | 手写 Markdown 或社区装 |
| Memory 机制 | 容量上限 + 自动压缩（2200 chars） | 纯追加，无限膨胀 |
| Skill 加载 | 渐进式按需加载（轻量索引） | 全量塞入上下文 |
| 学习能力 | 自我进化，越用越强 | 不会从工作中学到新东西 |
| 设计哲学 | 自己长 | 靠人喂 |

## 三个子系统

### Memory 系统
- 两个文件：`MEMORY.md`（环境事实，2200 chars 上限）和 `USER.md`（用户偏好，1375 chars 上限）
- 冻结快照机制实现 Prefix Cache 共享
- 声明式事实 vs 命令式指令

### Skill 系统
- 自动创建（5+ 工具调用阈值）
- `fuzzy_find_and_replace` 精确修补 + 安全扫描 + 自动回滚
- 渐进式加载（先看目录再翻全文）
- Skill Hub 解决冷启动（预装数据库专业技能）

### Nudge Engine
- 双计数器：10 回合 / 10 迭代触发
- 后台 fork Agent 静默审查（/dev/null 输出）
- 最多 8 次工具调用，防无限递归

## 实战效果

K8s 部署三次会话演进：工具调用 12→9→6，错误 2→1→0。

## 相关来源

- [[sources/来源_HermesAgent|来源：Hermes Agent Self-Improving]] — 原始文章
- [[sources/来源_SOUL|来源：SOUL — Hermes Agent 人格化]] — 170 行 SOUL.md 让 Agent 从工具变合伙人
- [[sources/来源_OpenClaw与Hermes架构|来源：OpenClaw 与 Hermes 架构对比]] — 源码级架构对比分析
- [[entities/项目_OpenClaw|OpenClaw 项目]] — 对比竞品
