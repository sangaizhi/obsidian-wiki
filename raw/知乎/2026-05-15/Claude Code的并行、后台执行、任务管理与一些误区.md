---
title: "Claude Code的并行、后台执行、任务管理与一些误区"
source: "https://zhuanlan.zhihu.com/p/2021181226682253905"
author:
  - "[[魔法学院的Chilia​哥伦比亚大学 理学硕士]]"
published:
created: 2026-05-15
description: "往期回顾： 万字长文解析Agent框架中的上下文管理策略从Claude Code入手看Agent框架设计思路（基础篇）从Claude Code入手看Agent框架设计思路：多智能体(Multi-Agent)在这篇文章中，我们继续从Claude Code来看Code…"
tags:
  - "clippings"
---
[收录于 · 大模型agent](https://www.zhihu.com/column/c_2013968425089770602)

42 人赞同了该文章

往期回顾：

- [万字长文解析Agent框架中的上下文管理策略](https://zhuanlan.zhihu.com/p/2012088406826562496)
- [从Claude Code入手看Agent框架设计思路（基础篇）](https://zhuanlan.zhihu.com/p/2014805541709447594)
- [从Claude Code入手看Agent框架设计思路：多智能体(Multi-Agent)](https://zhuanlan.zhihu.com/p/2018114851793315554)

在这篇文章中，我们继续从Claude Code来看Code Agent的设计模式。本篇会介绍Claude Code的并行、后台执行和任务管理，会涉及到很多易混的概念，这也是我在初学时总傻傻分不清楚的概念。这些误区在这篇文章中都会做澄清。

Anthropic不小心泄露了Claude Code的源码，这对我们理解Claude Code的工作原理有了很大的帮助。所以在写本篇的时候，我也会参照一些源码的内容，但是不会堆叠代码，而是只放必要的部分来帮助我们理解。

## 0x01. 并行执行

**（1）什么是多工具并行？**

**单消息多工具并行** 在Agent框架中是一个非常重要的性能优化。 它指的是：在一条message里，Agent可以同时发出多个tool call，它们会被并发地执行，而非串行执行。它们同时进行，不过最后一定会 **按原始的顺序** 返回。这个机制在很多Agent框架中都有，并不是Claude Code独有的。

下面是实际使用中并行调用的一个简单例子，就是assistant在调用tool的时候不再是只发出一个tool call，而是可以发出多个tool call；对应的工具调用返回结果（tool result）是与发出的tool call顺序对应的：

```json
[
  {
    "role": "assistant",
    "content": [
      {
        "name": "Bash",
        "type": "tool_use",
        "input": {
          "command": "bash command 1",
        }
      },
      {
        "name": "Bash",
        "type": "tool_use",
        "input": {
          "command": "bash command 2",
        }
      }
    ]
  },
  {
    "role": "user",
    "content": [
      {
        "type": "tool_result",
        "content": "result of bash command 1",
      },
      {
        "type": "tool_result",
        "content": "result of bash command 2",
      }
    ]
  }
]
```

特殊地， **子智能体** 作为一个特殊的工具调用（它是一个工具名称为"Agent"的一个工具调用），我们当然也可以并行地起很多子智能体，让不同的子agent们负责不同的、最好是互不依赖的方面。在上一篇文章 《 [从Claude Code入手看Agent框架设计思路：多智能体(Multi-Agent)](https://zhuanlan.zhihu.com/p/2018114851793315554) 》中，我们已经详细介绍了子智能体，并给出了一个并行调用多个Explore subagent的例子。

**（2）Claude Code中的 [流式工具并行](https://zhida.zhihu.com/search?content_id=272147687&content_type=Article&match_order=1&q=%E6%B5%81%E5%BC%8F%E5%B7%A5%E5%85%B7%E5%B9%B6%E8%A1%8C&zhida_source=entity) （StreamingToolExecutor）**

这个是翻看了Claude Code源码才发现的一个细节，也是Claude Code框架最精妙的设计之一。Claude Code用的是流式工具并行，意思就是 **模型还在输出token的时候，就有一些tool call开始执行了** 。

普通 Agent 的实现是：等模型说完 → 提取出工具调用 → 执行工具调用 → 返回结果。中间有一段多余的等待时间，哪怕第一个工具的所有参数在模型输出的第n秒就已完整了，也要等模型说完所有话才能开始执行。

而Claude Code则不同，它的一个优化就是模型还在说话呢，一些工具就已经开始执行了。Claude Code 的工具执行响应比较快，原因之一就是工具执行的延迟被隐藏在了模型推理的时间里。Claude Code 的做法是监听 Anthropic API 的 [Server-Sent Events](https://zhida.zhihu.com/search?content_id=272147687&content_type=Article&match_order=1&q=Server-Sent+Events&zhida_source=entity) （SSE）流，SSE流大概长成这样：

```
event: message_start
data: {"type":"message_start","message":{"id":"msg_01...",...}}

event: content_block_start
data: {"type":"content_block_start","index":0,"content_block":{"type":"tool_use","id":"toolu_
01A","name":"Read","input":{}}}

event: content_block_delta
data: {"type":"content_block_delta","index":0,"delta":{"type":"input_json_delta","partial_jso
n":"{\"file_"}}

event: content_block_delta
data: {"type":"content_block_delta","index":0,"delta":{"type":"input_json_delta","partial_jso
n":"path\":"}}

event: content_block_delta
data: {"type":"content_block_delta","index":0,"delta":{"type":"input_json_delta","partial_jso
n":"\"/src/auth.py\"}"}}

event: content_block_stop         ## 此时参数 JSON 完整了！
data: {"type":"content_block_stop","index":0}

event: content_block_start         ## 第二个工具开始，但此时第一个已经在跑了
data: {"type":"content_block_start","index":1,"content_block":{"type":"tool_use","id":"toolu_
01B","name":"Read",...}}
```

我们可以看到这里的event包括：

- content\_block\_start：新 block 开始（文本或 tool\_use）
- content\_block\_delta：内容碎片增量地到达
- content\_block\_stop：这个 block 结束，这是一个关键的边界
- message\_stop：整条消息结束

那么，每当一个 content\_block\_stop事件到达（Claude Code监听到这个边界时），就说明这个工具的输入 JSON 已经完整了。那么不管后面还有没有其他工具正在生成，就立刻把这个工具提交执行。

StreamingToolExecutor的核心代码如下：

```
// StreamingToolExecutor.ts
export class StreamingToolExecutor {
  // 模型流式吐出一个 tool_use block，立刻开始执行
  addTool(block: ToolUseBlock, message: AssistantMessage): void { ... }

  // 并发安全的工具可以同时跑，不安全的操作独占
  // 结果按接收顺序排队，保证输出确定性
  async *getRemainingResults(): AsyncGenerator<MessageUpdate> { ... }
}
```

addTool 在 content\_block\_stop时被调用，getRemainingResults 在流结束后负责把所有结果按顺序吐出来。

**（3） [并发安全调度](https://zhida.zhihu.com/search?content_id=272147687&content_type=Article&match_order=1&q=%E5%B9%B6%E5%8F%91%E5%AE%89%E5%85%A8%E8%B0%83%E5%BA%A6&zhida_source=entity) （ `isConcurrencySafe` ）**

并不是所有工具都够并发，需要用 `isConcurrencySafe` 标记做区分。代码中的接口是这样的：

```
isConcurrencySafe(input: z.infer<Input>): boolean
```

它接收的是 input 参数，也就是说每次调用时都会把具体的内容传进来进行判断，而不是在工具类上写死一个布尔值。比如，同样都是Bash这个Tool，但是执行不同命令，isConcurrencySafe的返回值可以不同，这个并不是写死在工具类型上的。Bash执行 `ls` 可以标记为 safe，但是执行文件修改操作的就会标记为 unsafe。

当然了，Read、Glob、Grep 这些工具因为是天然只读的，所以isConcurrencySafe 总是返回 true，所以就不需要检查 input了。

安全调度逻辑在 `canExecuteTool` 里：

```ts
// src/services/tools/StreamingToolExecutor.ts                                              
 private canExecuteTool(isConcurrencySafe: boolean): boolean {                                
   const executingTools = this.tools.filter(t => t.status === 'executing')                    
   return (                                                                                   
     executingTools.length === 0 ||                                                           
     (isConcurrencySafe && executingTools.every(t => t.isConcurrencySafe))                    
   )                                                                                         
 }
```

意思就是：一个工具能立刻跑，当且仅当"现在没有任何工具在执行" 或者 "这个工具是 safe 的，并且当前所有正在跑的工具也都是 safe 的"。也就是说，多个只读工具（safe）可以并行，但只要有一个写（unsafe）操作，就必须等前面所有工具跑完，再独占执行，期间不允许其他工具插队。这样，就保证了并发安全调度。

还有一个有意思的细节：如果 Bash 工具执行出错，会通过 siblingAbortController 取消所有正在并行的其他工具；但如果是 Read 或 WebFetch出错，其他工具可以继续跑。这是因为bash 命令通常有隐式依赖链，一个失败往往意味着后续没有意义，而读操作之间是独立的。

**（4）结果排序保证（按顺序缓冲）**

**Anthropic API 要求 `tool_result` 消息里的结果顺序必须和对应的 `tool_use` 顺序一致** ，否则 API 会报错或行为异常。所以，即便排在后面的任务执行得更快、先完成了，它的结果也不会排在先执行的任务前面。

翻看Claude Code的代码，我们可以发现： `completedResults` 是按 block index 排序的 Map，最后会按原始顺序组装 tool\_result列表发回 API。

## 0x02. 后台执行

> “......博尔赫斯后来为它写了首诗，诗中说，他丢硬币这一举动，在这星球的历史中添加了两条平行的、连续的系列：他的命运及硬币的命运。此后他在陆地上每一瞬间的喜怒哀惧，都将对应着硬币在海底每一瞬间的无知无觉。”——《夜晚的潜水艇》，陈春成

**（1）区分：后台执行和并发执行**

虽然后台执行和并发执行经常在一起使用（即后台并发执行），但“后台”和“并发”是完全独立的两个概念。

- **并发** 决定的是 **数量** 。如果并发执行，同一轮对话中会一次性发出多个工具调用/subagent，它们会同时启动。
- **后台** 决定的是 **阻塞行为** 。通过设置 `run_in_background: true` ，工具/subagent可以在后台运行，主对话不会等待其完成，而是立即继续执行其他任务或者响应用户。

"并发"和"后台"可以随意组合，比如并发但不后台运行、后台运行但不并发。

**（2）任务进入后台的时机**

Bash 命令在遇到下面几种情况的时候，会被放入后台运行：

- Bash这个Tool在被调用时，模型的输出会直接声明 `run_in_background: true` ，就像下面这个例子一样：
```
{
    "role": "assistant",
    "content": [
      {
        "name": "Bash",
        "type": "tool_use",
        "input": {
          "command": "/root/.cargo/bin/cargo build -p deno 2>&1 | tail -30",
          "timeout": 600000,   ####设置超时时间
          "description": "Build deno in the background",
          "run_in_background": true
        }
      }
    ]
},
```

此时工具立即返回，不等命令结束：

```json
{
  "role": "user",
  "content": [
    {
      "type": "tool_result",
      "content": "Command running in background with ID: {task_id}. Output is being written to: /tmp/claude-0/{path}/tasks/{task_id}.output",
      "is_error": false,
    }
  ]
}
```
- 前台运行超时时，会触发 `startBackgrounding()` ，内部调用 `backgroundExistingForegroundTask()` ，将这个超时的工具调用打入后台。从上面这个例子中我们可以看到，在Bash命令调用的时候，会有一个timeout参数的设置，这应该是为了避免有些工具前台运行的时间太长，阻塞整个流程的进展。在实际使用中我发现，Claude Code将这些超时的工具调用打入后台之后，就会时不时地用TaskOutput这个工具（本文之后会讲）去看这个后台运行的工具现在做到什么程度了。如果确实发现运行时间太长，它会反思自己是不是效率太低了，就有可能把这个工具调用终止掉，然后换一个效率更高的方式重新运行。
- 用户按 Ctrl+B 会触发 `backgroundAll()` ，把所有前台的任务一次性打入后台运行。

**（3）后台任务日志的存储**

任务进入后台后，会立即调用 `registerTask(taskState, setAppState)` ，把任务状态写入 `AppState.tasks` （ `React` 状态树）。同时开始把进程的 stdout/stderr 实时追加写入磁盘的这个位置： `~/.claude/tmp/<项目哈希>/<sessionId>/tasks/<taskId>.output` 。Bash后台任务和Agent 后台任务的 output 文件形态是不同的：

- Bash 后台任务存储的位置就是普通文件，存储的内容是Bash命令执行的stdout/stderr，实时追加。命令跑到中间的时候读这个文件，就能看到当前运行的log。所以后面我们也会看到，TaskOutput这个工具其实就是在Read这个.output文件，来看每个Task执行的进展。
- Subagent 后台任务存储的.output文件是在建立 **符号链接** ，指向 Subagent 的 JSONL 会话记录：
```
/tmp/.../tasks/{task_id}.output                                           
    → /root/.claude/projects/.../subagents/agent-{task_id}.jsonl
```

> 背景知识：【符号链接（symlink）】  
> 符号链接就是一个文件，它里面的内容只有一个字符串，即目标路径。当你用任何程序打开它时，操作系统内核会自动重定向到目标路径，用户完全感知不到中间发生了什么，但看到的内容就变成目标路径的内容了。

这个指向的 JSONL 每行是一条 message，包含subagent完整的对话历史、工具调用与observation。用 Read工具读它就能看到 subagent 的完整执行轨迹了。

至于前台命令（ `run_in_background: false` ），则不会写额外的文件，它的输出会直接作为 `tool_result` 返回。

**（4）任务完成通知**

后台任务完成时，框架会把 `<task-notification>` XML写入通知队列 `commandQueue[]` ，形如下面所示：

```
<task-notification>
  <task-id>{task_id}</task-id>
  <tool-use-id>{tool_use_id}</tool-use-id>                  
  <output-file>/tmp/claude-0/.../tasks/{task_id}.output</output-file>             
  <status>completed</status>                                                      
  <summary>Background command "xxxx" completed (exit code 0)</summary>                                                                      
</task-notification>
```
- task-id：后台任务的唯一 ID，和.output 文件名对应
- tool-use-id：最初那次 run\_in\_background 工具调用的 ID，用于关联上下文
- output-file：日志文件路径，模型可以用 TaskOutput/Read 工具去读
- status：completed / failed / killed
- summary：可读的一句话描述

这个task-notification通知送达模型的方式取决于当时模型的状态。

**情况一：模型正在运行工具**

通知被 **捎带** 进下一个工具返回的 tool\_result content 里，包在 `<system-reminder>` 标签内一起送达：

```json
{                                                                                 
  "role": "user",
  "content": [{                                                                   
    "type": "tool_result",                                                        
    "content": "{tool response of this turn}\n\n<system-reminder>\n<task-notification>\n    
<task-id>{task_id}</task-id>\n  <status>completed</status>\n  <summary>Background 
command \"...\" completed (exit code 0)</summary>\n</task-notification>\n</system-reminder>",                          
    "tool_use_id": "xxxx"
  }]                                                                              
}
```

**情况二：模型空闲（没有工具在运行，等待用户输入）**

task notification会作为一条新的 user消息注入，主动触发新一轮模型调用，不需要用户发任何消息。

## 0x03. 任务管理

**两类任务，与一些误区**

刚才我们在讲解的时候，经常用到"Task"这个词，其实有一些滥用之嫌。实际上，Claude Code 中有两套"任务（Task）"概念，这个非常容易混淆，所以在这里做一下区分。

**（a）第一类是TODO 管理工具** （TaskCreate/TaskUpdate/TaskGet/TaskList）。这些是 **UI 层** 的进度追踪，不是真正的执行。它们在用户界面上显示一个可视化的todo list（如下面的图所示），帮助用户了解当前任务的进展，只是纯粹的状态管理。源码里这套工具被一个叫 `isTodoV2Enabled()` 的开关控制，在交互式会话中默认开启。

- `TaskCreate` ：创建任务
```
{
  "subject": "Fix authentication bug",       // 标题
  "description": "What needs to be done",    // 详情
  "activeForm": "Fixing authentication bug"  // in_progress 时显示的文字,用于 UI spinner 动画
}
```

创建后 status 默认是 pending。

- `TaskUpdate` ：更新任务状态、设置依赖关系：
```json
{
   "taskId": "1",
   "status": "in_progress",
   "owner": "my-agent-name",
   "addBlocks": ["2", "3"],
   "addBlockedBy": ["0"],
 }
```

addBlocks 的意思是"在我完成之前，这些任务不能开始"；addBlockedBy的意思是"这些任务完成之前，我不能开始"。status的变化是： pending → in\_progress → completed。

下面这个例子是在实际使用中的UI：

![](https://pic2.zhimg.com/v2-5a28d1e0a4d447c8e2298a4fc3a5e6f3_1440w.jpg)

task2、3依赖于task1；task4依赖于task2、3。现在正在做task1

![](https://pic2.zhimg.com/v2-0ef291004428353d59b458e834dfac9f_1440w.jpg)

task1、2、3完成，正在做task4

- `TaskList` ：查看所有任务，返回所有任务的摘要（id、subject、status、owner、blockedBy）。
- `TaskGet` 返回单个任务的完整详情，比 TaskList多返回 description 和 blocks 字段（本任务阻塞哪些任务）。

这里还有一个误区，就是认为这种TODO管理和 [Plan mode](https://zhida.zhihu.com/search?content_id=272147687&content_type=Article&match_order=1&q=Plan+mode&zhida_source=entity) 是一回事。实际上，Plan mode 的意思是让Code Agent在动手之前先设计，然后给用户审批。进入Plan Mode之后只能探索代码，不能写代码，最后用 `ExitPlanMode` 把方案呈给用户批准，用户批准了才开始执行。 `TaskCreate/TaskUpdate` 这些TODO管理工具是执行过程中的进度追踪，用来在 UI上显示现在做到哪一步了。实际上它们两个经常一起用：plan mode里设计好方案，批准后开始执行，执行时用 task list 追踪进度。

**（b）第二类是后台任务管理工具** （TaskOutput/TaskStop），它们管理的是真正在后台运行的 shell 命令或 subagent。

- `TaskStop` 可以强制终止一个后台任务。
- `TaskOutput` 目前是已经废弃了，它的工具描述里面写着

> \[Deprecated\] — prefer Read on the task output file path

prompt 里也说：

> DEPRECATED: Prefer using the Read tool on the task's output file path instead. Background tasks return their output file path in the tool result, and you receive a <task-notification> with the same path when the task completes — Read that file directly.

意思就是说，在后台任务启动时，tool\_result 里会直接给出output 文件路径，也就是我们在2-3中看到的 `~/.claude/tmp/<项目哈希>/<sessionId>/tasks/<taskId>.output` ，任务完成时收到的 <task-notification> 里也有同一个路径。那么我们其实直接 Read 这个文件就行。

那么为什么TaskOutput会被废弃呢？

是因为它依赖内存中的任务注册表，任务完成后会从 AppState.tasks 里清除，一旦错过时间窗口，TaskOutput 就会报 "No task found"。

```
const task = appState.tasks?.[task_id] as TaskState | undefined;
if (!task) {
  return { result: false, message: \`No task found with ID: ${task_id}\` };
}
```

但是其实任务的log文件一直在磁盘上，任务完成后仍然可读，任务还在跑时也能读到已经输出的内容（相当于实时 log），通过Read就可以实时访问。

在Prompt里面，Claude Code也强调了“反复轮询(polling)”，因为每次轮询都要消耗一次 API call。所以Claude Code建议不要轮询，而是等 `<task-notification>` ，它会在任务完成时自动 push 过来。

编辑于 2026-04-12 12:01・北京