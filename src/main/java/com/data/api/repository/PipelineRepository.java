package com.data.api.repository;

import org.springframework.stereotype.Repository;

import com.data.api.model.PipelineConfig;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PipelineRepository extends JpaRepository<PipelineConfig, UUID> {
    
}
