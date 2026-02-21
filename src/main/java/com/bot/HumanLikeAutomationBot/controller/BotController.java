package com.bot.HumanLikeAutomationBot.controller;

import com.bot.HumanLikeAutomationBot.dto.StartBotRequest;
import com.bot.HumanLikeAutomationBot.entity.BotSession;
import com.bot.HumanLikeAutomationBot.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bot")
@RequiredArgsConstructor
public class BotController {

    private final BotService botService;

    @PostMapping("/start")
    public BotSession start(@RequestBody StartBotRequest request) {
        return botService.startBot(request);
    }
}