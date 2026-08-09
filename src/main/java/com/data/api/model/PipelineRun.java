package com.data.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "pipeline_run")
@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
public class PipelineRun {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_config_id", nullable = false)
    private PipelineConfig pipelineConfig;

    @Column(nullable = false)
    private PipelineStatus status; // ex: PENDING, RUNNING, SUCCESS, FAILED

    private String errorMessage;

    private String executedBy;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime startedAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
