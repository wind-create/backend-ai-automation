package com.bot.HumanLikeAutomationBot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import java.sql.Types;

@Entity
@Table(name = "action_script")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionScript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JdbcTypeCode(Types.LONGVARCHAR)
    @Column(name = "script_content", columnDefinition = "TEXT")
    private String scriptContent;

    private Integer version;

    private String status; // ACTIVE / DISABLED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}