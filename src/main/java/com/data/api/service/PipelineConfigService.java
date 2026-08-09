package com.data.api.service;

import org.springframework.stereotype.Service;

import com.data.api.dto.request.PipelineConfigRequest;
import com.data.api.model.PipelineConfig;
import com.data.api.repository.PipelineConfigRepository;

@Service
public class PipelineConfigService {

    private final PipelineConfigRepository pipelineRepository;

    public PipelineConfigService(PipelineConfigRepository pipelineRepository) {
        this.pipelineRepository = pipelineRepository;
    }

    public PipelineConfig createPipelineConfig(PipelineConfigRequest request) {
        PipelineConfig pipelineConfig = new PipelineConfig();
        pipelineConfig.setFormat(request.format());
        pipelineConfig.setSource(request.source());
        pipelineConfig.setDestination(request.destination());
        pipelineConfig.setTeam(request.team());
        pipelineConfig.setCreatedBy("developer"); // This should ideally come from the authenticated user context
        pipelineConfig.setPriority(1); // Default priority, can be modified later
        return pipelineRepository.save(pipelineConfig);
    }
    
}
