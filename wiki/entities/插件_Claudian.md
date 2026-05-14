---
type: entity
tags:
  - obsidian
  - plugin
  - claudian
  - ai
  - tool
summary: "Claudian 是一个 Obsidian 插件，在侧边栏嵌入 AI 编程代理（Claude Code / Codex / Opencode），支持行内编辑、Plan Mode 和多标签对话。"
sources:
  - "raw/抖音/2026-05-14/抖音-视频-20260514-Obsidian从0到1完整攻略.md"
updated: "2026-05-14"
---

# 实体：Claudian 插件

## 基本信息

- **全称**：Claudian
- **类型**：Obsidian 社区插件
- **GitHub**：[YishenTu/claudian](https://github.com/YishenTu/claudian)
- **功能**：在 Obsidian 侧边栏嵌入 AI 编程代理
- **前置依赖**：需先安装 Claude Code CLI / Codex CLI

## 核心功能

| 功能 | 说明 |
|------|------|
| **AI 侧边栏** | 在 Obsidian 内直接与 AI 对话，Vault 作为工作目录 |
| **行内编辑** | 选中文本 → 快捷键 → AI 直接修改 + word-level diff 预览 |
| **@mention** | 提及文件、子代理、MCP 服务器 |
| **Plan Mode** | 先规划再执行（Shift+Tab 切换模式） |
| **多标签** | 支持 fork、resume、compact 对话历史 |

## 支持的 AI 后端

- Claude Code CLI
- Codex CLI
- Opencode

## 安装方式

1. Obsidian → 设置 → 社区插件 → 搜索 "Claudian"
2. 或从 GitHub Releases 下载 `main.js`、`manifest.json`、`styles.css` 手动放入 `.obsidian/plugins/claudian/`

## 关联页面

- [[sources/来源_Obsidian攻略|来源：Obsidian 攻略]]
- [[overview/知识图谱|知识图谱]]
