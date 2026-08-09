package com.data.api.repository;

import org.springframework.stereotype.Repository;

import com.data.api.model.PipelineConfig;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PipelineConfigRepository extends JpaRepository<PipelineConfig, Long> {
    
}
