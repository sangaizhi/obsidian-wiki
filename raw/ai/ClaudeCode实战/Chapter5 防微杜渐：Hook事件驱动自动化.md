    
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

### 5.2.6 "能否阻止"：最关键的维度

在所有的Hooks 时间中，“能否阻止”是最核心的分类维度，它决定了事件是用于“控制流程”还是仅用于“观察记录”。

具备阻止能力的事件包括：
* PreToolUse
* PermissionRequest
* UserPrompt
* Stop
* SubagentStop
* TeammateIdle
* TaskCompleted
* ConfigChange
* WorktreeCreate

只读事件：
* PostToolUse
* Notification
* SubagentStart
只读事件主要用于读取上下文。注入而外的信息或者触发侧边效应（如发送通知），但无法阻止或者修改Claude的核心执行逻辑。

最常用的3个事件：PreToolUse(工具执行前的守门员)、PostToolUse(工具执行后的质量守卫)、Stop(任务完成时的质量门控)，通过这3个事件，即可构建出健壮的自动化闭环。

## 5.3 配置体系：6个位置，6种用途

Claude Code 的配置系统采用分层叠加机制，优先级从上至下依次降低。Hooks 的配置采用标准的JSON格式，并严格遵循六层优先级架构。这种设计允许开发者根据作用域灵活部署自动化逻辑。
<div align=center><img src="./assets/chapter5_3_1_Hooks配置的6个层级与作用域.png" alt="Hooks配置的6个层级与作用域" title="Hooks配置的6个层级与作用域"/></div>


`.claude/setting.json`是团队协作的核心载体。所有成员在拿到项目时即可自动同步团队的的配置。
`./claude/setting.local.json`适用于需要覆盖团队默认配置的个人场景。
`~/.claude/setting.json`则用于管理跨项目的个人偏好。

`setting.json`采用3层嵌套设计：事件类型 -> match组 -> Hooks处理器列表，例如：
```json
{
    "hooks": 
    {
        "PreToolUse": [
	        {
                "match": "Bash",
                "hooks": [
                    {
                        "type": "command",
                        "command": "./.claude/hooks/block-dangerous.sh",
                        "timeout": 30
                    }
                ]
            }
        ],
        "PostToolUse": [
	        {
                "matcher": "Write|Edit",
                "hooks": [
	                {
                        "type": "command",
                        "command": "prettier --write  \"$CLAUDE_FILE_PATH\""
                    }
                ]
            }
        ]
    }
}
```

matcher字段用于指定该组Hook适用的工具范围。
* Bash：匹配所有的 Bash 调用
* Write|Edit：匹配 Write或者Edit工具调用（管道符 | 表示逻辑 “或”）
* * ： 匹配所有工具
对于Stop、Notification、UserPromptSubmit 等生命周期，matcher字段将被忽略，因为这些事件不针对特定工具。而在 SubagentStart 或者 SubagentStop事件中，matcher 匹配的是子智能体类型名称，而非工具名称。

## 5.4 三种处理器类型：确定性的阶梯

Hook 处理器包含3种类型，构成了一个”确定性递减、灵活性递增“的阶梯。具体的选择取决于验证逻辑所需的判断力度。

### 5.4.1 command类型：确定性规则

该类型用于执行Shell命令或者脚本。作为最常用且最可靠的的类型，确定性规则永远比大模型的判断更为可信。例如：
```json
{
	"type": "command",
	"command": "./.claude/hooks/check-security.sh",
	"timeout": 30
}
```

`command`类型的Hook通过标准输入（stdin）接收JSON格式的上下文数据（包含session_id、tool_name、tool_input等），通过标准输出(stdout)输出JSON格式的决策，并依据退出码表达最终意图。
* 退出码 0：表示成功。系统将 stdout 种的JSON解析结果作为决策依据；
* 退出码 2：表示有意阻止。系统将 stderr 的内容作为错误原因反馈给 claude；
* 其他退出码：表示脚本异常。stderr 内容仅在调试模式下可见，但不会阻断主流程。

退出码 2 的设计至关重要，它严格区分了“有意阻止操作”与“脚本异常”。脚本异常不应阻碍正常工作流。

### 5.4.2 prompt类型：单词大模型评估

当验证逻辑需要一定的判断力，但不需要执行多步操作时，建议使用 `prompt`类型。该类型会调用小型的模型（通常为Haiku）对当前情况进行评估。例如：
```json
{
	"type": "prompt",
	"prompt": "评估这段代码修改是否引入了安全漏洞。$ARGUMENTS",
	"model": "claude-haiku-4-5",
	"timeout": 30
}
```

其中 $ARGUMENTS为占位符，运行时将被替换为Hook接收到完整输入JSON。大模型的响应需要遵循以下JSON格式。
* 允许通过的JSON格式：
```json
{"ok": true, "reason":"代码修改安全，未引入已知漏洞模式"}
```
* 拒绝操作的JSON格式：
```json
{"ok": false, "reason":"检测到潜在的SQL注入风险：用户输入未经转义直接拼接到查询字符串"}
```

### 5.4.3 agent 类型：多轮子智能体验证
 
当验证逻辑需要实际查看代码文件、执行搜索或者多步操作才能得出结论时，应使用`agent`类型。该类型会启动一个子智能体，以便能够利用 Read、Grep、Glob 等工具进行多轮深度验证， 例如：
```json
{
	"type": "agent",
	"prompt": "检查所有修改的文件是否通过了单元测试。运行测试套件并验证结果。$ARGUMENTS",
	"timeout": 30
}
```

`agent` 类型的子智能体最多运行50轮对话/操作后必须返回决策，其响应格式与 `prompt`类型完全一致。

**总结**:
选择Hook处理器类型时，应遵循”能用`command`类型的不用`prompt`类型，能用 `prompt`类型的不用`agent`类型“的降级原则。

  ![Hook处理器类型的确定性与灵活性对别](./assets/chapter5_4_Hook处理器类型的确定性与灵活性对比.png)

确定性规则（如模式匹配、文件名检查、正则表达式）在速度和可靠性上永远大于大模型判断。只有当验证逻辑确实需要”理解力（语义分析）“或者”检查代码能力（多文件上下文检索）“时，才考虑升级到`prompt`或者`agent`类型。

## 5.5 hookSpecificOupt：与Claude交流的协议

Hook的输出格式使用嵌套在 `hookSepcificOutput`对象中的`permissionDecision`格式。例如：
```json
{
  "hooksSpecificOutput": {
    "hooksEventName": "PreToolUse",
    "permissionDecision": "deny",
    "permissionDecisionReason": "此命令试图删除受保护的系统目录",
    "additionalContext": "受保护的路径模式: /etc, /usr, /var"
  }
}

```

`permissionDecision`支持以下3种值：
* allow：绕过权限系统直接执行
* deny：阻止执行
* ask：交由用户确认（该选项并非自动拒绝，而是表达“我不确定，请有人来决定”的态度）
*
`additionContext`字段适用于所有事件类型，其内容将被注入 Claude 的上下文中。这一机制构建了一个高效的反馈闭环。例如`PostToolUse`Hook 可以通过`additionContext`将代码静态分析结果反馈给Claude，Claude 再接收到这些信息后会自动修复问题，整个过程不需要人工干预。

另外，所有事件均支持一下几个通用的顶层字段：
```json
{
	"continue": false,
	"stopReason": "检测到安全违规，会话已中止",
	"suppressOutput": false,
	"systemMessage": "警告，此操作已被安全策略拦截"
```

* `continue=false`：相当于“紧急制动 ”。无论当前处于何种事件节点，该设置都会立即终止 Claude 的处理；
* `systemMessage`：该字段的内容将直接显示给用户，而不会传递给 Claude。


## 5.6 工程实战：安全方式体系

使用Hooks构建一套完整的安全防范体系，通过以下3道防线进行安全防护。
* 危险命令拦截：block-dangerous
* 敏感文件保护：
* 全量操作审计：

### 5.6.1 PreToolUse：危险命令拦截

该Hook 的目的是拦截可能引发灾难的 Bash 命令。设计细节：
* 调试信息输出值标准错误（如：stderr，即 >&2）,而非标准输出（stdout）。这是因为 stdout必须严格保留用于输出JSON格式的决策结果。
* 使用 jq 工具解析输入的JSON数据，避免了脆弱的手动字符串匹配。
* 每一次拦截操作都附带清晰、具体的原因说明。
```shell
#!/bin/bash

# ./claude/hooks/block-dangegous.sh

set -e

INPUT=${cat}

# 提取命令（调试信息输出值 stderr）

COMMAND=$(echo "$INPUT" | jq -r '.tool_input.command' // ""')

echo "DEBUG: Checking command: $COMMAND" >&2

# 危险命令模式列表

DANGEROUS_PATTERNS=(

  "rm -rf /"

  "rm -rf ~"

  "rm -rf \$HOME"

  "> dev/sd"

  "dd if="

  "mkfs."

  ":(){:|:&};:"  # fork bomb（Fork 炸弹）

  "chmod -R 777 /"

  "git push --force origin main"

  "git push --force origin master"

  "git reset --hard origin"

  "DROP DATABASE"

  "DROP TABLE"

  "TRUNCATE"

  "curl.* | bash"

  "curl.* | sh"  # 危险的管道执行

)


for pattern in "${DANGEROUS_PATTERNS[@]}"; do

  if [[ "$COMMAND" == *"$pattern"* ]]; then

    echo "BLOCKED: Dangerous command detected: '$COMMAND'" >&2

    cat <<EOF

{

    "hookSpecificOutput":{

        "hookEventName": "PreToolUse",

        "permissionDecision": "deny",

        "permissionDecisionReason": "Dangerous command detected: '$COMMAND'"

        }

}

EOF

    exit 2

  fi

done

echo '{"hookSpecificOutput":{"hookEventName": "PreToolUse","permissionDecision": "allow"}}'

exit 0
```

### 5.6.2 PreToolUse：敏感文件保护

该Hook的目的是保护敏感文件免受意外修改。该Hook的匹配器配置为`Write|Edit`。确保仅在发生文件写入或者编辑操作时才触发。
```shell
#!/bin/bash

# ./claude/hooks/protect-files.sh

set -e
INPUT=${cat}

FILE_PATH=$(echo "$INPUT" | jq -r '.tool_input.file_path' // ""')
if [ -z "$FILE_PATH" ]; then
  echo '{"hookSpecificOutput":{"hookEventName":"PreToolUse","permissionDecision":"allow"}}'
  exit 0
fi

FILENAME=$(basename "$FILE_PATH")

# 受保护的文件名模式

PROTECTED_FILES=(
  ".env"
  ".env.local"
  ".env.production"
  "credentials.json"
  "secrets.yaml"
  "secrets.yml"
  "secrets.json"
  "id_rsa"
  "id_ed25519"
)

# 受保护的扩展名
PROTECTED_EXTENSIONS=(
  "pem"
  "key"
  "crt"
  "p12"
  "pfx"
)      

# 受保护的目录
PROTECTED_DIRS=(
  ".git"
  ".ssh"
  ".node_modules"
)

# 检查目录
for dir in "${PROTECTED_DIRS[@]}"; do
  if [[ "$FILE_PATH" == *"$dir"* ]]; then
    echo "BLOCKED: Attempt to access protected directory: '$FILE_PATH'" >&2
    cat <<EOF
{
    "hookSpecificOutput":{
        "hookEventName": "PreToolUse",
        "permissionDecision": "deny",
        "permissionDecisionReason": "不允许修改受保护目录中的文件: $dir"
        }
}  
EOF
    exit 2
  fi
done


# 检查文件名
for name in "${PROTECTED_FILES[@]}"; do
  if [[ "$FILENAME" == "$name" ]]; then
    echo "BLOCKED: Attempt to access protected file: '$name'" >&2
    cat <<EOF
{
    "hookSpecificOutput":{
        "hookEventName": "PreToolUse",
        "permissionDecision": "deny",
        "permissionDecisionReason": "不允许修改受保护文件: $name"
        }
}
EOF
    exit 2
  fi
done

# 检查扩展名
EXT = "${FILENAME##*.}"
for ext in "${PROTECTED_EXTENSIONS[@]}"; do
  if [[ "$EXT" == "$ext" ]]; then
    echo "BLOCKED: Attempt to access protected file with extension: '$EXT'" >&2
    cat <<EOF
{
    "hookSpecificOutput":{
        "hookEventName": "PreToolUse",
        "permissionDecision": "deny",
        "permissionDecisionReason": "不允许修改受保护扩展名的文件: .$ext"
    }
}
EOF
    exit 2
  fi
done

echo '{"hookSpecificOutput":{"hookEventName":"PreToolUse","permissionDecision":"allow"}}'
exit 0
```


### 5.6.3 PostToolUse：全量操作审计

该hook通过配置`matcher:"*"`,使得其能够捕获并记录所有的工具调用。在合规性要求严格的企业环境中，这是不可获取的机制。
```shell
#!/bin/bash
# ./claude/hooks/audot-log.sh

INPUT=${cat}
LOG_DIR="${CLAUDE_PROJECT_DIR:-.}/.claude/logs"
mkdir -p "$LOG_DIR"
LOG_FILE="$LOG_DIR/audit-$(date +%Y-%m-%d).log"
TIMESTMAP=$(date -Iseconds)
TOOL_NAME=$(echo "$INPUT" | jq -r '.tool_name // "unknown"')
TOOL_INPUT=$(echo "$INPUT" | jq -c '.tool_input // {}')

echo "[$TIMESTMAP] $TOOL_NAME: $TOOL_INPUT" >> "$LOG_FILE"
echo "{}"
exit 0
```

### 5.6.4 完整配置

将`5.6.1~5.6.3`对应的3个hook脚本整合进`.claude/settings/json`，就形成了一套严密的纵深防御体系.
```json
{
  "hooks": {
    "PreToolUse": [
      {
        "matcher": "Bash",
        "hooks": [
          {
            "type": "command",
            "command": "./.claude/hooks/block-dangerous.sh"
          }
        ]
      },
      {
        "matcher": "Write|Edit",
        "hooks": [
          {
            "type": "command",
            "command": "./.claude/hooks/protect-files.sh"
          }
        ]
      }
    ],
    "PostToolUse": [
      {
        "matcher": "*",
        "hooks": [
          {
            "type": "command",
            "command": "./.claude/hooks/audit-log.sh"
          }
        ]
      }
    ]
  }
}
```

这套配置构建了一个涵盖“事前拦截、事中防护、事后审计”的完整安全闭环。


































































