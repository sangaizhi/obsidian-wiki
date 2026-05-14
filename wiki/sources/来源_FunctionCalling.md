---
type: source
tags:
  - ai
  - agent
  - function-calling
  - tool-use
  - source
summary: "Function Calling 原理、5 大常见翻车场景、Schema 设计最佳实践、面试高频考点、OpenAI vs Anthropic 对比。"
sources:
  - "raw/douyin/2026-05-14/抖音-视频-20260514-FunctionCalling.md"
updated: "2026-05-14"
---

# 来源：Function Calling 深度解析

## 来源信息

- **原始文件**：`raw/douyin/2026-05-14/抖音-视频-20260514-FunctionCalling.md`
- **平台**：抖音短视频
- **日期**：2026-05-14
- **链接**：https://v.douyin.com/mhEimpPgzhw/
- **创作者**：小哲讲 agent
- **主题**：Function Calling 原理、常见陷阱与面试要点

## 核心要点

### Function Calling 流程

1. LLM 收到请求 + 工具列表（name + description + parameters）
2. LLM 推理判断是否需要调用工具
3. 需要 → 返回 `function_call`（tool_name + arguments）
4. 应用层解析 JSON，执行对应函数
5. 将执行结果注入下一轮 LLM 调用上下文
6. LLM 基于结果继续推理

### 5 大翻车场景

| 场景 | 原因 | 解决思路 |
|------|------|---------|
| 参数生成错误 | description 不清晰 | 写清格式约束和必填指引 |
| 工具选择错误 | 工具语义相似 | 区分使用场景，差异化名称 |
| 循环调用 | 结果不理想反复重试 | 设置 max_turns，附带后续建议 |
| 上下文污染 | 中间结果占满窗口 | 工具结果摘要化、分层记忆 |
| 描述过于简单 | LLM 不知何时用 | 写使用场景 + 排除场景 |

### OpenAI vs Anthropic

| 特性 | OpenAI | Anthropic Claude |
|------|--------|------------------|
| 接口名 | `function_call` / `tool_calls` | `tool_use` / `tool_result` |
| 并行调用 | ✅ | ✅ |
| MCP 支持 | 第三方 | 原生支持 |
| extended thinking | 需 reasoning_effort | ✅ 内置 |

### MCP 协议的优势

- 动态发现：不用硬编码所有工具
- 标准化：工具描述格式统一
- 安全传输：OAuth token 在 Vault 中
- 去中心化：不同工具部署在不同 Server

## 关联页面

- [[concepts/概念_FunctionCalling|Function Calling]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_ManagedAgents|Managed Agents]]
- [[concepts/概念_Harness工程|Harness Engineering]]
