# 从玩具到生产力：用真实项目讲透 AI Agent 的 Harness Engineering

> **来源：** 知乎专栏
> **日期：** 2026-05-14
> **链接：** https://zhuanlan.zhihu.com/p/2035659924911421217
> **抓取时间：** 2026-05-14 15:30

---

这篇文章不讲 Prompt 技巧，也不推销某个 Skill，只想说清两件事——在企业工程环境里，如何把大模型 Harness（约束与治理）成一个能持续参与交付的协作者；以及大模型时代，程序员为什么正在从"亲手写代码的人"迁移成"定义目标、控节奏、做验收的人"。

---

## 一、什么是 Harness Engineering？

Harness 不是某条提示词、某个工具，也不是多写几份文档。**它是一整套把大模型纳入工程体系的控制面：**

- 如何提供唯一的真相源？
- 如何约束执行边界？
- 如何接入业务能力（Capability）？
- 如何观测、调试运行状态？
- 如何让产出可验证、可回归，让其他工程师能接手？

### 关键边界

| | 传统软件工程 | Harness Engineering |
|--|------------|-------------------|
| 管理对象 | 确定性（代码逻辑） | 非确定性（概率引擎） |
| 目标 | 防止人犯错 | 约束模型不失控 |
| 工具 | 类型系统、单测、CR | 沙盒、Checkpoint、外部验证 |

> 传统软件工程管的是「确定性」，Harness Engineering 管的是「非确定性」。

---

## 二、架构坐标系

两个坐标轴：
- **X轴（执行流路由）：** 静态预设 vs. 动态自主
- **Y轴（状态与上下文）：** 隐式内部 vs. 显式外部

**四个象限：**
1. **象限一（Harness Engineering）：** 模型提供意图，外部 Harness 负责状态隔离与沙盒校验 ✅ 推荐
2. **象限二（提示词驱动）：** AutoGPT、原生 ReAct，模型自主性高
3. **象限三（无状态链）：** 单次 API 调用，LLM 当纯函数
4. **象限四（传统管道）：** LangChain 顺序链，外部状态管理严谨

---

## 三、避坑指南

### 伪 Harness（不是 Harness + 坏做法）
- **"软约束"陷阱：** 在 Prompt 写 5000 字 DO NOT——只是口头嘱咐
- **"军火库"陷阱：** 塞 20 个 API 让模型自己挑——没有边界约束

### 劣质 Harness（是 Harness 但质量差）
- **"盲打"陷阱：** 暴力死循环重试——模型可能为了修语法错把架构删了
- **"官僚主义"陷阱：** 强制重型文档流——浪费 Token，一变即成垃圾

### 好的 Harness 三要素
1. **前置验证（Evaluator 沙盒）：** 基于证据触发 Retry
2. **最小真相源（Spec is Truth）：** 任务跨天能无损恢复
3. **物理门禁（Checkpoint Before Execute）：** 破坏前必须授权

---

## 四、为什么企业环境里 Harness 比 Prompt 更重要？

本地 Demo 缺陷可以被掩盖，但企业环境：
- 链路长、边界严（内部鉴权）
- 试错成本高
- Agent 挑战：调的是不是正确接口？失败能不能自愈？

> **Prompt 是指令，Harness 是约束——前者在模型脑子里，后者在模型外面。**

---

## 五、Aegis 案例：真实项目中如何 Harness Agent

### 1. 起步：先收敛目标，不急着编码
> "这个项目是一个空的 Python 项目，请阅读架构设计文档，了解我想做什么，然后向我复述需求并讨论。"

### 2. 连续开发：Spec + Handoff 对抗上下文腐烂
- Handoff 文档构成 Agent 的"外部持久化记忆"
- 每轮对话从"阅读 handoff 恢复上下文"开始

### 3. 执行：将 Prompt 溶解进 Capability 框架
- 一个 Capability = 专属 Prompt + Python 脚本 + Validator
- 把分支拆成独立管道，Agent 做轻量路由决策

### 4. 运行：跨越"能聊"与"能跑"的分水岭
- 真实环境：504 超时、403 拦截、SSE 静默退出
- 处理方式：引导 Agent 做链路排错，而非调 Prompt 语气

### 5. 交付：测试与回归前置化
- 测试不再是收尾动作，而是工作轨道本身

---

## 六、sdd-riper-one-light 的实施协议

Skill 地址：https://github.com/huisezhiyin/sdd-riper/tree/main/skills/sdd-riper-one-light

三个契约：
1. **前置断言（Pre-conditions）** — 强制 Checkpoint + Restate First
2. **后置断言（Post-conditions）** — 闭环回写，基于证据验证
3. **不变式（Invariants）** — 维护最小真相源，对抗跨周期状态腐烂

---

## 七、行业印证

| 团队 | 做法 |
|------|------|
| OpenAI Engineering | 代码仓库作为唯一记录系统，人类变"环境设计师" |
| Anthropic Labs | 强制 Context Resets + 剥离执行者与验证者 |
| 某大厂 deer-flow | "Super Agent Harness"——Docker 沙盒 + SKILL.md 按需加载 + LangGraph 编排 |

---

## 八、从 0 到 1 落地路径

1. **先搭真相源** — Spec 和状态文档
2. **约束执行边界** — Checkpoint + Approval
3. **构建最小能力目录** — 明确 Tool 和接口边界
4. **前置验证闭环** — 单测、回归、日志检索
5. **完善恢复机制** — Handoff 流程
6. **逐步释放自由度** — 先铺轨道，再追速度

---

## 附：8 阶段 SOP

| 阶段 | 输入 | 要求返回 | 控制动作 |
|------|------|---------|---------|
| 目标收敛 | 先读文档 | 需求复述、主线判断 | 先纠偏再放行 |
| 状态恢复 | 读 Spec/Handoff | 已完成项、未完成项 | 用外部真相源恢复 |
| 上下文装配 | 只给索引 | 最小上下文清单 | 按需补充 |
| 任务分块 | 只做一段 | 1-3 个动作、风险 | 只批准当前轮次 |
| 链路设计 | 判断模式 | 执行方案 | 定路线不改 Prompt |
| 执行前校准 | Checkpoint | 理解、目标、风险 | 对齐后 Approval |
| 外部验证 | 不接受主观 | 基于日志、测试 | 用证据决策 |
| 回写交接 | 回写完成项 | 偏差、残留、下一步 | 留干净恢复点 |

### 三层目标管理

| 层级 | 职责 |
|------|------|
| **总核心目标** | 整个项目要完成什么 |
| **阶段性核心目标** | 当前几轮只收敛什么 |
| **本轮动作目标** | 这一轮只做哪 1-3 个动作 |

### 偏航的 4 个信号
1. 开始绕过阶段目标，直接谈总目标
2. 跳过中间产物，直接要改代码
3. 用主观语气替代客观证据
4. 混淆阶段完成和全局完成

### 可直接照抄的句式

```
# 起手收敛
先读架构设计文档，不要实现。复述你理解的目标和项目主线。

# 压最小 spec
先把这轮压成最小 spec，没有批准不要进入实现。

# 执行前 checkpoint
先别改代码。做 checkpoint：当前理解、下一步、风险、验证方式。

# 发现偏航
先停。复述这轮阶段性核心目标，不要谈总目标。

# 基于证据验证
去看测试、日志、接口回包，基于事实回答。

# 阶段验收
明确说：这轮完成了什么，还剩什么，下一轮最小目标是什么。
```

---

## 核心结论

> **今天的大模型已经够强，可以参与研发交付；但没有 Harness，它充其量是个高级玩具；有了 Harness，它才能成为研发链路中的协作者。**

> **程序员的核心价值正在从"亲手写出每一行代码"，转向"定义目标、卡住边界、掌控节奏、验收结果"。**

---

## 参考链接

- [OpenAI Engineering：在智能体优先的世界中利用 Codex](https://openai.com/zh-Hans-CN/index/harness-engineering/)
- [Anthropic Labs：Harness design for long-running application development](https://www.anthropic.com/engineering/harness-design-long-running-apps)
- [bytedance/deer-flow: SuperAgent Harness](https://github.com/bytedance/deer-flow)
- [huisezhiyin/sdd-riper-one-light](https://github.com/huisezhiyin/sdd-riper/tree/main/skills/sdd-riper-one-light)
