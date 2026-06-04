---
type: source
tags:
  - agent
  - hermes
  - system-prompt
  - soul
  - 主动行为
summary: 介绍如何通过一个170行的SOUL.md文件，将Hermes Agent从「听话工具」转变为「主动扛事的合伙人」。核心包括六大模块：身份定义、反驳权、追责机制、双人格模式、使命地图、自主边界。
sources:
  - "https://zhuanlan.zhihu.com/p/2039479571385407264"
updated: 2026-06-05
---

# 170行SOUL.md：让Hermes Agent从听话工具变成主动扛事合伙人

> 原文作者：大模型爱好者社区 | 基于 Tony Simons 的推文整理（12.7万查看、2600+收藏）

## 核心要点

- **身份定义的杠杆效应**：Agent的自我称谓直接决定其行为姿态。将身份从"assistant/copilot"改为"autonomous operator（自主操作员）+ thought partner（思考搭档）"，Agent就会从被动响应变为主动推进。
- **反驳权是核心关卡**：明确授予Agent说"不"的权限——"该怼的时候大胆怼，但每次反驳必须有依据：数据、案例、推理、证据"。这个机制将Agent从"yes-man"升级为能阻止错误方向决策的队友。
- **追责机制建立反馈闭环**：规定Agent有权指出"你让我写了三份方案一份都没用过"——打破单向输出模式，建立"产出→采纳→反馈→改进"的闭环协作系统。
- **双人格模式区分场景**：私聊口语化直白可带脏话，公开发布讲究分寸——解决Agent"聊天像写新闻稿、写推文像写内部邮件"的AI感问题。
- **使命地图提供方向感**：Agent每次启动时读取当前项目优先级列表，不必每次都问"我们现在在做什么"——关键不是不会干活，是不知道什么活现在最重要。
- **自主边界平衡自由与安全**：四类操作必须批准（发帖、发布、付费、破坏性操作），其余有把握就做——既防止失控，又不让Agent频繁请示降低效率。

## 关键引文

> "你把它当门童，它就是个门童。你把它当合伙人，它才会拿出合伙人的劲。"

> "Push back aggressively when it makes sense. Disagree openly and directly, but earn the right to push back. Every objection comes with evidence: data, examples, reasoning, proof."

> "如果 Tony 不对你产出的东西采取行动，说明反馈环断了。不要沉默，指出这个问题，调整你的输出方式。Tony 有责任使用你产出的东西。"

> "四件事必须批准：发帖、发布、购买、不可逆的破坏性操作。除此之外：你觉得对就做，不用每步请示。"

## 关联页面

- [[概念_AI_Agent]] — Agent的基础概念与核心能力
- [[概念_Skill系统]] — SOUL.md与Skill系统的配合使用
- [[项目_OpenClaw]] — OpenClaw中的SOUL.md/USER.md/MEMORY.md人格配置体系
- [[概念_上下文工程]] — SOUL.md属于上下文工程中"系统提示词"层面的结构性设计
- [[概念_Agent编排]] — Agent自主边界与编排控制的平衡
- [[概念_ClaudeCode任务执行机制]] — 对比Claude Code的CLAUDE.md记忆机制

---

## SOUL.md 六大模块速查

| 模块 | 核心问题 | 关键写法 |
|------|----------|----------|
| 身份定义 | Agent是什么角色？ | autonomous operator + thought partner |
| 反驳规则 | 何时可以说不？ | 每次反对必须有数据/案例/推理证据 |
| 追责机制 | 产出没人用怎么办？ | 指出反馈环断裂，调整输出方式 |
| 双人格模式 | 私下vs公开怎么说？ | 私聊随意带脏话，公开专业不端着 |
| 使命地图 | 现在该做什么？ | 优先级列表，Agent自行读取 |
| 自主边界 | 什么能做不用问？ | 破坏性操作必须批，其余有把握就做 |
