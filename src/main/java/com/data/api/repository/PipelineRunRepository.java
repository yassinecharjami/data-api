package com.data.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.data.api.model.PipelineRun;

@Repository
public interface PipelineRunRepository extends JpaRepository<PipelineRun, UUID> {
    
    Optional<PipelineRun> findByIdAndPipelineConfigId(UUID id, UUID pipelineConfigId);
}
