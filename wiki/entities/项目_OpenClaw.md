---
type: entity
tags:
  - ai
  - agent
  - openclaw
  - project
summary: "OpenClaw 是一个开源、自托管的 AI Agent 平台，280K+ GitHub Stars，三层架构，支持 20+ 消息渠道。"
sources:
  - "raw/OpenClaw橙皮书_extracted.txt"
updated: "2026-05-14"
---

# 实体：OpenClaw 项目

## 基本信息

- **全称**：OpenClaw
- **类型**：开源 AI Agent 平台（MIT License）
- **创始人**：Peter Steinberger（奥地利开发者）
- **代码规模**：约 43 万行 TypeScript
- **内存占用**：约 1GB（运行时）
- **启动时间**：3-5 秒
- **吉祥物**：龙虾（中文社区称使用 OpenClaw 为「养虾」）

## 关键数据（截至 2026年3月）

| 指标 | 数据 |
|------|------|
| GitHub Stars | 280,000+（全球软件项目第一） |
| Forks | 53,232+ |
| 贡献者 | 1,075+ |
| 内置 Skills | 55 个 |
| ClawHub Skills | 13,700+ |
| 支持消息渠道 | 20+ |
| 最新版本 | v2026.3.8 |

## 发展简史

| 时间 | 事件 |
|------|------|
| 2025年11月 | ClawdBot 诞生，作为 Peter 的周末项目 |
| 2026年1月中旬 | 72 小时爆发增长，获 60,000+ Stars |
| 2026年1月27日 | Anthropic 商标警告，改名 Moltbot |
| 2026年1月30日 | 再次改名 OpenClaw |
| 2026年2月初 | CVE-2026-25253 RCE 漏洞爆发 |
| 2026年2月初 | ClawHavoc 供应链攻击 |
| 2026年2月14日 | Peter 加入 OpenAI |
| 2026年3月3日 | Stars 超过 250K，超越 React 成 GitHub 第一 |
| 2026年3月7日 | v2026.3.7 史诗级更新，89 次提交 |
| 2026年3月8日 | 深圳龙岗 AI 局发布 OpenClaw 支持政策意见稿 |
| 2026年3月9日 | v2026.3.8 安全加固版发布 |

## 技术架构

### 三层架构

- **Gateway**：中央控制平面，维护 WebSocket 服务、管理 Session、调度 Agent。默认绑定 `ws://127.0.0.1:18789`
- **Node**：设备端执行节点，负责摄像头、录屏、系统命令等本地操作
- **Channel**：消息渠道接入层，连接 WhatsApp、Telegram、Discord、Slack、飞书、钉钉等 20+ 平台

### 设计哲学

- **Unix 哲学**：CLI 是 Agent 连接世界的终极接口
- **极简工具集**：核心只有 Read / Write / Edit / Bash 四个工具
- **反 MCP 立场**：故意不支持 MCP 协议，通过 Bash 直接调用 CLI
- **自我扩展**：Agent 可在运行时写、重载、测试自己的扩展
- **Session 树形结构**：支持分支 side-quest，不污染主对话上下文

## 记忆系统

四层记忆架构：SOUL（不可变）→ TOOLS（按需加载）→ USER（语义长期记忆）→ Session（实时对话），详见 [[concepts/概念_Agent记忆|Agent 记忆]]。

## 相关生态

- **Moltbook**：AI Agent 社交网络，截至 2026年2月有 32,912 个注册 AI Agent
- **ClawHub**：技能市场，13,729 个注册 Skills
- **openclaw-china**：一站式国内平台支持插件
- **养虾文化**：中文社区将运行 OpenClaw 称为「养虾」，用户自称「养虾人」

## 关联页面

- [[sources/来源_OpenClaw橙皮书|来源：OpenClaw橙皮书]]
- [[concepts/概念_AI_Agent|AI Agent]]
- [[concepts/概念_Agent记忆|Agent 记忆]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_Skill系统|Skill 系统]]
- [[overview/知识图谱|知识图谱]]
