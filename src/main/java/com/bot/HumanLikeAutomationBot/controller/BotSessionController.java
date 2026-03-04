package com.bot.HumanLikeAutomationBot.controller;

import com.bot.HumanLikeAutomationBot.entity.BotSession;
import com.bot.HumanLikeAutomationBot.repository.BotSessionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@PreAuthorize("hasAnyRole('STAFF')")
@RequiredArgsConstructor
public class BotSessionController {

    private final BotSessionRepository botSessionRepository;

    @GetMapping
    public List<BotSession> all() {
        return botSessionRepository.findAll();
    }
}