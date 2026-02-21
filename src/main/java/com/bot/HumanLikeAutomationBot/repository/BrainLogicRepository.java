package com.bot.HumanLikeAutomationBot.repository;

import com.bot.HumanLikeAutomationBot.entity.BrainLogic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrainLogicRepository extends JpaRepository<BrainLogic, Long> {
    Optional<BrainLogic> findByStatus(String status);
}