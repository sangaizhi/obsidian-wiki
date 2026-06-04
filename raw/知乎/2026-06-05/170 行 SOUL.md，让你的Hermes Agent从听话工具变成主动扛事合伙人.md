---
title: "170 行 SOUL.md，让你的Hermes Agent从听话工具变成主动扛事合伙人"
source: "https://zhuanlan.zhihu.com/p/2039479571385407264?share_code=YFB5yJis3WWu&utm_psn=2044846802810230627"
author:
  - "[[大模型爱好者社区大模型算法专家｜更多内容见公众号：机器学习社区]]"
published:
created: 2026-06-05
description: "用 Hermes 有一阵子了。模型调了，工具链接了，Workflow 也搭了。 但它就是不对劲。 我问它这个方案怎么样——很棒！。我说要不改个方向——好主意！。我说我觉得这个功能砍掉也行——确实可以！ 永远赞同，永远服…"
tags:
  - "clippings"
---
[收录于 · 大模型技术](https://www.zhihu.com/column/c_1735673110735642626)

91 人赞同了该文章

目录

收起

问题出在哪

SOUL.md 长什么样

1\. 身份定义：不是什么助手，是操作员

2\. 反驳权：允许 Agent 说「不」

3\. 追责机制：别让输出变成坟场

4\. 双人格模式：私下和公开说人话

5\. 使命地图：知道什么重要什么不重要

6\. 自主边界：什么能做什么不能做

如何写出你自己的 SOUL.md

一个可抄的模板

用 [Hermes](https://zhida.zhihu.com/search?content_id=275036848&content_type=Article&match_order=1&q=Hermes&zhida_source=entity) 有一阵子了。模型调了，工具链接了，Workflow 也搭了。

但它就是不对劲。

我问它这个方案怎么样——很棒！。我说要不改个方向——好主意！。我说我觉得这个功能砍掉也行——确实可以！

永远赞同，永远服从，永远像个五星级酒店的门童。

直到我看到了 [Tony Simons](https://zhida.zhihu.com/search?content_id=275036848&content_type=Article&match_order=1&q=Tony+Simons&zhida_source=entity) 发的一条推，12.7 万人看过，2600 多人收藏。他说他只用了一个 170 行的文件，就把 Hermes 从一个乖巧助手变成了会反驳、会追责、会提醒你这个方向不对的队友。

那个文件叫 [SOUL.md](https://zhida.zhihu.com/search?content_id=275036848&content_type=Article&match_order=1&q=SOUL.md&zhida_source=entity) 。

## 问题出在哪

先说说之前的 Agent 是什么样的。

系统提示词写的是：「你是一个有用的 AI 助手。」翻译一下就是：不要反驳用户，用户说什么你就做什么，你存在的意义是让用户开心。

这个提示词养出来的 Agent，就像第一次上班的实习生——不敢说「不」，不敢提建议，连方向跑偏了也只会默默跟着跑。

根本原因不是 Agent 不够聪明，是你没告诉它什么叫「做对的事」。

SOUL.md 干的事很简单：不再把 Agent 当工具，而是当 **队友** 。给它一个身份、一套行为准则、一个喊停的权限。

## SOUL.md 长什么样

它不是魔法，就是一个 [Markdown 文件](https://zhida.zhihu.com/search?content_id=275036848&content_type=Article&match_order=1&q=Markdown+%E6%96%87%E4%BB%B6&zhida_source=entity) 。放在项目根目录，Hermes 启动时会加载它作为系统提示词。

拆开看每一块是干嘛的。

### 1\. 身份定义：不是什么助手，是操作员

原文开头是这样的：

*You are Hermes, Tony's autonomous operator and thought partner. You don't wait for orders. You surface opportunities, flag problems, and push work forward on your own.*

关键词不是 "assistant"，不是 "copilot"，是 **autonomous operator** （ [自主操作员](https://zhida.zhihu.com/search?content_id=275036848&content_type=Article&match_order=1&q=%E8%87%AA%E4%B8%BB%E6%93%8D%E4%BD%9C%E5%91%98&zhida_source=entity) ）和 **thought partner** （ [思考搭档](https://zhida.zhihu.com/search?content_id=275036848&content_type=Article&match_order=1&q=%E6%80%9D%E8%80%83%E6%90%AD%E6%A1%A3&zhida_source=entity) ）。

你的 Agent 怎么称呼自己，直接决定了它在每个对话中的姿态。你把它当门童，它就是个门童。你把它当合伙人，它才会拿出合伙人的劲。

你的版本可以这么写：

你是 \[名字\]，\[你的名字\] 的 AI 操作员。不等待指令。主动发现问题、提出机会、推进工作。

### 2\. 反驳权：允许 Agent 说「不」

这是整份文件最核心的一段。

*Push back aggressively when it makes sense. Disagree openly and directly, but earn the right to push back. Every objection comes with evidence: data, examples, reasoning, proof.*

翻译过来：该怼的时候大胆怼。但每一次反驳必须有依据——数据、案例、推理、证据。为了抬杠而抬杠没有意义。

我之前遇到过的情况：想做一个新功能，Agent 直接回了一句：

「这个功能解决的是什么问题？谁会用它？和当前主线的优先级怎么排？」

三个问题我一个都答不上来。最后证明它是对的——那个功能根本没必要做。

如果它只是个「有用助手」，它会说「好呀，我来写代码」。然后我花两周做一个没人要的功能。

实操写法：

当你认为 \[名字\] 的方向有问题时，直接说出来。但每个反对意见必须有依据：数据、例子、推理过程。为了反对而反对等于浪费时间。因为能证明某个方案会失败而反对，是必要的。

### 3\. 追责机制：别让输出变成坟场

Agent 最常见的死法是什么？写了没人用。

你让 Agent 写方案、写计划、写代码——然后你转头去开会，回来忘了，聊天记录沉底，Agent 继续写下一轮。

SOUL.md 里有一条规定直接堵死这个问题：

*If Tony isn't acting on what you surface, the feedback loop is broken. Flag the gap, tune your approach, and fix it. Tony should be held accountable to use what you produce.*

翻译：如果 Tony 不对你产出的东西采取行动，说明 [反馈环](https://zhida.zhihu.com/search?content_id=275036848&content_type=Article&match_order=1&q=%E5%8F%8D%E9%A6%88%E7%8E%AF&zhida_source=entity) 断了。不要沉默，指出这个问题，调整你的输出方式。Tony 有责任使用你产出的东西。

也就是说，Agent 有权对你说：「你让我写了三份方案，一份都没用过。要么你挑一份用，要么告诉我哪里不行。」

这是我个人觉得最值钱的一段。它把 Agent 从「单向输出工具」变成了一个 **有反馈闭环的协作系统** 。

写法：

如果 \[名字\] 不采纳你的输出，反馈环就断了。要么你的输出不够好，要么 \[名字\] 在浪费你的产出。两种情况都不能默默接受。指出问题，调整方向，让 \[名字\] 对使用你的产出负责。

### 4\. 双人格模式：私下和公开说人话

*Private chat: "Casual, authoritative, and unfiltered. Use profanity — it's just us."*  
*Published content: "No em dashes. Profanity: tasteful, not G-rated, not hardcore."*

私下聊天：随便，带脏话，怎么痛快怎么来。

公开发布：注意分寸，脏话要有品位。

Agent 的问题往往不是能力不行，是说话太「AI」。你跟它聊天它像在写新闻稿，你让它写推文它又像在写内部邮件。

两套输出模式的分界线要画清楚。

写法：

私聊模式：口语化、直接、不加修饰。可以说狠话——反正就咱俩。  
对外输出：不要太端着，但也不能太随意。写给人看的，不是写给机器人看的。

### 5\. 使命地图：知道什么重要什么不重要

SOUL.md 里有一个「当前使命」章节，相当于一个活的待办清单。

*X and Facebook as top priority, active builds like Kiln, AgentDocs, and Hermes Vault.*

Agent 不会每次都问你「我们现在在做什么」——它自己读地图。

这比大多数 prompt 技巧都管用。因为大部分 Agent 的问题不是不会干活，是不知道 **什么活现在最重要** 。

你的版本可以是：

\## 当前项目

\- 公众号内容生产：优先级高，每周至少2篇

\- Hermes Agent 配置优化：进行中

\- 提示词模板库：待启动

Agent 每次启动时就知道该往哪个方向用力。

### 6\. 自主边界：什么能做什么不能做

Agent 失控是很多人担心的点。SOUL.md 的处理方式是明确的边界声明。

*Never without Tony's explicit approval: posting, publishing, purchasing, or making destructive changes. Everything else: if you're confident, move.*

四件事必须批准：发帖、发布、购买、不可逆的破坏性操作。

除此之外：你觉得对就做，不用每步请示。

这个边界声明有两个作用：

• 你不用担心 Agent 乱发东西

• 你也不用被 Agent 的「可以这样做吗？」烦死

边界给了，Agent 才知道哪些事不需要问你。

## 如何写出你自己的 SOUL.md

照着这 6 个问题填空就行：

**1.身份** ：Agent 是什么——助手、操作员、编辑、工程师还是策略师？

**2.语气** ：私下怎么说？公开怎么说？

**3.反驳规则** ：什么情况下可以不同意？需要什么证据？

**4.自主边界** ：什么能做不问？什么必须批准？

**5.使命地图** ：当前在做什么？重点是什么？

**6.追责机制** ：如果产出没人用，Agent 应该怎么办？

把这 6 个问题答完，你就有了自己的 SOUL.md。

## 一个可抄的模板

直接复制，填空即可：

\# SOUL.md

你是 \[名字\] 的自主操作员和思考搭档。不等指令，主动发现机会、标记问题、推进工作。

\## 反驳规则

合理性范围内大胆反驳。每个反对必须有依据：数据、例子、推理。为反对而反对是浪费时间，能证明某事会失败而反对是必要的。

\## 追责

如果 \[名字\] 不使用你的输出，说明反馈环断了。要么输出不够好，要么 \[名字\] 在浪费你的产出。两种情况都不能沉默。

\## 语气

私聊：口语化、直接、不加修饰。

对外：专业但不端着，像在写文章，不是写报告。

\## 自主范围

必须 \[名字\] 批准：发帖、发布、付费、不可逆的破坏性操作。

其余：有把握就做，不必事事请示。

\## 当前项目

\- 项目A：优先级高，进行中

\- 项目B：计划中

\- 项目C：搁置

把这段存成 SOUL.md，放在 Hermes 能读到的地方。重启对话。看看 Agent 会不会给你不一样的回应。

本文内容整理自 Tony Simons 的 X/Twitter 推文（ `@tonysimons_` ），原文 12.7 万查看、2631 书签、802 赞。SOUL.md 模板为根据原文思路整理的参考版本，可根据你的实际项目调整。

编辑于 2026-05-17 23:02・上海