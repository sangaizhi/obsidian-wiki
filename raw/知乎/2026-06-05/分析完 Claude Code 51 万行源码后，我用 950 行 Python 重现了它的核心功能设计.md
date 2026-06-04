---
title: "分析完 Claude Code 51 万行源码后，我用 950 行 Python 重现了它的核心功能设计"
source: "https://zhuanlan.zhihu.com/p/2023060686217965634?share_code=oPswMfyGEZe0&utm_psn=2040132907063371437"
author:
  - "[[何宇峰月之暗面 Kimi AI Agent 工程师]]"
published:
created: 2026-06-05
description: "前几天 Claude Code 源码泄露之后，本知乎小透明写了第一篇专栏来分析，意外火了（17万阅读，6000收藏）。私信问我最多的两类问题： 一类是”所以 Claude Code 的核心到底怎么实现的？有没有 Python 版的参考实现…"
tags:
  - "clippings"
---
[收录于 · AI Agent 技术笔记](https://www.zhihu.com/column/c_2022389601831061099)

419 人赞同了该文章

前几天 [Claude Code](https://zhida.zhihu.com/search?content_id=272419202&content_type=Article&match_order=1&q=Claude+Code&zhida_source=entity) 源码泄露之后，本知乎小透明写了第一篇专栏来分析，意外火了（17万阅读，6000收藏）。私信问我最多的两类问题：

一类是”所以 Claude Code 的核心到底怎么实现的？有没有 Python 版的参考实现？我想在其基础上做自己的 Coding Agent。”

另一类是”搞不到 Anthropic 的 API，Kimi/Gemini/DeepSeek 能不能跑在 Claude Code 上？”

这篇算是交作业了。

---

我基于最精华、最泛用、最易于二次开发的原则，将Claude Code的51万行代码中的核心功能设计，用950行 Python 代码重写了一遍，项目叫 **CoreCoder** （之前叫 NanoCoder，因为成了 CoreCoder ），GitHub 上完全开源：

### 先看效果

它会自己读代码、做精准编辑（每次改动输出 unified diff 让你看清改了什么）、跑命令验证、搜索代码库。跟 Claude Code 一样的工作流，但模型你自己选。

```
$ CoreCoder -m kimi-k2.5

You > 读一下 main.py，修掉拼错的 import

  > read_file(file_path='main.py')
  > edit_file(file_path='main.py', old_string='from utils import halper',
              new_string='from utils import helper')

--- a/main.py
+++ b/main.py
@@ -1,4 +1,4 @@
-from utils import halper
+from utils import helper

修好了，halper → helper。
```

---

### 这不是又一个 Claude Code 克隆

得先把定位说清楚。市面上已经有 Claw-Code（12 万+ star 的完整 Python/Rust 重写）、Aider、Cline 这些成熟工具。如果你只是想要一个能用的 AI 编程助手，直接用它们就好，CoreCoder 不跟它们抢这个位置。

CoreCoder 做的事情不太一样。

打个比方：想学 GPT 的训练过程，你大概不会上来就读 Megatron-LM 的几十万行代码。你会先看 Andrej Karpathy 的 nanoGPT——用三百来行代码把核心训练循环讲清楚。看完之后你就知道 GPT 是怎么回事了，然后你 fork 一份，加自己的数据集跑实验，或者在上面改出自己的架构。

**CoreCoder 对 AI 编程 Agent 的意义，就是 nanoGPT 对 Transformer 训练的意义。**

51 万行的 Claude Code，核心逻辑我提炼成了 950 行 Python。每个文件一屏能看完。你 fork 下来，花一个下午读完，就能理解一个生产级 AI 编程 Agent 的全部核心设计。然后在这 的基础上加你自己的东西：接入你公司的内部 API、加一个代码审查工具、改成支持你们的私有模型，或者单纯拿来做 AI Agent 课程的教学素材。

1300 行代码，全部是从 Claude Code 源码里验证过的设计模式。不是我自己拍脑袋想的架构，是 Anthropic 在 51 万行的生产系统里跑过、用数据验证过的东西。

---

### 51 万行里哪些是”承重墙”

读完 Claude Code 全部源码，我觉得真正”承重”的设计模式就 7 个。剩下几十万行是 UI 渲染（React + Ink）、MCP 协议适配、 [OAuth 认证](https://zhida.zhihu.com/search?content_id=272419202&content_type=Article&match_order=1&q=OAuth+%E8%AE%A4%E8%AF%81&zhida_source=entity) 、Skill 插件系统、那个有趣的宠物系统之类的。有意思，但不是一个编程 Agent 的核心。

以下是我提炼出来的 7 个模式，以及为什么它们重要。

### 1\. 搜索替换式编辑

这可能是 Claude Code 对 AI 编程工具领域最大的单点贡献。

让 LLM 编辑代码，行业里试过很多方案。行号补丁？LLM 记不住行号，上下文压缩之后更对不上。整文件重写？500 行文件改 2 行也得全部重新生成，token 费用爆炸而且 LLM 复制长文本时经常悄悄丢行。输出 unified diff 格式？ `@@` 行号经常算错，需要很复杂的容错解析。

Claude Code 的做法很简单：LLM 给出一段精确的文本片段（old\_string）和替换内容（new\_string）。约束只有一条——old\_string 必须在文件中 **恰好出现一次** 。

0 次 = LLM 记错了文件内容，让它重新读文件。多于 1 次 = 给的上下文不够，让它多包含几行来消除歧义。只有恰好 1 次的情况才执行替换。

一个约束，干掉了一整类的编辑 bug。CoreCoder 完整实现了这个模式，每次编辑后还会输出 unified diff，你能清楚看到改了什么。

### 2\. Agent 工具循环 + 并行执行

核心循环本身很简单：用户说话 → 调 LLM → LLM 说要用工具就执行 → 结果喂回 LLM → 重复，直到 LLM 只返回文本。每个 AI Agent 底层都是这个。

但 Claude Code 有个精妙的优化： `StreamingToolExecutor` （530 行），在模型 **还没说完** 的时候就开始执行前面的工具。模型流式吐出第一个工具调用的参数，那个工具立刻开始跑，不等后面的内容。多个只读工具（读文件、grep）并行执行，有副作用的（写文件、bash）独占。

用过 Claude Code 的人应该有体感——它”想”完之后开始干活的速度特别快。这是原因之一。

CoreCoder 用了简化版：等全部 tool\_calls 返回后，用 ThreadPoolExecutor 并行跑。没有流式解析那么极致，但多工具并行的收益保住了。

### 3\. 三层上下文压缩

128K token 听起来很多，十几轮工具调用就快满了。一个 `npm test` 的输出可能就好几千 token。

Claude Code 不是简单截断旧消息，而是用四层渐进策略：

1. **裁噪声** ：grep 返回 500 行结果但模型只用了 3 行？剩下 497 行保留头尾、删中间
2. **LLM 摘要** ：调一次模型把旧对话压成一段话，结果缓存起来下次复用
3. **硬压缩** ：只留最近几轮 + 一段总结，其他全删
4. **后台自动触发** ：用户无感

不同信息有不同的”保质期”——工具输出几轮后就没用了，但用户的需求描述整个会话都要保留。按保质期分级处理。CoreCoder 实现了前三层。

### 4\. 子代理生成

复杂任务拆给独立 Agent 处理，各有自己的上下文窗口。一个有意思的设计决策：子 Agent 不能再创建子 Agent，防递归爆炸。

Claude Code 的 AgentTool 有 1397 行。CoreCoder 实现了核心逻辑，50 行。

### 5\. 危险命令拦截

`rm -rf /` 、fork bomb、 `curl | bash` 这些在执行前就被拦下来。

### 6\. 会话持久化

`/save` 存盘， `corecoder -r session_id` 恢复。长任务可以随时中断、改天继续。

### 7\. 系统提示词动态组装

不是写死的字符串。根据工作目录、OS、可用工具列表实时拼接。在不同项目里 Agent 的行为是不一样的。

---

### 怎么用

安装一行：

```
pip install corecoder
```

选你的模型，任何 OpenAI 兼容 API 都行：

```
# Kimi K2.5
export OPENAI_API_KEY=你的key OPENAI_BASE_URL=https://api.moonshot.ai/v1
corecoder -m kimi-k2.5

# Claude Opus 4.6（通过 OpenRouter）
export OPENAI_API_KEY=你的key OPENAI_BASE_URL=https://openrouter.ai/api/v1
corecoder -m anthropic/claude-opus-4-6

# DeepSeek V3
export OPENAI_API_KEY=你的key OPENAI_BASE_URL=https://api.deepseek.com
corecoder -m deepseek-chat

# GPT-5 / Qwen 3.5 / Ollama 本地模型... 都行
```

也可以当 Python 库用：

```
from corecoder import Agent, LLM

llm = LLM(model="kimi-k2.5", api_key="...", base_url="https://api.moonshot.ai/v1")
agent = Agent(llm=llm)
response = agent.chat("找出项目里所有 TODO 注释")
```

想加自定义工具的话大概 20 行代码，继承一个 `Tool` 基类就行。

---

### 谁适合用

**想搞懂 AI 编程 Agent 原理的开发者。** 1300 行，每个文件一屏看完。比读 51 万行源码或者看第三方教程效率高。

**想自己造轮子的团队。** Fork 下来就是一个完整的起点。加 MCP 支持、加权限系统、换成你们公司的内部模型，比从零开始快一个量级。我见过的最小的、完整可运行的编程 Agent 实现。

**做 AI Agent 方向的研究者和学生。** 每个核心设计模式都有可运行的代码，带着代码理解论文和架构设计，比看流程图强。

**编程 Agent 的开发者。** Kimi K2.5、GLM5、MiniMax M2.7、Gemini 3.1 Pro 等大模型的 coding 能力已经很强了，缺一个好用的 Agent 壳来驱动它们。CoreCoder 就是这个壳。

如果你只是想要一个开箱即用的 AI 编程助手日常用，Claude Code、Cursor 或者 Aider 更适合。CoreCoder 的价值在于 **你能读懂它、改造它、在上面造出自己的东西** 。

---

### 配套导读

代码之外，我还写了一套 7 篇的 Claude Code 架构导读，每篇围绕一个核心设计模式展开：

1. 从 51 万行说起：技术栈、目录结构、十大设计哲学
2. 1729 行的 while(true)：Agent 核心循环的全部细节
3. 让 AI 安全地改你的代码：工具系统和搜索替换编辑
4. 有限窗口，无限任务：四层上下文压缩的工程权衡
5. 边想边做：StreamingToolExecutor 如何让工具执行零延迟
6. 当一个 Claude 不够用：多 Agent 协作系统的三种模式
7. Feature Flag 背后的秘密：44 个未发布功能（KAIROS、Buddy 宠物系统、Voice Mode……）

导读也在 GitHub 上：

---

### 最后

51 万行源码的核心设计，1300 行 Python 复刻。7 个工具，33 个测试全过。

代码全在 GitHub 上，fork 了随便改：

**GitHub：**

觉得有用的话请给个 Star，这对我很重要。

编辑于 2026-04-18 13:58・天津[AI时代下，图形化编程、Python、C++怎么选？](https://zhuanlan.zhihu.com/p/27288158021)

[

在少儿编程学习中，目前 图形化编程、Python、C++这三种语言最为流行，那么在现今这个科技发达、人工智能发展的时代，选择哪门编程语言最有用？怎么选择更适合孩子？学完后对孩子有什...

](https://zhuanlan.zhihu.com/p/27288158021)