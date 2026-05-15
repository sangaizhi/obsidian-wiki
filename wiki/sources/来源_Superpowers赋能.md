---
type: source
source: "https://v.douyin.com/NMtAQtBIGnA/"
author: "鲁大猿（AI 技术专题·第24集）"
created: 2026-05-15
tags:
  - source
  - 抖音
  - Superpowers
  - Claude Code
  - TDD
  - Skill
related:
  - "[[entities/工具_Superpowers|Superpowers 工具]]"
  - "[[concepts/概念_Skill系统|Skill 系统]]"
  - "[[concepts/概念_SpecCoding|Spec Coding]]"
  - "[[concepts/概念_Agent编排|Agent 编排]]"
  - "[[concepts/概念_工具调用|工具调用与执行]]"
---

# 来源：Superpowers 赋能 Claude Code

> 开源 AI 编程工作流插件，深度适配 Claude Code、Codex CLI、OpenCode 等主流 AI 编码代理。核心特色：强制执行测试驱动开发（TDD）。

## 工作流程

```
用户需求 → 需求澄清 → 撰写 Spec → 制定计划 → 子代理驱动开发 → 审查 → 完成
```

1. **需求澄清** — 不直接写代码，先问"你到底想做什么"
2. **撰写 Spec** — 将需求拆分为模块设计文档
3. **制定计划** — 强调 TDD（真红/绿）、YAGNI、DRY
4. **子代理驱动开发** — 分发任务给子代理，自动审查
5. **持续迭代** — 可连续自主工作数小时不偏离计划

## 核心技能（Skills）

Superpowers 由一组可组合的 skill 构成，自动触发：

| Skill | 触发时机 | 作用 |
|-------|---------|------|
| brainstorming | 编码前 | 细化需求、探索方案、生成设计文档 |
| using-git-worktrees | 设计批准后 | 创建隔离工作区和新分支 |
| writing-tests | 实现前 | 真 TDD 模式，先写测试再写实现 |
| implementation | 测试就绪 | 按工程任务执行实现 |
| review | 实现后 | 自动审查代码质量 |

## 关键价值

- **填补方法论空白** — 将"即兴模式"（prompt → 代码 → 改 bug）升级为工程化流程
- **TDD 强制执行** — 最大的差异化，AI 生成代码容易跳过测试
- **子代理架构** — Supervisor 模式的编程场景落地
- **跨 Harness 兼容** — 覆盖 Claude Code、Codex、Gemini、Cursor 等

## 相关页面

- [[entities/工具_Superpowers|Superpowers 工具]] — 项目实体详情
- [[concepts/概念_Skill系统|Skill 系统]] — Superpowers 本身是一组可组合 Skills
- [[concepts/概念_SpecCoding|Spec Coding]] — 同为编码方法论，Spec Coding 强调规格先行
- [[concepts/概念_Agent编排|Agent 编排]] — 子代理架构属于多 Agent 协同
