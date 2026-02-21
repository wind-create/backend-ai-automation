package com.bot.HumanLikeAutomationBot.repository;

import com.bot.HumanLikeAutomationBot.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkflowRepository extends JpaRepository<Workflow, Long> {
    Optional<Workflow> findByStatus(String status);
}