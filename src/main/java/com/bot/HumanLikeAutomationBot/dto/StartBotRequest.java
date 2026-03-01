package com.bot.HumanLikeAutomationBot.dto;

import lombok.Data;

@Data
public class StartBotRequest {
    private Long workflowId;
    private Long brainId;
    private Long actionScriptId;
    private String machineName;
}