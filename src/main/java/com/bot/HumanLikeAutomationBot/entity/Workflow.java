package com.bot.HumanLikeAutomationBot.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import java.sql.Types;
import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Table(name = "workflow")
@Data
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;


    @JdbcTypeCode(Types.LONGVARCHAR)
    @Column(name = "json_definition", columnDefinition = "TEXT")
    private String jsonDefinition;

    @Column(name = "version")
    private Integer version;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}