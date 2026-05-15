---
type: source
source: "https://zhuanlan.zhihu.com/p/2022738059486143021"
author: "SmartCode得物技术"
created: 2026-05-14
tags:
  - source
  - 知乎
  - MCP
  - Skill
  - 日志诊断
  - 实践
related:
  - "[[concepts/概念_Skill系统|Skill 系统]]"
  - "[[concepts/概念_FunctionCalling|Function Calling]]"
  - "[[concepts/概念_工具调用|工具调用与执行]]"
  - "[[concepts/概念_Agent架构模式|Agent 架构模式]]"
---

# 来源：日志诊断 Skill

> 得物技术团队将 MCP 日志平台接入与 Claude Code Skill 模式结合，实现「查日志 → 找关键信息 → 扫描代码 → 定位问题」全自动闭环。

## 核心架构

### 日志平台 MCP

基于 MCP（Model Context Protocol）协议的日志查询服务，Claude Code 通过 SSE 长连接与 MCP Server 通信：

- **鉴权流程**：secretKey → acquireTokenTool → accessToken（1小时有效，最多5个同时存在）
- **核心工具**：logsQuery / logSqlQuery / countLogTool / logFields
- **queryString 语法**：支持精确匹配（=）、模糊匹配（≈）、AND/OR/NOT 连接符

### /log-diagnosis Skill 工作流

```
用户输入 → Claude 加载 SKILL.md → 读取配置 → 检查 Token
→ 计算时间范围 → MCP 分页拉取全量日志（最多20页）
→ 切换代码分支 → 检索代码 → 综合分析 → 诊断报告
→ 恢复原始代码分支
```

### 核心能力

- **Token 自动管理**：accessToken 过期自动刷新
- **分页全量拉取**：禁止只查第一页就下结论
- **跨服务分析**：自动识别上下游服务，拉取关联日志交叉验证
- **代码联动**：日志类名/方法名精确定位到代码

## 关键洞见

> **"MCP 给数据，Skill 给流程"** — 协议层（MCP）解决数据获取问题，规范层（Skill）解决分析流程问题，两者组合形成完整的诊断闭环。

### 实战案例

SQL BUG 诊断：customer_tag 字段在 MyBatis XML 中缺少空字符串判断（''），导致查询条件被跳过并返回全表数据。Skill 自动完成从日志到代码到根因的全链路分析。

### Skill 目录结构

```
.claude/skills/log-diagnosis/
├── SKILL.md        # 技能行为规范（核心）
├── README.md       # 使用说明
└── reference.md    # 附录：时间脚本、queryString 示例
```

## 相关概念

- [[concepts/概念_Skill系统|Skill 系统]] — 核心：将固定流程封装为可复用模块
- [[concepts/概念_FunctionCalling|Function Calling]] — MCP 本质是标准化的工具调用协议
- [[concepts/概念_工具调用|工具调用与执行]] — MCP 作为工具调用的工程实践
