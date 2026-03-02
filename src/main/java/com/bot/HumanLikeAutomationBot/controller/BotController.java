package com.bot.HumanLikeAutomationBot.controller;

import com.bot.HumanLikeAutomationBot.dto.StartBotRequest;
import com.bot.HumanLikeAutomationBot.entity.BotSession;
import com.bot.HumanLikeAutomationBot.service.BotService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bot")
@RequiredArgsConstructor
public class BotController {

    private final BotService botService;

    @PostMapping("/start")
    public ResponseEntity<BotSession> start(@RequestBody StartBotRequest request) {
        return ResponseEntity.ok(botService.startBot(request));
    }

    @PostMapping("/stop/{sessionId}")
    public ResponseEntity<String> stop(@PathVariable Long sessionId) {
        botService.stopBot(sessionId);
        return ResponseEntity.ok("Bot stopped successfully");
    }

    // =========================================
    // 🔥 GET ALL SESSIONS
    // =========================================
    @GetMapping("/sessions")
    public ResponseEntity<List<BotSession>> getAllSessions() {
        return ResponseEntity.ok(botService.getAllSessions());
    }

    // =========================================
    // 🔥 GET SESSION BY ID
    // =========================================
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<BotSession> getSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(botService.getSessionById(sessionId));
    }

    // =========================================
    // 🔥 GET ACTIVE SESSION
    // =========================================
    @GetMapping("/active")
    public ResponseEntity<BotSession> getActiveSession() {
        return ResponseEntity.ok(botService.getActiveSession());
    }
}