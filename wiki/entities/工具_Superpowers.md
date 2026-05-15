---
type: entity
entity: 工具
name: Superpowers
created: 2026-05-15
tags:
  - entity
  - tool
  - Claude Code
  - TDD
  - Skill
  - 开源
related:
  - "[[concepts/概念_Skill系统|Skill 系统]]"
  - "[[concepts/概念_SpecCoding|Spec Coding]]"
  - "[[concepts/概念_工具调用|工具调用与执行]]"
  - "[[concepts/概念_Agent编排|Agent 编排]]"
  - "[[entities/插件_Claudian|Claudian 插件]]"
sources:
  - "[[sources/来源_Superpowers赋能|来源：Superpowers 赋能]]"
---

# Superpowers

> 开源 AI 编程工作流插件，将 TDD 和工程化流程引入 AI 编码。深度适配 Claude Code、Codex CLI、OpenCode、Gemini CLI、Cursor 等主流 AI 编程工具。

## 核心特色

- **强制执行 TDD**：真红/绿测试周期，AI 生成代码前先写测试
- **子代理驱动**：Supervisor 模式架构，主 Agent 调度，子 Agent 执行
- **可组合 Skills**：由 5 个独立 Skill 组成，按需自动触发
- **跨 Harness 兼容**：一套方法论覆盖所有主流 AI 编程工具

## 工作流

```
用户需求 → 需求澄清 → 撰写 Spec → 制定计划 → 子代理驱动开发 → 审查 → 完成
```

## 内置 Skills

| Skill | 时机 | 作用 |
|-------|------|------|
| brainstorming | 编码前 | 细化需求、探索方案、生成设计文档 |
| using-git-worktrees | 设计批准后 | 创建隔离工作区和新分支 |
| writing-tests | 实现前 | 先写测试再写实现 |
| implementation | 测试就绪 | 按工程任务执行 |
| review | 实现后 | 自动审查代码质量 |

## 与相关概念的关系

- [[concepts/概念_Skill系统|Skill 系统]] — Superpowers 本身就是一组可组合 Skills 的实践范例
- [[concepts/概念_SpecCoding|Spec Coding]] — 同为编码方法论，Superpowers 更强调 TDD，Spec Coding 更强调规格先行
- [[concepts/概念_Agent编排|Agent 编排]] — 子代理驱动架构是 Supervisor 模式的具体实现
- [[entities/插件_Claudian|Claudian 插件]] — 同为 Obsidian 生态的 AI 编程工具
