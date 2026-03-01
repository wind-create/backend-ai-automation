package com.bot.HumanLikeAutomationBot.repository;

import com.bot.HumanLikeAutomationBot.entity.ActionScript;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActionScriptRepository extends JpaRepository<ActionScript, Long> {

    Optional<ActionScript> findByStatus(String status);
}