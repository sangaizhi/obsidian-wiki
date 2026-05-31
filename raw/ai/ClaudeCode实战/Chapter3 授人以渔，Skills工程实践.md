## 3.1 从Claude.md 到Skills，知识的两个维度

### 3.1.1 Claude.md
* 加载策略：每天都用、所有任务、通用规则
* 知识类型：常驻知识

Claude.md 像是企业的规章制度，定义了项目的基本规则，eg：项目的编码语言，缩进风格，命名风格；这些规则在每一次对话的每一个瞬间都必须生效，因此必须常驻于上下文中，每次全量加载。代价是无论是否在处理相关任务，这些Token都会被消耗。
### 3.1.2 Skills
* 加载策略：有时采用、特定任务、专业知识
* 知识类型：按需知识
Skills 集中于特定领域的知识，解决的是**知识的按需投放问题**。

| 系统文件      | 承载的内容  | 生效范围     | 触发方式 | 加载策略    | 典型用途     | 与Agent的关系  | Token消耗 | 企业本体论   |
| --------- | ------ | -------- | ---- | ------- | -------- | ---------- | ------- | ------- |
| Claude.md | 项目通用规则 | 当前项目     | 任何对话 | 每次全量加载  | 使用Java语言 | 所有Agent共享  | 固定开销    | 企业规章制度  |
| Skills    | 专业领域知识 | 可跨项目、跨会话 | 按需激活 | 渐进式按需加载 | 进行代码审查   | 可绑定特定Agent | 按需支付    | SOP操作手册 |
**企业本体论**
一个企业其实拥有两类截然不同的知识资产：
* 通用规则：所有都必须遵守，如考勤、安全红线，对应Claude.md；
* 特定岗位的标准操作程序（Standard Operating Procedure）：只有特定岗位的人执行特定的任务时才需要用到的知识，对应Skill；

**定义**
一个Skill是一个包含指令的<font color=red>文件夹</font>，被打包成一个简单的目录结构，用来<font color=red>教</font>大模型如何处理特定任务或者工作流。
* 文件夹：从”字符串“到”工程化“，Skill绝不仅仅是一段Prompt或者一个简单的配置项，而是一个完整的工程化目录，可以容纳复杂的代码库、详尽的领域文档、多样的模板、可执行的脚本
* 教：从”约束“到”内化“，”教“微妙的揭示了其运作机制的本质，”教“是一种能力赋予和内化。通过Skill，大模型真正理解了一个领域的运作逻辑，不再是被动执行指令的工具，而是变成了该领域的熟手。

## 3.2 SKILL的结构
### 3.2.1 目录结构

```text
alter-log-analysis
├── SKILL.md     包含skill的名称(烤肉串命名法，短横线分隔，像文件名一样清晰)
                  【核心骨架】：必要的主技能文件，大小写必须精确匹配              
├── scripts       【手脚】：可选的可执行脚本，让skill能主动“做”事
   └── analyze_alert.py 如告警分析主入口
   
├── reference     【记忆库】：可选的按需加载的参考文档，不占常驻内存
   └── record_storm_pattern.md 只有分析“记录风暴”时才读取的规范
   
├── templates     【模具】：可选的输出模板，确保产生格式统一
   └── record_storm_report.md 分析“记录风暴”的结果报告模板
```

**文件名**: SKILL.md  唯一的触发器。文件名必须采用全大写的形式，skill.md、Skill.md、SKILL.MD 通通无效。常见Agent的加载器在扫描文件时，执行的时精确字符串匹配，不是模糊搜索。
**目录名**：kebab-base(烤肉串命名法)，目录名需要跨平台使用，采用全小写+短横线+数字的形式，最多64个字符，不能存在空格、下划线、大写字母、首尾横线和连续横线。主要为了抹平操作系统的差异，保证你的Skills能在任何环境、任何工具链中都能被无损识别。
<font color=red >特别注意</font>：
* Skills的目录在禁止存放README.md。README.md应放在父目录下，因为当一个Skill被激活后，部分Agent会读取目录下的所有Markdown 文档作为上下文，引入不必要的噪声。
### 3.2.2 前置元数据

SKILL.md 顶部的YAML Frontmatter 定义了Skill的身份标识与行为边界。
例如：
```yaml
---
name: alter-log-analysis
description: Use when receiving alters via WeworkChat that require atuomaterd root-cause analysis
argument-hint: "[name space] [output format]"
disable-model-invocation: true
user-invocable: false
allowed-tools:
  - Read
  - Grep
  - Glob
  - Write
  - Base(python:*)
model: deepseek-v4-flash
context: fork
agent: Explore
hooks:
  PreToolUser:
    - matcher: Base
      hooks:
        - commands: echo "$TOOL_INPUT" >> audit.log
---
```

以上元数据可以分为三大块，分别对应SKill 的3个核心维度：触发机制、权限控制、运行时环境。
**触发机制**：包含name、description、argument-hint，它们定义了 Skill 是什么，负责向Agent和用户传达Skill的功能定位及调用方式，是触发逻辑的基础。
* name：唯一标识符，最多64个字符，若省略，默认使用目录名，建议与目录名保持一致；
* description：触发描述，最多1024个字符，若省略，则使用SKILL.md 正文的第一段
* argument-hint：参数提示，在 ”/“ 菜单中显示，帮助用户了解该Skill接受的输入格式

**权限控制**：包含disable-model-invocation、user-invocable、allowed-tools、model，规定了”谁能调用“以及”能做什么“，通过限制调用来源、可用工具及指定模型，构建起严格的安全与资源边界。
* disable-model-invocation：禁止模型自动调用，设置为true时，禁止agent自动调用，用户必须通过 ”/skill-name“手动触发；默认值：false
* user-invocable：用户可调用行，设置为false时，从 ”/“ 菜单中隐藏，但是agent仍可自动调用，默认：true
* allowed-tools：工具白名单，精确控制Skill执行时可调用的工具及权限范围
* model：指定模型，设定该Skill使用的模型，建议简单任务使用推理速度快、低成本模型。

**运行时环境**：包括 context、agent、hooks。它们决定了”在哪里执行“以及”执行过程中发生什么“，用于配置隔离环境、子智能体类型及生命周期时间钩子，确保任务在预期的上下文中运行。
* context：执行上下文，设置为”fork“时，将在隔离的子智能体中执行，确保不污染对话上下文
* agent：子智能体类型，当context=fork时生效，可选值为当前agent支持的子智能体类型或者自定义的Agent。
* hooks：生命周期事件钩子，定义Skill激活期间内的事件处理逻辑，仅在Skill激活状态下生效。


## 3.3 渐进式披露
 当一个Agent有非常多的SKILL时，每个SKILL都包含数千Token的内容，大模型在每次对话中如何精确匹配到可以激活的SKILL？
### 3.3.1 图书馆模型

浏览图书分类编目定位分类  -->  提取目标书籍查阅图书目录  -->  仅精读所需章节。

Skills 系统也是采用了类似的三层渐进式披露模型，以极低的Token成本实现海量知识的按需调用。
![图书馆模型](./assets/chapter3_3.3.1_图书馆模型.png)



### 3.3.2 description预算机制

渐进式披露的第一层级，即所有Skill的`description`是以常驻的方式注入的大模型的上下文中。这意味着他们必须共同瓜分的一个有限的Token预算，是Agent工作的一个瓶颈。    

 `Claude Code`官方的规则是：description 总预算的上限为上下文窗口总量的2%，如果未指定或者计算出现异常，则默认固定16000个字符。
  这部分有限Token预算由所有安装的Skill平分，而不是按需分配，计算公式：
	         `单个Skill可用字符数 = 总预算 / Skill的总数`
  如果某个Skill的description超过了可用字符数，它将被静默排除。Claude Code完全不知道该Skill的存在，在扫描节点会看不到，在后续的步骤中也无法加载。
  对于 description 预算限制的情况，有三个技巧可以避开：
  * 在Skill中配置 `disable-model-invocation: true`，这样该Skill的description 就不会注入到上下文中。
  * 运行诊断命令 `/context` 查看是否有超出预算的Skill被静默移除。
  * 调整环境变量`SLASH_COMMAND_TOOL_CHAR_BUDGET`，手动扩大预算池。
  
因此：不要盲目创建Skill，而是追求`少而精`的架构设计：
* 合并同类Skill：将多个零碎的Skill合并为一个综合性的Skill
* 隐藏内部工具：将纯内部调用的子任务标记为  `disable-model-invocation: true`，减少对预算的占用。
* 关注 Token 投资回报率：将 description 中的每一个字符都视为一笔昂贵的投资，投入精准换来高效的检索和准确的执行。

## 3.4 触发机制：如何抉择Skill的调用

Skill的触发机制是整个Agent系统的关键核心环节，直接决定了Skill能否在适当时机被激活，甚至比Skill的内容本身的优劣更为重要。毕竟再好的内容如果无法成功触发，也是无效的Skill。

### 3.4.1 双通道激活机制
#### 3.4.1.1 显示调用
   用户直接通过`/skill-name`命令调用Skill，Agent会立即加载并行对应的Skill。此方式具有明确的，直接且无歧义的特点。若Skill定义了`argument-hint`,用户还可以携带参数直接调用。
![显示调用](./assets/chapter3_3.4.1.1_显示调用.png)
   
#### 3.4.1.2 词义匹配
   大模型在深入理解用户的意图后，自主判断哪个Skill于当前任务最契合，从而自动加载。这是Skill的核心价值所在，用户仅需描述需求 ，Agent在幕后智能决策是否调用以及如何调用最合适的Skill，对用户完全透明。
   
![词义匹配](./assets/chapter3_3.4.1.2_词义匹配.png)


**特别说明**：如果Skill实在比较多，可以在Agent将Skills信息拼接到提示词之前，使用小模型过滤一遍Skills，然后在将过滤出来的Skill打包拼接到发给大模型的提示词中。

一般的Agent设计都是将Skill信息拼接到系统提示词之后或者用户消息之前。
### 3.4.2 Skill的灵魂 - description

词义匹配机制完全依赖于description字段，该字段并非人类阅读的说明性文章，而是Agent决策“是否调用该Skill”的唯一依据。

Claude Code 推荐的 description 结构公式：
`[功能定义](做什么) + [触发场景](何时用) + [核心能力](能做什么)`

以下是<font color=red>不好</font>的写法：
```
# 过于模糊，导致Agent无法判断使用时机
description: Helps with projects

# 过于技术化，缺失用户视角的触发关键词
description: 使用层次关系实现项目实体模型

# 仅描述功能，未界定触发场景
description: 生成API文档
```

好的写法：
```
# 正确示范：特点是表述清晰、覆盖具体触发场景，详述核心能力
description: Generate API documentation from Express,FastAPI, or Srping Boot source code. Use when user asks to "Write API docs","document endpoints","create OpenAPI specs", or mentions "Swagger". Supports route detection, request/response schema extraction, and authentication requirement marking.
```

`description`字段的长度是1024个字符，因此需要字斟句酌、精心选词。
1. 第一步：定义核心能力(What)，用一句话精准概括该Skill “能做什么”，确定基本功能定位；
2. 第二步：明确触发场景(When)，使用 Use when user... 句式，详细列举各种可能触发该Skill的用户指令、短语或者关键词，提高语义匹配的命中率。
3. 第三步：划定排除范围(Not for)，此步骤可选但是推荐，如果该Skill容易被触发，务必加上Not for...，明确指出其不适用的场景。

记住一点：
description 是给 大模型看的，而非人类读者。大模型阅读`description` 是在进行深度的词义匹配，大模型会根据捕捉的用户意图匹配最合适的Skill。因此，我们必须在`description`中穷尽用户可能使用的表达方式。

### 3.4.3 防止过触发与欠触发

Skill的触发面临两种典型的失效模式。
#### 3.4.3.1 欠触发
 * 表现：Skill本应该被调用，但实际没有被调用；
 * 分析：有评测数据显示，若缺乏明确指引，Agent有56%的概率不会去查看可用得到skills；
 * 原因：`description`写的过于技术化或者学术化，与用户自然的口语化表述存在较大的语义差距；
 * 修复：在`description`中加入用户常用的表达词汇，涵盖领域数据的同义词，口语化说法，甚至是一些常见的错误表述、易混淆表述。
#### 3.4.3.2 过触发
* 表现：Skill在不该调用的场景下被错误激活；
* 原因：`description`定义的过于宽泛，包含了太多高频通用词汇，导致词义匹配命中率太高；
* 修复：引入负向约束，明确划定边界，使用 `Not for... ` 排除干扰项。

**评估方式**： 构建一个包含10~20个测试用户的验证集，同时覆盖“应触发”和“不应触发”两类场景。相关任务触发率应该达到90%以上，无关任务误触发率应控制在5%以下。
### 3.4.4 两种Skill

Skill中有一个字段`disable-model-invocation`，通过该字段可以发现两种不同Skill的交互方式，**参考型**和**任务型**。
#### 3.4.4.1 参考型Skill
   配置：默认行为
   核心逻辑：“按需加载的知识库”，大模型会根据上下文通过`description`自动判断是否需要该Skill。
   使用场景：提供知识、规范、框架或者标准，用户不需要感知Skill的存在，需要大模型在合适时机自动装载。
#### 3.4.4.2 任务型Skill
   配置：显示设置`disable-model-invocation: true`；
   核心逻辑：“受控的执行工具”，大模型无法自动触发，必须通过用户使用 `/skill-name`手动调用。`description`不注入大模型上下文，仅作为用户在选择Skill是的识别说明。
   使用场景：具有副作用的操作，需要用户明确授权才能执行。

**副作用**：一个操作不仅返回结果，还改变了系统外部状态，比如部署上线会改变线上服务状态。

#### 3.4.4.3 如何设计
判断标准：如果大模型自动执行这个Skill，最坏的情况是什么？
* 感到紧张：例如，自动提交了为测试的代码。这种必须设置为任务型Skill，`disable-model-invocation: true`。
* 无关紧要：例如：多展示了一段参考文档，稍微增加了一点Token消耗，可以选择参考型Skill。让大模型根据上下文自动加载。
**核心原则**：”“副作用”越大，控制权越要收紧，对于任何可能改变系统状态，造成不可逆后果的操作，永远不要信任Claude的自动判断。

## 3.5 SKILL.md 正文
 
 SKILL.md是路由器，不是正文，是Skill真正发挥作用的地方。
### 3.5.1 路由器思维
  一个常见的误区是将所有信息都写到SKILL.md的正文中，把正文当做“知识仓”。但是，正确的设计是将其定位为路由器，文件自身仅包含核心流程与路由表，而相近的知识内容则分散存储于被引用的文件中。
![路由器思维](./assets/chapter3_3.5.1_路由器思维|1000)


核心技巧：构建“快速参考”，该表格能以极低的Token，清晰的向大模型指引关键维度的路由条件。
```
## 快速参考

|分析类型                  |触发关键词          |参考资源                      |
| ----------------------- | ------------------ | --------------------------- |
|收入分析（Revenue）       |收入、营收、销售额   |reference/revenue.md         |
|成本分析（Cost）          |成本、费用、支出     |reference/rcosts.md          |
|盈利分析（Profitablility）|利润、毛利率、净利率 |reference/profitablility.md  |

```

相比大模型逐行扫描整个SKILL.md，这种结构化表格的效率会更高。

### 3.5.2 契约式引用

在 `SKILL.md`中引用辅助文件时，不能只罗列路径。应当建立一份明确的“契约”，确保大模型清晰知晓3个核心要素：触发时机（合适加载）、资源位置（去哪儿找）以及预期产出（获取何物）。

* 弱引用（反面示例）
	```
	# 弱引用：缺乏上下文（大模型无法判断合适该加载此文件，缺乏行动指令）
	参考 `reference/revenue.md` 获取详细内容
	```
	
* 契约式引用：明确条件+路径+内容预期
	```
	## 收入分析
	当用户询问关于收入增长，平均收入或者收入组成时：
	参考 `reference/revenue.md` 获取计算工时或者行业标准。 
	```
	
这一设计里面与子智能体流水线中的”交接契约“一脉相承：下游消费者不仅需要知道上游的位置，更必须明确上游能提供什么。

### 3.5.3 500行法则
为什么是500行？
因为500行代码约为2000~3000 Tokens，是单个Skill激活后合理的上下文开销。将其与`System Prompt`及当前会话历史累加，能确保总Token数维持在可控范围内。若超过500行，就意味着我们将“参考资料”和“路由指令”混淆了，需要立即重构。

重构对策：

| 重构信号            | 对策               |
| --------------- | ---------------- |
| 大段公式或规范说明       | 移至 reference/ 目录 |
| 多个完整示例（单个超过30行） | 移至 examples/ 目录  |
| 多个输出模板          | 移至 templates/ 目录 |
| 可独立执行的逻辑        | 封装为 scripts/ 脚本  |
| 多个平行的功能模块       | 考虑拆分为多个独立的Skill  |

## 3.6 allowed-tools：知识约束行动

`allowed-tools` 是Skills 安全架构中的核心字段，体现了“知识应当约束行动得” 的设计原则。
具体的权限配置应该基于Skill对业务逻辑的“认知“；
例如：
代码审查Skill：审查过程仅读取代码，禁止修改，因此仅授予只读工具；
文档生成Skill：需要创建新文件，不能修改既有文件，仅此仅授予写入(Write)权限，禁止编辑(Edit)权限;

### 3.6.1 权限设计模板

最小权限原则，确保每个Skill仅拥有完成其特定任务所需的最低限度工具权限。
```
# 审计类Skill: 严格只读
allowed-tools:
  - Read
  - Grep
  - Glob

# 生成类Skill：科可写不可改
allowed-tools:
  - Read
  - Grep
  - Glob
  - Write

# 分析类Skill：只读+特定脚本
allowed-tools:
  - Read
  - Grep
  - Glob
  - Base(python:*)

# 执行类Skill：受控命令白名单
allowed-tools:
  - Read
  - Bash(git status:*)
  - Bash(git add:*)
  - Bash(git commit:*)
  - Bash(npm text:*)
```

### 3.6.2 Bash的精细控制语法
Bash 工具支持通过前缀匹配机制，实现对可执行命令的细粒度管控。其核心语法为`Bash(prefix:*)`，其中 prefix 指定允许的命令前缀，* 作为通配符代表后续参数。
示例：
```
# 运行所有以 git 开头的子命令
Bash(git:*) 

# 仅允许 git log 及其参数，禁止 git push 等危险操作
Bash(git log:*)

# 仅允许运行测试命令，防止误执行 npm install
Bash(npm test:*)

# 允许所有Python脚本执行
Bash(python:*)

# 允许执行 scripts/ 目录下的特定脚本
Bash(./scripts/*:*)
```

当大模型尝试执行Bash命令时，Agent会进行前置校验：
* 提取前缀：获取用户请求执行的完整命令字符串；
* 匹配规则：检查该命令是否以配置的prefix开头；

执行决策：若匹配成功，则放行；若失败，则直接拒绝，返回权限错误。

切记，遵循权限最小化原则时构建Skill安全的即使。
```
# 精确授权：只明确列出任务所需的具体命令子集
# 场景：代码提交 Skill
# 策略：仅允许 status、add、commit 3个特定子命令
allowed-tools:
  - Bash(git status:*)
  - Bash(git add:*)
  - Bash(git commit:*)
    

# 过度授权：使用全局通配符等同放弃安全
# 场景：错误的通用配置
# 风险：允许执行任意的 Shell 命令
allowed-tools:
  - Bash(*)
```

## 3.7 参数传递与动态注入
 Skill不仅仅时静态指令，更支持运行时参数传递和上下文预注入，使其行为能偶根据场景进行动态调整。
### 3.7.1 $ARGUMENTS 和位置参数
```
---
name: migrate-component
description: Migration a component between frameworks
argument-hint: "[component] [from] [to]"
disable-model-invocation: true
---
Migration the $0 component from $1 to $2
Preserve all existing behavior and tests
```

   当用户使用`/migrate-component SearchBar React vue`命令调用上述Skill时，大模型实际接收到的指令为`Migrate the SearchBar component from React to Vue.Preserve all existing behavior and tests`。
SKILL.md 可以使用以下变量：


| 变量            | 说明            |
| ------------- | ------------- |
| $ARGUMENTS    | 所有参数的完整字符串    |
| $ARGUMENTS[0] | 第一个参数(索引从0开始) |
| $ARGUMENTS[2] | 第二个参数         |
| $0、$1、$2      | 位置参数的简写形式     |

### 3.7.2 动态上下文注入

这是Skills 系统中最具威力且独一无二的特性。"!`command`"语法允许将该SKILL.md发送给大模型之前，现在Shell环境中执行指定命令，并将命令的输出结果直接内联替换到Prompt中。
例如：
有已有一个Skill
```
---
name: pr-create
---
## Current content
Current branch:
!`git branch --show-current`

Recent commits:
!`git lo origin/main..HEAD  --oneline 2>/dev/null || echo "No commits"`

Files changed:
!`git diff --stat origin/main 2>/dev/null || git diff --stat HEAD~3`
```
  当用户执行`/pr-create "Add auth"`命令时，大模型收到的是已经填充了动态数据的Prompt：
  ```
  ## Current content
Current branch:
feature/auth

Recent commits:
a1b2c3d Add JWT middleware
d4e5f6g Add login endpoints

Files changed:
src/auth/middleware.ts | 45 +++
src/auth/login.ts      | 82 +++
2 files changed, 127 insertions(+)
  ```
"!`command`"  特性使用前后对比

| 维度          | 未启用          | 使用           |
| ----------- | ------------ | ------------ |
| 启动是的上下文     | 空白，需要对轮对话探索  | 已注入关键信息      |
| 首次响应的工具调用次数 | 3~5词（用户手机信息） | 1~2次（直接执行行动） |
| Token消耗     | 高            | 低            |
| 响应速度        | 慢            | 快            |
| 结果一致性       | 低（存在信息遗漏风险）  | 高（固定注入相同信息）  |
大模型在执行 “!`command`” 时遵循严格的顺序，先替换 $ARGUMENTS 变量，再执行Shell命令。这样就存在一个问题，用户输入的内容将直接拼接到Shell命令中，如果不加以管控，极易收到Shell注入攻击。因此，任何使用 “!`command`” 语法的Skill，必须通过`allowed-tools`配置严格限制其可执行的命令范围，以构建必要的安全围栏。

## 3.8 作用域与优先级
 Skill文件可不属于不同的层架，每个层级对应特定的生效范围与适用场景。

| 位置                                 | 生效范围      | 用途                     |
| ---------------------------------- | --------- | ---------------------- |
| 企业配置中心                             | 全员生效      | 强制执行的企业级开发规范与安全策略      |
| `~/.claude/skills/<name>/`         | 个人所有项目    | 个人编码习惯，通用工具集以及跨项目辅助脚本  |
| `<project>/.claude/skills/<name>/` | 仅限当前项目    | 项目特有的工作流、业务逻辑定制及团队协作规范 |
| Plugin内置资源                         | Plugin启用时 | 社区共享的能力包、特定框架的专用指令集    |

Skill 位置的优先级
企业策略 >  个人配置(./claude/) > 项目配置(.claude/) > Plugin内置。

**企业策略**：企业级配置拥有最高权限，用于强制执行全局安全与合规策略。
**个人配置**：位于个人目录的配置，用于满足开发者的个人习惯。
**项目配置**：位于项目根目录的配置，专为特定项目服务。

将 Skill 目录 纳入项目的版本控制，是实现团队知识零成本共享的最优解。
* 即插即用：团队成员拿到项目代码后，相关Skill自动生效，无需手动操作；
* 同步演进：Skill跟随代码一同更新；

## 3.9 Skill的4种设计模式

![Skill设计模式组合决策树](./assets/chapter3_3.9_Skill设计模式组合决策树|1000)

 * **模板驱动模式**：利用预定义模板严格约束输出格式。该模式适用于需要标准化输出的场景。例如：周报、审查报告
 * **脚本增强模式**：将确定性计算逻辑封装为脚本，由大模型调用执行而非自行推导。适用于财务计算、正则匹配、数据转换等场景。相比于大模型推理，脚本执行更精准、更节省Token，更具复现性。经验：如果发现自己的SKILL.md 中编写公式使大模型运行计算，请立即停止，该逻辑应当被移至脚本中。
 * **知识分层模式**：依据使用频率对知识进行分层组织，遵循”8/2法则“（即80%的请求仅需20%的核心内容），将高频知识内联制SKILL.md，低频知识则置于引用文件中按需加载。
 * **工具隔离模式**：通过 `allowed-tool` 机制严格界定Skill的能力边界。这属于安全设计范畴，核心价值在于明确"禁止做什么"，这比”能做什么“更为关键。

## 3.10 实战
### 3.10.1 代码审查Skill

代码审查约定：
* 优先级原则：优先关注安全问题，其次是性能问题，最后才是代码问题；
* 反馈要求：必须提供具体的修改建议，严禁仅指出问题而不给方案；
* 分级标注：每个问题均需标注严重等级；

#### 3.10.1.1 目录结构
```
code-reviewing
├── SKILL.md                    # 核心审查流程与标准
├── reference
   └── security-level-guide.md  # 详细等级判定标准
```

#### 3.10.1.2 SKILL.md 正文
```markdown
---
name: code-reviewing
## 按照团队标准执行结构化代码审查。按优先级顺序检查安全漏洞、性能问题和代码质量。当用户要求“审查代码”、“进行代码审查”、“检查此PR”、“审核此功能”或提供代码并要求反馈时使用。
description: Performs structured code reviews following team standards. Checks security vulnerabilities, performance issues, and code quality in priority order. Use when users asks to "review code", "do a code review", "check this PR","audit this function", or provides code and asks for feedback.
allowed-tools:
  - Read
  - Grep
  - Glob
---
# 代码审查流程
你是一名资深代码审查员。执行代码审查时，请严格遵循以下优先级顺序。

## 第一优先级：安全审查
发现以下安全问题应立即报告：
- SQL注入风险：如直接拼接SQL字符串、未使用参数化查询
- XSS 漏洞：未转义的用户输入直接输出只HTML
- 敏感信息硬编码：包括密码、密钥、Token、数据库连接字符串等
- 权限验证缺陷：如缺失认证中间件、存在越权访问逻辑
  
## 第二优先级：性能问题
- N+1查询：循环内频繁查询数据库
- 索引缺失：高频查询字段未建立索引
- 重复计算：循环内存在可提升至循环外的不变量计算
- 内存泄露风险：如未关闭的连接、持续增长的缓存等

## 第三优先级：代码质量
- 函数过长：超过50行且无合理理由
- 命名不规范：变量或者函数命名含义不清
- 错误处理缺失：如空的catch块，异常被静默吞掉
- 代码重复：违反 DRY 原则
  
## 输出格式规范
每个发现的问题必须包含以下4个要素：
- 严重等级：Critical / Major / Minor
- 问题描述：具体阐述问题所在
- 文件位置：file_path:line_number
- 修改建议：提供具体的代码修正方案或者解决策略
  
若未发现任何问题，请明确回复"通过审查"，并简述已检查的主要方面。

注：详细的等级判断标准请参见`reference/security-level-guide.md`。
```

### 3.10.2 任务型Skill：智能提交

在实际编程中，每日需要频繁提交代码，手动撰写 commit message， 既耗时又容易不规范。为此，我们需要设计一个任务型Skill。由于该Skill的操作具有副作用 (直接修改代码仓库历史记录)，因此该Skill必须由用户手动触发，严禁自动执行。

```markdown
---
name: version-committing
description: Quick git commit with auto-generated or specified message
argument-hint: "[optional: commit message]"
disable-model-invocation: true
allowed-tools:
  - Bash(git status:*) 
  - Bash(git add:*)
  - Bash(git commit:*)
  - Bash(git diff:*)
model: deepseek-v4-flash
---

# Task: create a git commit

## Input Handling
If a message is provided: $ARGUMENTS
- Use that as the commit message
If no message is provided:
- Analyze the changes with `git diff --staged` (or `git diff` if nothing staged)
- Generate a concise, meaningful commit message
  
## Current State (Auto-detected)
Git status:
!`git status --short 2>/dev/null || echo "Not a git repository"`

Stage changes:
!`git diff --stageed --stat 2>/dev/null || echo ""Nothing staged`

## Steps
1. Check 'git status' to see current state
2. If nothing staged, run `git add .` to stage all changes
3. Review what will be committed with `git diff --staged` 
4. Create commit with appropriate message
5. Show brief comfirmation
   
## Commit Message Format
- Start with type: `feat:`,`fix:`,`docs:`,`refactor:`,`test:`,`chore:`
- Be consice but descriptive (max 72 chars for first line)
- Example: `feat: add user authentication with JWT`
  
## Output
Show a brief comfirmation:
√ Committed: [commit message]
  [number] files changed
```
说明：
* **安全控制(`disable-model-invocation: true`)**：强制禁用模型调用，确保执行过程依赖预设脚本，防止意外的AI推理介入。
* **动态参数($ARGUMENTS)**：支持灵活的参数传递机制，允许用户直接指定提交信息或者留空以触发自动生成。
* **上下文注入(!`command`)**：利用Shell命令在执行期间即时补货并注入当前的Git状态。这使得大模型在启动时就拥有完整的上下文信息；
* **成本与性能优化**：指定使用轻量级模型，提交操作主要依赖规则而非负载推理，该配置在保证操作的准确性，有效降低了延迟和资源消耗。

## 3.11 测试与迭代
3类核心测试方法，以确保Skill的健壮性。
* **触发测试**：准备10个应触发和10个不应触发Skill的问题，用来验证大模型判断的准确率。目标：相关任务触发率要高于90%，无关任务误触发率应低于5%；
* **功能测试**：验证Skill加载后的执行质量，检查要点需包含：输出格式是否服务预期、检查项是否完整覆盖、边界情况是否得到妥善处理；
* **性能对比**：针对同一任务，分别在“有Skill“和”无Skill“的状态下各执行5次，对比Token消耗量、用户修正次数以及最终输出质量。

如果需要反复手动修正大模型的输出，这就表明SKILL.md正文需要更新，将修正逻辑直接写入SKILL.md，下次就不会发生同类错误。

## 3.12 从软件工程看Skills
### 3.12.1 关注点分离（Separation of Concerns）
核心理念：“授人以鱼，不如授人以渔”。
Skills是将解决问题得到方法、步骤与经验沉淀为可复用的结构化资产，而非提供一次性答案。使得我们的Agent从以来“临时对话灵感”转变为能稳定复现高质量工作流。
Agent的三层架构职责：
* CLAUDE.md：全局规则（项目背景、通用规范）
* Skills：专用工作流（特定领域的复杂逻辑封装）
* 子智能体：任务执行（动态规划与实时操作）
工程师警示：严禁将所有逻辑写入CLAUDE.md，就像不要把所有代码写在main函数一样，这样会导致上下文同于、维护困难、难以扩展。

### 3.12.2 依赖倒置（Dependency Inversion）
核心机制：面向接口编程，而非面向实现编程。
 大模型不直接依赖Skill的具体内部实现，而是依赖其`description`和输出契约。只要保持契约不变，开发者可随时替换、重构或升级Skill的内部逻辑。

### 3.12.3 缓存优化与惰性加载
核心策略：渐进式披露
“渐进式披露”是一种典型的**惰性加载**策略，系统不是在启动时预加载所有知识库，而是在首次需要特定技能时才加载相关资源。

### 3.12.4 最小权限原则
安全基石：allowed-tools 是安全经典在AI领域的直接映射。
明确“不能做什么”比定义“能做什么”更能保障系统安全，防止恶意代码或幻觉导致的越权操作。

### 3.12.5 开放标准
生态愿景：声明式、自包含、知识本位
Anthropic 将Skills作为Agent Skills 的开放标准规范推广，自2025-12 分布以来，主流Agent平台均已提供远程支持。

Skills 成功的三大本质属性：
* **声明式**：纯Markdown格式，任何大模型均可读取和理解，无黑盒二进制；
* **自包含**：一个文件夹即包含全部所需，复制即安装，无需复杂的依赖管理；
* **知识本位**：核心价值在于内容本身而非特定格式，不绑定单一平台；

