---
type: entity
entity: 工具
name: LLM 开源工具箱
created: 2026-06-05
tags:
  - entity
  - tool
  - llm-toolbox
  - open-source
related:
  - "[[concepts/概念_工具调用|工具调用与执行]]"
  - "[[concepts/概念_Skill系统|Skill 系统]]"
  - "[[entities/项目_ClaudeCode|Claude Code 项目]]"
sources:
  - "[[sources/来源_LLM开源工具箱|来源：LLM 开源工具箱]]"
---

# LLM 开源工具箱

> 月之暗面 Kimi AI Agent 工程师何宇峰在做 LLM 应用过程中攒的一套开源工具链，全部 `pip install` 一行安装。

## 五件套工具

### GitSense — 找开源贡献机会

- 扫 GitHub 热门仓库的 open issue，用 LLM 分析与个人技能的匹配度（1-10 分打分）
- Repo Radar：评估仓库的维护活跃度、review 节奏、PR merge 倾向
- 支持 `--no-llm` 纯 GitHub 搜索模式，不烧 API 费用
- 安装：`pip install gitsense-radar`

### RuleForge — 自动生成 AI 助手规则

- 扫描代码库，检测 20+ 语言和 30+ 框架，自动生成 CLAUDE.md 或 .cursorrules
- 非通用模板，根据项目实际使用情况生成：用了 pytest 写 pytest 规则，用了 Docker 加 Docker 约束
- 安装：`pip install ruleforge`

### PromptDiff — Prompt 的语义 Diff

- 两个版本 prompt 跑同一组测试用例，用 embedding cosine similarity 做语义比较
- LLM-as-judge 分类改进和退步，终端出 diff 报告，支持 JSON 接 CI
- 安装：`pip install "promptdiff[semantic]"`

### TokenTracker — LLM 花费追踪

- 改一行 import 即可追踪所有 LLM 调用的 token 和花费
- 数据存本地 SQLite，零配置，不上传云端
- CLI 按天 / 按模型查看花费明细
- 安装：`pip install tokentracker`

### BatchLLM — 批量调 LLM

- CSV 丢进去，每行用 LLM 处理一遍，自动并发 + 自动重试 + 断点恢复
- 支持 `batchllm estimate` 预估费用
- 安装：`pip install batchllm`

## 使用链路

```
GitSense（找 issue） → RuleForge（配规则） → PromptDiff（调 prompt） → BatchLLM（跑评测） → TokenTracker（盯成本）
```

## 背后项目

这些工具是何宇峰做大项目时的「副产品」：

| 项目 | Star | 说明 |
|------|------|------|
| **CoreCoder** | 690+ | 从 Claude Code 51 万行源码提炼的 1400 行 Python 核心重写 |
| **AnyCoder** | — | 基于 CoreCoder 的日用版本，支持 100+ 模型 |
| **ContractGuard** | 100+ | AI 合同审查 |
| **FindJobs-Agent** | 200+ | 大厂岗位智能匹配 |

## 相关来源

- [[sources/来源_LLM开源工具箱|来源：LLM 开源工具箱]] — 原始文章
