---
type: source
tags:
  - ai
  - agent
  - claude-code
  - skill
  - claude.md
  - progressive-disclosure
  - trigger
  - engineering
  - dynamic-context
  - design-patterns
  - software-engineering
summary: "Claude Code 实战系列第三章（全文 12 节）：Skills 工程实践全链路。从知识两维度、工程化结构、渐进式披露、触发机制，到动态上下文注入、4 种设计模式、2 个实战案例、测试迭代方法和软件工程五大原则映射。"
sources:
  - "raw/ai/ClaudeCode实战/Chapter3 授人以渔：Skills工程实践.md"
updated: "2026-06-23"
---

# 来源：Claude Code 实战 — Skills 工程实践

> Claude Code 实战系列 Chapter 3（全文 12 节），聚焦 Skills 工程化实践，从知识管理维度到软件工程本质的全链路拆解。

## 核心要点

1. **知识的两维度**（§3.1）：Claude.md = 企业规章制度（常驻知识，全量加载），Skills = 岗位 SOP（按需知识，渐进式加载）。
2. **SKILL 工程化结构**（§3.2）：Skill 是文件夹而非字符串。kebab-case 命名，SKILL.md 全大写。元数据三维度（触发/权限/运行时）。
3. **渐进式披露**（§3.3）：图书馆三层模型。description 预算为上下文窗口的 2%，平分给所有 Skill，超出者被静默排除。
4. **触发机制**（§3.4）：双通道激活（显式 + 语义匹配）。description 三要素公式（What + When + Not for）。过/欠触发诊断。参考型 vs 任务型。
5. **SKILL.md 正文设计**（§3.5~3.6）：路由器思维、契约式引用、500 行法则。
6. **动态上下文**（§3.7）：$ARGUMENTS 变量 + `!`command`` 运行时 Shell 注入，自动填充分支名、提交记录等上下文，配合 allowed-tools 构建安全围栏。
7. **作用域与优先级**（§3.8）：企业 > 个人 > 项目 > Plugin 四级层级。纳入版本控制实现团队即插即用。
8. **4 种设计模式**（§3.9）：模板驱动（标准化输出）、脚本增强（确定性计算替代 LLM 推理）、知识分层（8/2 法则按需加载）、工具隔离（allowed-tools 安全边界）。
9. **实战案例**（§3.10）：代码审查 Skill（三级优先级：安全→性能→质量）+ 智能提交 Skill（任务型 + disable-model-invocation + !`command` 注入）。
10. **测试与迭代**（§3.11）：触发测试（10+/10-，≥90%/+≤5%）、功能测试、性能对比（5 次 A/B）。修正逻辑写入 SKILL.md 形成迭代闭环。
11. **软件工程视角**（§3.12）：关注点分离（三层架构）、依赖倒置（description=接口）、惰性加载（渐进式披露）、最小权限（allowed-tools）、开放标准（声明式+自包含+知识本位）。

## 关键引文

> "Skills 集中于特定领域的知识，解决的是**知识的按需投放问题**。"

> "SKILL.md 是路由器，不是知识仓库。"

> "\"副作用\"越大，控制权越要收紧。"

> "500 行代码约为 2000~3000 Tokens。若超过 500 行，意味着'参考资料'和'路由指令'混淆了。"

> "如果发现自己在 SKILL.md 中编写公式让大模型运行计算，请立即停止——该逻辑应当被移至脚本中。"

> "严禁将所有逻辑写入 CLAUDE.md——就像不要把所有代码写在 main 函数里。"

## 关联页面

### 概念页（9 个）
- [[concepts/概念_渐进式披露|渐进式披露]] — §3.3 三层加载模型与预算机制
- [[concepts/概念_Skill触发机制|Skill 触发机制]] — §3.4 双通道激活与 description 设计
- [[concepts/概念_Skill工程设计|Skill 工程设计]] — §3.2/§3.5/§3.8 目录规范、路由器思维、作用域
- [[concepts/概念_Skill动态上下文|Skill 动态上下文]] — §3.7 变量与 !`command` 注入
- [[concepts/概念_Skill设计模式|Skill 设计模式]] — §3.9 4 种设计模式+决策树
- [[concepts/概念_Skill实战案例|Skill 实战案例]] — §3.10 代码审查+智能提交
- [[concepts/概念_Skill测试与迭代|Skill 测试与迭代]] — §3.11 三类测试+迭代闭环
- [[concepts/概念_Skill与软件工程|Skill 与软件工程]] — §3.12 五大工程原则
- [[concepts/概念_Skill系统|Skill 系统]] — 跨来源的 Skill 完整知识体系

### 比较与实体
- [[comparisons/ClaudeMD_vs_Skills|Claude.md vs Skills]] — §3.1 知识两维度的详细比较
- [[entities/项目_ClaudeCode|Claude Code 项目]] — Claude Code 的 Skill 机制实现方
