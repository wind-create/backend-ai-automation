package com.bot.HumanLikeAutomationBot.repository;

import com.bot.HumanLikeAutomationBot.entity.BotSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BotSessionRepository extends JpaRepository<BotSession, Long> {

    Optional<BotSession> findByStatus(String status);
}