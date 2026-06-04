---
title: "用 LLM Agent 重构告警排查流程｜得物技术"
source: "https://zhuanlan.zhihu.com/p/2045812718301623091?share_code=GrFNIZ2nYaxI&utm_psn=2045892647211082072"
author:
  - "[[SmartCode得物技术]]"
published:
created: 2026-06-05
description: "一、引言告警来了，第一反应是打开日志平台搜关键词，切到 APM 看监控曲线，再去链路追踪系统找 trace 详情。三个平台来回切换，最后发现只是上游 GC 抖动导致的瞬间超时，一分钟后就自愈了。这类告警排查通常需要…"
tags:
  - "clippings"
---
## 一、引言

告警来了，第一反应是打开日志平台搜关键词，切到 APM 看监控曲线，再去链路追踪系统找 trace 详情。三个平台来回切换，最后发现只是上游 GC 抖动导致的瞬间超时，一分钟后就自愈了。这类告警排查通常需要 10～30 分钟，主要耗时不在分析本身，而在于频繁登录不同平台、拼凑分散的数据。此外，排查效率高度依赖个人经验，新人面对告警往往不知道该先看什么。于是我们做了 Troubleshooter——用 LLM Agent 自动完成告警的数据采集、根因分析和处置建议生成。上线后，中位数排查耗时从 20 分钟左右降到 4.4 分钟，覆盖了 11 个服务和 10+ 种告警类型。这篇文章是对技术方案的详细介绍。

![](https://pic4.zhimg.com/v2-5077bf603492d40b4b7042ab12a73869_1440w.jpg)

## 二、架构设计

整体采用分层设计，核心原则是告警接入与排查执行解耦——接入层只负责接收和持久化，排查由独立的调度器异步触发。

![](https://pic4.zhimg.com/v2-1efae478fa7aa348f459a424fa7b3b9b_1440w.jpg)

### 技术栈：

![](https://pic1.zhimg.com/v2-a661e07c854f774906702281a120e604_1440w.jpg)

Agent 框架选 [Spring AI Alibaba](https://zhida.zhihu.com/search?content_id=276194757&content_type=Article&match_order=1&q=Spring+AI+Alibaba&zhida_source=entity) 而不是自建 ReAct 循环，主要是省事——框架已内置推理循环、工具拦截器、模型拦截器，开箱可用。

## 三、核心流程

### 完整排查链路：

![](https://pic4.zhimg.com/v2-0df5ba87b9e069dac267aca9f6c2a723_1440w.jpg)

### 核心步骤说明：

- **告警接入** ：接收告警数据，生成唯一事件 ID；
- **指纹生成** ：提取 5 维度特征（服务名、告警类型、错误模式、指标名、错误摘要），生成 32 位 MD5 指纹；
- **知识匹配** ：在知识库中检索相似记录，匹配成功则直接复用历史结论；
- **AI 排查** ：Supervisor Agent 编排排查，执行 [ReAct 推理循环](https://zhida.zhihu.com/search?content_id=276194757&content_type=Article&match_order=1&q=ReAct+%E6%8E%A8%E7%90%86%E5%BE%AA%E7%8E%AF&zhida_source=entity) ；
- **结论验收** ：独立 Validation Agent 检查根因明确性和处置建议完整性；
- **报告推送** ：生成 Markdown 格式报告，推送到飞书群组；
- **知识沉淀** ：运维确认后的结论存入知识库。

## 四、AI 排查引擎：ReAct Agent 实战

这是整个系统最核心的部分。我们使用了 Spring AI Alibaba 的 ReactAgent，它实现了经典的 ReAct推理循环：

```
LLM 思考 → 选择工具 → 执行工具 → 观察结果 → 继续思考 → ... → 输出结论
```

### SupervisorAgent：不是简单地调 Prompt

很多人以为「AI 排查」就是构造一个 Prompt 丢给大模型。但实际中，LLM 无法凭空知道你的服务当前 QPS 是多少、错误日志里写了什么、调用链哪个环节超时了。它需要工具。SupervisorAgent 的核心设计：

```
@Component
publicclassSupervisorAgent{

    // 四个 @Tool 方法，暴露给 ReactAgent 框架
    @Tool(description = "查询并分析应用日志，返回排查摘要")
    public String queryLogs(String serviceName, Integer minutes, ...){ ... }

    @Tool(description = "查询服务监控指标（QPS/RT/错误率/CPU/内存/GC）")
    public String queryMetrics(String serviceName, String metricTypes, ...){ ... }

    @Tool(description = "通过 traceId 查询分布式调用链详情")
    public String queryTrace(String traceId){ ... }

    @Tool(description = "无 traceId 时，通过接口路径查询错误日志并提取 traceId")
    public String queryEndpointErrors(String serviceName, String endpoint, ...){ ... }

    // 执行排查的核心方法
    public InvestigationResult investigate(TroubleEvent event){
        // 1. 动态构建 instruction（策略匹配 or 兜底策略）
        String dynamicInstruction = buildDynamicInstruction(event);

        // 2. 构建 ReAct Agent
        ReactAgent agent = ReactAgent.builder()
                .name("supervisor")
                .model(selectedModel)
                .systemPrompt(dynamicInstruction)
                .methodTools(this)   // 四个 @Tool 方法作为可用工具
                .build();

        // 3. 验收循环：最多 2 次 LLM 内容重试
        for (int attempt = 0; attempt <= maxRetries + 2; attempt++) {
            AssistantMessage response = agent.call(currentPrompt);

            // 结论验收
            ValidationResult validation = conclusionValidationAgent
                    .validate(responseText, metricsQueried, eventId);

            if (validation.isPassed()) break;  // 通过，结束

            // 不通过：将反馈注入 prompt，要求 LLM 修正
            currentPrompt = buildRetryPrompt(feedback, responseText);
        }

        return InvestigationResult.complete(responseText, suggestion);
    }
}
```

### 四个排查工具的设计哲学

**工具一：queryLogs——日志查询与分析**

日志是排查的第一入口。它通过 WebSocket 连接日志平台，按优先级分批查询：

```
查询优先级: traceId > exceptionName > endpoint > keywords
```

每批结果都会经过 LLM 异步分析，最后汇总所有批次的结论。此外还有日志去重（LogDeduplicator），防止同一请求的多条日志重复占据分析窗口。

**工具二：queryMetrics——监控指标查询**

支持 10 个维度的指标查询：qps, rt, errorRate, containerCpu, containerMemory, percentileRt, gcCount, gcRt, qpstop10, rttop10。LLM 可以根据告警类型自主决定查询哪些维度。比如 OOM 告警时，LLM 通常会自主选择 qps,rt,containerCpu,containerMemory,gcCount,gcRt 全维度查询；而对于接口 RT 突增，则关注 qps,rt,percentileRt,rttop10。MetricService 接口为不同环境（csprd/oa/pre/t1）提供了可插拔的实现，因为 APM 查询参数中环境代码不同——生产环境用 csprd-proxy，测试环境用 t1-proxy。

**工具三：queryTrace——分布式链路追踪**

当告警带有 traceId 时，此工具直接查询 APM 获取 Span 树，渲染为格式化文本后交给 LLM 分析。它能识别出哪个下游依赖变慢、哪个环节抛出了异常。

**工具四：queryEndpointErrors——接口错误排查**

当没有 traceId 但有明确的接口路径时，这个工具会查询该接口的错误日志，通过正则提取所有 traceId，再逐个分析。有个重要的防护逻辑：当 endpoint 为根路径 "/" 时，拒绝执行——匹配所有请求的查询不具备排查意义。

### 动态策略组装

不同服务、不同告警类型的排查思路差异很大。比如 OOM 告警要关注 GC 指标和堆内存，接口超时要关注 QPS 突变和下游依赖延迟。我们设计了策略匹配机制：按

(service\_name, alert\_type)从数据库精确匹配排查策略，未匹配时使用内置兜底策略。运维人员可以通过前端页在线编辑策略内容，无需改代码。

```
instruction = ROLE + strategy(service_name, alert_type) + OUTPUT_FORMAT
```

### 工具超时隔离

外部系统（日志平台、APM）不一定稳定。每个工具调用通过 ToolExecutor 包装，用独立线程池 + Future.get(timeout) 实现超时控制：

```
public ToolResult executeWithTimeout(Tool tool, Map<String, Object> parameters){
    Future<ToolResult> future = executor.submit(() -> tool.execute(parameters));
    try {
        return future.get(tool.getTimeoutSeconds(), TimeUnit.SECONDS);
    } catch (TimeoutException e) {
        future.cancel(true);
        return ToolResult.timedOut("工具执行超时");
    } catch (ExecutionException e) {
        return ToolResult.failed("工具执行失败: " + e.getCause().getMessage());
    }
}
```

超时时返回降级消息（如"指标查询超时，继续使用已有信息进行排查"），LLM 会基于已有证据继续推进，不会因单个工具超时导致整个排查流程中断。

### AI权限安全保证

超时时返回降级消息（如「指标查询超时，继续使用已有信息进行排查」），LLM 会基于已有证据继续推进，不会因单个工具超时导致整个排查流程中断。

## 五、幻觉控制与结论质量保障

LLM 输出不稳定是落地过程中最大的风险点。我们从四个维度构建了幻觉控制体系。

**规则格式校验（零 LLM 调用）：** 第一轮检查直接检查 5 个必要章节是否存在，以及指标表格格式是否规范。这一步完全不调用 LLM，毫秒级完成。

```
private ValidationResult checkFormat(String conclusion, boolean hasMetricsQuery){
    // 检查 5 个必要章节
    for (String[] keywords : REQUIRED_SECTIONS) {
        if (containsSection(conclusion, keyword)) found = true;
        else missingItems.add("缺少章节「" + keywords[0] + "」");
    }
    // 如果调用过 queryMetrics，必须有指标表格
    if (hasMetricsQuery && !METRIC_TABLE_PATTERN.matcher(conclusion).find()) {
        missingItems.add("缺少指标数据表格");
    }
}
```

**独立验收 Agent：内容质量审查：** 检查项包括：结论是否明确、核心判定是否合理、根因是否有工具证据、建议是否可执行。

**多轮交叉验证：** 不同工具的返回结果必须相互印证才能形成结论：queryLogs 与 queryTrace 交叉、queryLogs 与 queryMetrics 交叉、时间线一致性校验。

**重试机制：** 格式问题不消耗重试配额；内容问题最多重试 2 次；验收 Agent 异常时宽容通过。

## 六、排查过程可观测性

排查过程不能是一个黑盒。我们需要让运维人员看到 AI 每一步在做什么。

### 文件系统日志

每次排查在文件系统中创建独立目录（按事件 ID），LoggerInterceptor 和 ToolInterceptor 记录了所有 LLM 输入/输出和工具调用/返回：

```
logs/evt-20260514153012345-001/
├── raw_alert.txt          # 原始告警消息
├── 00_RECEIVED.log        # 接入记录
├── 01_INVESTIGATION_START.log
├── 03_LLM_SYSTEM_PROMPT.log  # 系统指令
├── 04_LLM_USER_PROMPT.log    # 告警信息
├── 05_LLM_CALL_1.log         # 第1次 LLM 调用
├── 06_工具调用：日志分析.log   # 日志分析工具结果
├── 07_LLM_CALL_2.log         # 第2次 LLM 调用
├── 08_工具调用：指标查询.log
├── 09_LLM_FINAL_RESPONSE.log # 最终结论
├── 10_VALIDATION_START.log
├── 11_VALIDATION_PASS.log
└── 12_CONCLUSION.log
```

### 内存进度追踪

EventProgressTracker 在内存中维护每个事件的实时状态（当前阶段、当前策略描述），前端通过 3 秒轮询获取进度更新。

![](https://picx.zhimg.com/v2-23eac99a02267030f7dcf3082dceba81_1440w.jpg)

## 七、真实排查案例：效率网关超时

下面以一次生产环境网关超时告警为例，完整展示 Troubleshooter 的排查过程。

### 告警信息：

```
服务名: 效率网关
告警类型: 效率网关业务应用30s内接口异常告警
错误信息: 效率网关业务应用30s内接口异常告警，持续0分钟
严重级别: P4
环境: 生产环境
接口路径: /xxxx-admin/daemon/api/xxxx/xxxx/list
TraceID: 无（告警未携带 traceId）
告警时间: 2026-04-23 18:10:36
```

**AI 排查过程：** SupervisorAgent 在 4 分钟内完成了 3 次工具调用 + 1 轮验收修正，逐步逼近根因。 **第 1 次验收（未通过）：** 缺失必填字段，要求补全。 **第 2 次验收（通过）：** LLM 补充了结构化字段，紧急程度为「观察」，置信度为「高」。

![](https://pic4.zhimg.com/v2-f1a968f3d0d7051317660cc385049e75_1440w.jpg)

### AI最终排查结论：

```
排查结论：
本次告警系 舆情管理端 服务底层 gorm-v2 数据库查询严重阻塞
引发 效率网关 30 秒硬性超时，经排查确认为特定多条件
过滤场景下的偶发性慢查询导致的链路中断

紧急程度：观察
判定依据：告警窗口（30分钟）内仅触发 2 次 ERROR，服务整体
QPS/RT/错误率指标均处于正常静默状态，属偶发个案。

置信度：高
依据：分布式调用链完整还原了 99% 耗时集中于 gorm-v2 组件，
网关超时堆栈、响应式流中断机制与业务端多状态过滤 SQL 高度吻合。

根因：
舆情管理端 接口 /api/xxx/xxx/list 在处理
upgrade_quality_status IN (0,1,2,3,5) 多条件过滤 + 分页查询时，
底层 gorm-v2 数据库组件执行严重阻塞，耗时 29.99s，超过 效率
网关 30s 超时阈值，触发 TimeoutException。

影响范围：
- 前端页面（public-xxx-system-fe）的舆情升级列表查询功能
- 30 分钟内 2 次请求失败
- 未引发级联故障，整体服务正常

处置建议：
1. 排查 舆情管理端 该接口的慢查询日志，确认多条件组合过滤
   是否缺失复合索引
2. 检查 效率网关该路由的 timeout 参数配置，补充 fallback 降级
3. 评估多状态组合查询的业务合理性，考虑异步查询或分批拉取
```
![](https://pic4.zhimg.com/v2-0963bb0f80dd2bbda0349b1d01ec2a87_1440w.jpg)

**效果对比：** 整个排查过程从告警接入到输出结论耗时约 4 分钟（工具调用总计 88s + LLM 推理 + 验收）。如果让人工来做，需要依次登录 APM 查链路、看监控、翻日志，保守估计需 10~20 分钟。

## 八、技术难点与踩过的坑

### 环境统一映射

告警信息中的环境标识五花八门：「xxprd」「生产」「XX石」「prd」「xxprd-proxy」实际上指的都是生产环境。而且日志平台和 APM 平台的环境代码还不一样。我们通过 EnvironmentProperties 集中管理了 5 套环境的别名映射：

```
troubleshooter:
  envs:
    -aliases: [xxprd, xxprd-proxy, prd, 生产, xx石, xx石生产]
      logEnv:XXPRD
      metricEnv:xxprd-proxy
    -aliases: [xxprd, xxprd-proxy, xx, xx生产]
      logEnv:XXPRD
      metricEnv:xxprd-proxy
    # ... 预发、测试等
```

支持精确匹配 + 包含匹配，且在 tool 方法中直接使用告警事件中的环境字段，不让 LLM 自行映射——避免 LLM "翻译"环境名时出错。

### LLM 调用 Round-Robin 多 Key

LLM 网关有频率限制，单个 API Key 在高频调用下会被限流。我们实现了 RoundRobinChatModel，支持配置多个 API Key，按事件 ID 哈希取模固定分配。同一个排查过程中的所有 LLM 调用使用同一个 Key，避免上下文切换。

## 九、效果与数据

系统上线后，我们统计了从 2026-04-21 至 2026-05-14 的运行数据。

### 运行概况：

![](https://picx.zhimg.com/v2-34bbda3288e11ecd1b5997f649860b7b_1440w.jpg)

### 排查性能：

![](https://pic4.zhimg.com/v2-e90aa4c3798c51006b15cd5041b17ac9_1440w.jpg)

### 效率对比：

![](https://pic1.zhimg.com/v2-87e036446b46be0a626a884f66392fe8_1440w.jpg)

**结论质量：** 验收首次通过率：约 60%；验收二次通过率：约 38%；验收耗尽兜底率：约 2%。

## 十、后续迭代方向

当前版本还有一些设计上的取舍，按优先级规划：

- **多 Agent 并行排查：** 日志查询和指标查询可以并行执行；
- **指纹知识库：** 实现秒级匹配已知问题；
- **跨服务关联分析：** 识别级联故障；
- **自动处置能力：** 从"排查+建议"升级到"排查+自动修复"；
- **向量语义检索：** 实现语义级相似问题检索；

Troubleshooter 不是要替代运维人员，而是把"登录多个平台、切换不同入口、凭经验猜方向"这种机械操作交给 AI，让运维人员专注于需要判断力和创造力的决策。如果你也在做类似的 AI Agent 落地项目，希望这篇文章能给你一些参考。

## 往期回顾

[1.得物技术在 AICon 关于大模型与 Agent 技术实践分享来袭！](https://link.zhihu.com/?target=https%3A//mp.weixin.qq.com/s%3F__biz%3DMzkxNTE3ODU0NA%3D%3D%26mid%3D2247544742%26idx%3D1%26sn%3D95a1c9a7738bbda6f89f887df7767534%26scene%3D21%26poc_token%3DHFHjIGqjHoRtA9R3jfCj2j5AMPg7RWkoYa6qiplw)

[2.HorizonVault 技术深潜：如何在 HDD 上做出 100GB/s+ 级大吞吐分布式存储｜得物技术](https://zhuanlan.zhihu.com/p/2043279092603164048)

[3.Claude Code Harness 工程：数仓侧落地方案｜得物技术](https://zhuanlan.zhihu.com/p/2040761367385401155)

[4.BP Claw 破解 AI 编码输入难题 ——FlinkSpec 需求智能化实践｜得物技术](https://zhuanlan.zhihu.com/p/2038207199843972299)

[5.基于 Harness + SDD + 多仓管理模式的 AI 全栈开发实践｜得物技术](https://zhuanlan.zhihu.com/p/2035665071955759902)

## 文 /溪石

关注得物技术，每周四更新技术干货

要是觉得文章对你有帮助的话，欢迎评论转发点赞～

未经得物技术许可严禁转载，否则依法追究法律责任。

编辑于 2026-06-04 10:31・上海