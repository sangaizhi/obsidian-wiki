---
type: source
tags:
  - ai
  - agent
  - openclaw
  - source
summary: "OpenClaw 橙皮书，从入门到精通的全方位参考手册，涵盖架构原理、部署方案、渠道接入、Skills系统、模型配置、安全与成本。"
sources:
  - "raw/OpenClaw橙皮书_extracted.txt"
updated: "2026-05-14"
---

# 来源：OpenClaw橙皮书

## 来源信息

- **原始文件**：`raw/OpenClaw橙皮书_extracted.txt`
- **主题**：OpenClaw AI Agent 平台的完整使用指南
- **版本**：v1.1.0，适用 OpenClaw v2026.3.8
- **发布时间**：2026-03-11
- **作者**：花叔（B站/YouTube：AI进化论-花生，公众号：花叔）

## 核心要点

### 关于 OpenClaw

- OpenClaw 是一个**开源、自托管的 AI Agent 系统**，让 AI 从"聊天工具"变成"能自主执行任务的数字员工"。
- 采用 MIT 许可证，截至 2026年3月拥有 280,000+ GitHub Stars（全球软件项目第一），从零到第一只用了不到 5 个月。
- 创始人 Peter Steinberger（奥地利开发者），2026年2月加入 OpenAI，项目移交开源基金会运营。

### 技术架构

- **三层架构**：Gateway（中央控制平面）→ Node（设备端执行节点）→ Channel（消息渠道接入层），以 WebSocket 为通信总线。
- **Loopback-First 设计**：Gateway 默认只绑定 localhost，天然安全；远程访问通过 Tailscale 等方案。
- **极简工具集**：核心只有 Read、Write、Edit、Bash 四个工具，遵循 Unix 哲学。

### 记忆系统

- **四层记忆架构**：SOUL（不可变人格内核）→ TOOLS（动态工具）→ USER（语义长期记忆）→ Session（实时对话）。
- **Daily Logs**：每天的交互以 append-only 方式写入 `memory/YYYY-MM-DD.md`。
- **Pre-Compaction**：Session 接近 Token 限制时自动触发静默压缩，将重要记忆持久化。
- **向量记忆搜索**：结合 Embedding 语义搜索 + BM25 关键词搜索，底层使用 SQLite-vec。

### Skills 系统

- **三层优先级**：工作区级 > 用户级 > 内置（55 个内置 Skills）。
- **ClawHub 技能市场**：13,729 个注册技能，但超过 50% 为垃圾/重复/低质量，800+ 被标记为恶意。
- **自建 Skill**：最小单位是一个目录 + `SKILL.md` 文件。
- **自我扩展能力**：Agent 可在运行时写、重载、测试自己的扩展。

### 设计哲学

- **Unix 哲学**：小工具、可组合、文本流。CLI 是 Agent 连接世界的终极接口。
- **反 MCP 立场**：故意不支持 MCP 协议，认为 CLI/Unix 才是真正能 scale 的方案。
- **Session 树形结构**：支持分支和 side-quest，不污染主对话上下文。

### 安全与风险

- CVE-2026-25253 RCE 漏洞（CVSS 8.8），13.5 万暴露实例中 5 万+ 可被直接攻击。
- ClawHavoc 供应链攻击，ClawHub 约 12% 的 Skills 被确认为恶意。
- v2026.3.7 起强制 Gateway 认证，修复了 30,000+ 未认证实例的安全隐患。

## 关联页面

- [[entities/项目_OpenClaw|OpenClaw 项目]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_Skill系统|Skill 系统]]
- [[concepts/概念_Agent记忆|Agent 记忆]]
- [[concepts/概念_AI_Agent|AI Agent]]
- [[overview/知识图谱|知识图谱]]
