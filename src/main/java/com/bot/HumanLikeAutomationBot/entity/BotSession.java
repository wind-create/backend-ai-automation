package com.bot.HumanLikeAutomationBot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bot_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BotSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workflowId;
    private Long brainId;

    private String status; // RUNNING / STOPPED / ERROR

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime lastHeartbeat;

    private String machineName;
}