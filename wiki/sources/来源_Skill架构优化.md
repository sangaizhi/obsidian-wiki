---
type: source
tags:
  - ai
  - agent
  - skill
  - system-prompt
  - architecture
  - source
summary: "上百个 Skill 塞爆 System Prompt 时的架构重构方案：元数据瘦身、分层加载、Skill Gating、上下文压缩。"
sources:
  - "raw/抖音/2026-05-14/抖音-视频-20260514-Skill系统架构优化.md"
updated: "2026-05-14"
---

# 来源：Skill 系统架构优化

## 来源信息

- **原始文件**：`raw/抖音/2026-05-14/抖音-视频-20260514-Skill系统架构优化.md`
- **平台**：抖音短视频
- **日期**：2026-05-14
- **视频ID**：7638562818790313267
- **主题**：Skill 元数据管理、分层加载、Context Window 优化

## 核心要点

### 核心问题

上百个 Skill 的元数据全部塞进 System Prompt → 上下文窗口被占满 → AI 反而变笨。

### 五大优化策略

**① 元数据瘦身（Metadata Diet）**
- description 精简至触发场景，不写具体操作 → 节省 60-80% Token
- 只保留 name + description + triggers，去掉冗余字段

**② 分层加载（Layered Loading）**
- 第一层：常驻上下文的轻量目录（每行 < 20 Token）
- 第二层：按需加载完整 SKILL.md 内容
- 核心原则：目录常驻 + 内容按需

**③ Skill Gating（过滤机制）**
根据操作系统、工具是否存在、环境变量、配置项、用户角色等条件过滤不可用的 Skill。

**④ Skill 与 CLI 配合**
- MCP = 工具常驻桌面（方便但占空间）
- CLI = 工具放柜子里（按需取用）
- Skill = 操作手册（让 AI 知道怎么用）

**⑤ 上下文压缩**
语义压缩、结构化索引、优先级排序、动态替换。

### 优化前后对比

| 维度 | 优化前 | 优化后 |
|------|--------|--------|
| Context Window 占用 | ~30,000+ Token | ~2,000 Token（索引目录） |
| AI 响应质量 | 下降 | 提升 |
| 新增 Skill 成本 | 高 | 低 |
| 匹配准确率 | 低 | 高 |

## 关联页面

- [[concepts/概念_Skill系统|Skill 系统]]
- [[concepts/概念_上下文工程|上下文工程]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[entities/项目_OpenClaw|OpenClaw 项目]]
