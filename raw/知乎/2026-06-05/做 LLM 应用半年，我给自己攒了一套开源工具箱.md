---
title: "做 LLM 应用半年，我给自己攒了一套开源工具箱"
source: "https://zhuanlan.zhihu.com/p/2041886052420409269?share_code=QQSFYdLkxoJx&utm_psn=2042672072292300290"
author:
  - "[[何宇峰月之暗面 Kimi AI Agent 工程师]]"
published:
created: 2026-06-05
description: "做 LLM 应用做了一年，写了不少代码，也烧了不少 API 费用。 过程中反复被一些小问题折腾：想给开源项目贡献代码但不知道从哪找合适的 issue；每次换项目都要手写一遍 AI 助手的规则文件；改了 prompt 不确定效果…"
tags:
  - "clippings"
---
做 LLM 应用做了一年，写了不少代码，也烧了不少 [API 费用](https://zhida.zhihu.com/search?content_id=275470337&content_type=Article&match_order=1&q=API+%E8%B4%B9%E7%94%A8&zhida_source=entity) 。

过程中反复被一些小问题折腾：想给开源项目贡献代码但不知道从哪找合适的 issue；每次换项目都要手写一遍 AI 助手的规则文件；改了 prompt 不确定效果是变好还是变坏；月底一看 API 账单吓一跳但不知道钱花在哪了；有几千条数据要批量用 LLM 处理但手动写 async retry 太烦。

每个问题都不大，但每次遇到都浪费时间。于是一个一个做成了工具，全部开源。pip install 一行装好，解决一个痛点。

### GitSense — 找开源贡献机会

GitHub：

我给 vLLM、PyTorch 等项目贡献了 35 个 merged PR，很多 issue 就是 GitSense 帮我发现的。

它做的事情很简单：扫 GitHub 上的热门仓库，找到 open 且没人认领的 issue，然后用 LLM 分析这些 issue 跟你的技能有多匹配（1-10 分打分），还会给出怎么下手的建议。现在还加了 repo radar，用来判断一个仓库值不值得花周末去投 PR：维护活跃度、review 节奏、issue 质量、PR merge 倾向都会看。

```
pip install gitsense-radar
gitsense scan --skills "python,pytorch,llm" --min-stars 1000
```

想刷开源履历的、准备面试的、参加 hackathon 的，都能用。不想烧 LLM API 费用也行，加 –no-llm 纯用 GitHub 搜索。

### RuleForge — 自动生成 AI 助手规则

GitHub：

用 Claude Code 的人都知道 CLAUDE.md 的重要性。用 Cursor 的人也知道.cursorrules。但手写这些规则文件很麻烦——你得把项目的语言、框架、lint 配置、测试框架、CI 系统全写清楚。

RuleForge 自动扫描你的代码库，检测 20+ 种语言和 30+ 种框架，生成对应的规则文件。

```
pip install ruleforge
ruleforge scan . --format claude  # 生成 CLAUDE.md
ruleforge scan . --format cursor  # 生成 .cursorrules
```

不是通用模板，是根据你项目实际用了什么生成的。用了 pytest 就会写 pytest 相关的规则，用了 Docker 就会加 Docker 的约束。

### PromptDiff — Prompt 的语义 Diff

GitHub：

改 prompt 是 LLM 开发最常见的操作，但改完之后效果是变好了还是变差了？大多数人靠感觉。

PromptDiff 把两个版本的 prompt 跑同一组测试用例，用 embedding cosine similarity 做语义比较，还能用 LLM-as-judge 分类哪些是改进、哪些是退步。

```
pip install "promptdiff[semantic]"
promptdiff compare v1.txt v2.txt --test-cases cases.jsonl
```

终端里直接出 diff 报告，也能输出 JSON 接 CI。每次改 prompt 之前跑一下，心里有数。

### TokenTracker — LLM 花费追踪

GitHub：

改一行 import 就能追踪所有 LLM 调用的 token 和花费。

```
# 之前
from openai import OpenAI
# 之后
from tokentracker import OpenAI  # 就改这一行
```

零配置，数据存本地 SQLite，不传任何东西到云端。CLI 看按天、按模型的花费明细。做量化实验的时候用它盯着 API 费用特别有用。

### BatchLLM — 批量调 LLM

GitHub：

CSV 丢进去，每行用 LLM 处理一遍，结果写回去。自动并发、自动重试、断点恢复。

```
pip install batchllm
batchllm run data.csv --prompt "总结这段文本：{text}" --model gpt-4o
```

处理一万行数据跑到一半断了？下次从断点继续，不重复处理已完成的行。跑之前还能用 batchllm estimate 估算要花多少钱。

### 串在一起用

这五个工具我自己的使用链路是这样的：

GitSense 找到值得贡献的 issue → 用 RuleForge 生成 AI 助手规则 → 用 PromptDiff 调试 prompt → 用 BatchLLM 批量处理评测数据 → 用 TokenTracker 盯 API 花费

不是每个场景都用得上全部五个，但做 LLM 开发总会在某个环节需要其中一两个。

### 这些工具背后的项目

这五个小工具是做大项目过程中”顺手”写的。大项目是什么？

CoreCoder（ [github.com/he-yufeng/Co](https://link.zhihu.com/?target=http%3A//github.com/he-yufeng/CoreCoder) ，690+ star）——从 Claude Code 51 万行源码里提炼的 1400 行 Python 核心重写。 [AnyCoder](https://zhida.zhihu.com/search?content_id=275470337&content_type=Article&match_order=1&q=AnyCoder&zhida_source=entity) 在它基础上做了日用版本，支持 100+ 模型。ContractGuard（100+ star）是 AI 合同审查，FindJobs-Agent（200+ star）是大厂岗位智能匹配。

这些项目的开发和测试过程就是上面五个工具诞生的原因。GitSense 帮我找 issue，RuleForge 帮我配 AI 助手规则，PromptDiff 帮我调 prompt，TokenTracker 帮我盯成本，BatchLLM 帮我跑评测数据。工具是副产品，但用起来比主项目还频繁。

GitHub 主页：

编辑于 2026-05-24 14:26・中国香港[为什么建议不要申请香港高才通计划？一文说清香港高才的续签难度、利弊、骗局](https://zhuanlan.zhihu.com/p/1916822769468932889)

[

香港高才通计划虽以“审批快、门槛明确”吸引人才，但存在显著续签风险、政策变数及市场乱象，今天我们就关键问题给大家做...

](https://zhuanlan.zhihu.com/p/1916822769468932889)