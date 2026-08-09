package com.data.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.data.api.model.PipelineRun;

import jakarta.persistence.LockModeType;

@Repository
public interface PipelineRunRepository extends JpaRepository<PipelineRun, UUID> {
    
    Optional<PipelineRun> findByIdAndPipelineConfigId(UUID id, UUID pipelineConfigId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PipelineRun p WHERE p.id = :id")
    Optional<PipelineRun> findByIdForUpdate(UUID id);
}
