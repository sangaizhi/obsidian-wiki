---
type: concept
tags:
  - ai
  - agent
  - training
  - chat-template
summary: "Agent 训练与 Chat Template 关注工具调用轨迹如何构造、校验并按模型固定对话格式进入训练和推理。"
sources:
  - "raw/知乎/2026-05-15/万字长文解读LLM Agent：总体框架、经典论文与实践.md"
created: "2026-05-17"
updated: "2026-05-17"
---

# 概念：Agent 训练与 Chat Template

## 定义

Agent 训练与 Chat Template 是让模型学会稳定调用工具的工程环节：前者构造可学习的工具调用轨迹，后者确保训练和推理时对话、工具定义、工具结果使用同一种序列化格式。

## 数据构建流程

- 工具构建：优先使用真实工具，只有不便接入真实 API 时才使用虚拟工具。
- 任务构建：通过工具列表、persona、场景和指令进化构造更接近真实场景的任务。
- 答案构建：在可执行或模拟环境中交互，记录完整 trajectory。
- 答案校验：可验证任务用 rule-based 校验，开放式任务用 LLM judge。

## 训练注意点

完整轨迹正确不代表每一步都值得学习。若某一步工具调用 JSON 无法解析、参数错误或中间代码执行报错，可以在 SFT 中 mask 掉对应 loss，避免模型学习错误格式。

## Chat Template 的作用

Chat Template 把 system、user、assistant、tool 和工具定义固定拼成模型训练时熟悉的字符串格式。模型一旦接受某种格式训练，推理时必须保持相同格式，否则容易发生分布漂移，影响工具调用稳定性。

## 关联页面

- [[concepts/概念_AI_Agent|AI Agent]]
- [[concepts/概念_工具调用|工具调用与执行]]
- [[concepts/概念_FunctionCalling|Function Calling]]
- [[sources/来源_LLM_Agent总体框架|来源：LLM Agent 总体框架]]

