package com.data.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.data.api.dto.response.RunStatusResponse;
import com.data.api.dto.response.UploadResponse;
import com.data.api.model.PipelineConfig;
import com.data.api.model.PipelineRun;
import com.data.api.model.PipelineStatus;
import com.data.api.repository.PipelineConfigRepository;
import com.data.api.repository.PipelineRunRepository;

@Service
public class PipelineRunService {

    private final PipelineRunRepository runRepository;
    private final PipelineConfigRepository configRepository;
    private final MinioService minioService;

    public PipelineRunService(PipelineRunRepository runRepository, PipelineConfigRepository configRepository, MinioService minioService) {
        this.runRepository = runRepository;
        this.configRepository = configRepository;
        this.minioService = minioService;
    }

    @Transactional
    public UploadResponse initUpload(Long pipelineId) {
        
        PipelineConfig config = configRepository.findById(pipelineId)
                .orElseThrow(() -> new RuntimeException("Pipeline config not found for the given ID"));

        PipelineRun run = new PipelineRun();
        run.setPipelineConfig(config);
        run.setStatus(PipelineStatus.PENDING);
        run = runRepository.save(run);
        
        String objectName = pipelineId + "/" + run.getId();
        String presignedUrl = minioService.generatePresignedUrl(objectName);
        
        return new UploadResponse(run.getId(), presignedUrl);
    }

    public RunStatusResponse getRunStatus(UUID pipelineId, UUID runId) {
        PipelineRun run = runRepository.findByIdAndPipelineConfigId(runId, pipelineId)
                .orElseThrow(() -> new RuntimeException("Run not found for the given pipeline ID and run ID"));
    
    return new RunStatusResponse(run.getId(), run.getStatus());
    }

    
}
