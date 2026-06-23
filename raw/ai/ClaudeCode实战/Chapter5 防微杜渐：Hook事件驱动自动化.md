    
    Claude.md 是建议，Skill是指导，但是从来没有人告诉Claude ‘这件事你绝对不能做’。而Hooks不同，它不是建议，而是强制执行。
    Hooks的核心逻辑：在操作执行的前后，插入额外的处理逻辑。

## 5.1  Hooks的定位
 回顾 `Claude.md`、`Skill`、`Agent`，这3种机制的对大模型的影响力是递增的。
 
   **Claude.md**：确定项目规范，有Claude在每次对话启动是读取；
   **Skill**：封装领域工作流，可在Claude需要自动或者手动触发；
   **Agent**：通过隔离上下文实现任务委派；

这3种机制的有一个共同特征：均作用于Claude的认知层面，`Claude.md`告诉Claude”应遵守何种规范“，`Skills`指导Claude"遇到此类问题时如何处理"，`子智能体`则指示Claude”应将子任务委派给谁“。当然，这些都仅仅只是建议性指导。但是作为语言模型，Claude在理论上可以忽略任何Prompt中的约束能力。

Hooks 的工作层面与前3着截然不同，作用与Claude的系统执行层，直接在系统执行层拦截Claude行为。例如，在Claude 尝试调用`rm -rf /`时，Hooks可以在系统执行层直接阻断该工具调用的执行，而不是像前3者”劝说“Claude不要执行该工具调用。

`Claude.md`与`Skills`定义的是策略，即应该怎么做，而`Hooks`定义的是机制，即”一旦违反策略，将被物理禁止“。`Hooks`对Claude的约束也不再依赖于Prompt的”引导“，而是通过系统底层的强制力来保障执行。

| 机制        | 工作层面  | 触发方式      | 约束性质 | 对Claude的控制 | 类比     |
| --------- | ----- | --------- | ---- | ---------- | ------ |
| Claude.md | 认知层   | 始终加载      | 建议   | 引导其行为      | 交通标志   |
| Skills    | 认知层   | 语义匹配/显示触发 | 指导   | 规范工作流      | 驾驶手册   |
| Hooks     | 系统执行层 | 事件自动触发    | 强制   | 拦截/阻止      | 路障/限速器 |
  
## 5.2 事件生命周期
  
  Claude Code的事件系统目前内置了多个事件，覆盖AI会话从启动到终止、从主对话到子智能体协作的完整生命周期。主要分为：会话级事件，工具调用事件、子智能体事件、完成事件与较新的事件类型。
### 5.2.1 会话级事件

会话级事件负责管理整个会话的生命周期，主要包含一下3个关键事件：

#### 5.2.1.1 SessionStart 事件

在会话启动或者恢复时触发。核心能力是通过`CLAUDE_ENV_FILE`注入环境变量。Hook脚本可向该文件写入 `export`语句，使变量在后续所有的Bash命令中生效。这就意味着可以在会话开始是自动配置开发环境。

   ```shell
   #!/bin/bash
   # session-init.sh  —— SessionStart Hook
   if [ -n "$CLAUDE_ENV_FILE" ]; then
       echo 'export NDOE_ENV=development' >> "$CLAUDE_ENV_FILE"
       echo 'export DEBUG_LOG=true' >> "$CLAUDE_ENV_FILE"
   fi
   exit 0
   ```

#### 5.2.1.2 SessionEnd 事件

在会话终止时触发。其匹配器（matcher）可以区分不同的终止原因：`clear(用户清除)`、`logout(登出)`、`prompt_input_exit(用户退出输入)`。该事件常用于清理临时资源或者记录会话统计信息。

#### 5.2.1.3 PreCompoct 事件

在上下文压缩前触发。其匹配器可以区分`manual(用户主动执行/compact指令)`和`auto(上下文窗口满后自动压缩)`。此事件适合在压缩前备份完整的对话记录。

### 5.2.2 工具调用事件

    最核心的事件类别，涵盖了Claude每次工具调用的完整生命周期。
#### 5.2.2.1 PreToolUse 事件
    
    整个Hooks系统中最强大的事件。

在Claude决定调用某个工具之后，工具实际执行之前触发。该事件支持3中操作：
* **允许（allow）**：绕过权限检查直接执行；
* **拒绝（deny）**：阻止执行并说明原因；
* **修改（updateInput）**：调整输入参数后执行；

“调整输入参数”这个操作的能力尤其强大：允许在不中断操作的前提下，静默地为命令添加安全参数。例如如下示例，将原本危险的`rm -rf`操作静默修改为`rm -rf --dry-run`，既让Claude继续完成任务，又避免了文件被真正删除。 
```json
{
  "hooksSpecificOutput": {
    "hooksEventName": "PreToolUse",
    "permissionDecision": "allow",
    "updateInput": {
	    "command": "rm -rf /tmp/test --dry-run"
    }
  }
}
```

#### 5.2.2.2 PostToolUse 事件

在工具执行成功后触发。它无法撤销已经发生的操作，但是具备两项核心功能：
* 通过 `additionalContext`向Claude反馈额外信息（如代码Lint检查结果）;
* 对输出进行后处理，如自动格式化刚写入的文件。

另外，在MCP场景中，该事件还拥有专属能力：可通过`updatedMCPToolOutput`字段直接替换MCO工具的原始输出内容。

#### 5.2.2.3 PostToolUseFailure 事件

在工具执行执行失败后触发，主要用于错误告警以及提供纠正性反馈。

#### 5.2.2.4 PermissionRequest 事件

在权限对话框即将弹出时触发。他与 `PreToolUse`的关键区别在于触发时机，`PreToolUse`会在每次工具调用前无条件触发，`PremissionRequest`仅在Claude需要用户手动确认确认权限时才被激活。通过该事件，可以通过编程方式自动批准或者拒绝权限申请。
```json
{
  "hooksSpecificOutput": {
    "hooksEventName": "PermissionRequest",
    "decision": {
	    "behavior": "allow",
	    "updatedPermissions": {}
    }
  }
}
```

#### 5.2.2.5 UserPromptSubmit 事件

在用户提交输入后，Claude 开始处理之前触发。该事件常用语输入预处理或者上下文注入场景。例如：在用户每次发送消息时，自动附加当前的Git分支信息。

### 5.2.3 子智能体事件
#### 5.2.3.1  SubagentStart 事件

在子智能体启动时触发，其匹配器可以根据子智能体的类型名称进行筛选，既支持内置类型（如：Bash、Explore、Plan），也支持 `.claude/agents`目录中定义的子智能体。记住，SubagentStart事件无法阻止子智能体的启动，但在子智能体运行时会通过 `additionalContext`注入关键上下文信息。例如，在子智能体自动时，自动加载团队的编码规范。

#### 5.2.3.2 SubagentStop 事件

在子智能体完成任务后触发。其行为与全局的`Stop事件`完全一致，也可以放行停止操作，也可以拦截该请求，强制子智能体继续工作，直到满足特定的指令标准。此外，`SubagentStop`的输入数据包含两个关键路径：
* transcript_path：主会话记录；
* agent_transcript_path：子智能体自身的对话记录；
借助这些信息，Hooks脚本能够复盘子智能体的完整工作流程，从而对其产出质量进行精准评估。

### 5.2.4 完成事件
#### 5.2.4.1 Stop 事件

在Claude完成整轮响应时触发。这是实现”质量门控“机制的核心：如果检测到输出内容未满足预设标准，可以通过设置`decision: "block"`阻止会话结束，强制 Claude继续修正或者完善工作。

#### 5.2.4.2 Notification 事件

在Claude发送系统通知时触发。其匹配器能够精准区分不同类型的通知，如：`permission_prompt(权限请求)`、`idle_prompt(空闲提示)`、`auth_success(认证成功)`等。该事件常用于自定义通知渠道的集成，例如：将关键告警在本地桌面触发弹窗提醒。

### 5.2.5 新的事件

#### 5.2.5.1 TeammateIdle 事件

专为多智能体团队协作设计。在队友智能体即将进入空闲状态时触发。

#### 5.2.5.2 TaskCompleted 事件

专为多智能体团队协作设计。在任务被标记完成时触发。

#### 5.2.5.3 ConfigChange 事件

在配置文件发生变更时触发。该事件主要用于审计与合规，帮助开发者追踪设置变化历史，防止未经授权的配置修改。

#### 5.2.5.4.  WorktreeCreate 事件

对应 GitWorktree 的创建，通过拦截该事件，用户可以自定义版本控制工作流的初始化操作，如：自动安装依赖。

#### 5.2.5.5 WorktreeRemove 事件

对应 GitWorktree 的删除，通过拦截该事件，用户可以自定义版本控制工作流的清理逻辑，如：删除临时构建产物。