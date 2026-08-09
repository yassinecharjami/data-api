package com.data.api.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.data.api.dto.request.PipelineConfigRequest;
import com.data.api.dto.response.RunStatusResponse;
import com.data.api.dto.response.UploadResponse;
import com.data.api.model.PipelineConfig;
import com.data.api.service.PipelineConfigService;
import com.data.api.service.PipelineRunService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pipeline")
public class PipelineController {
    
    private final PipelineConfigService pipelineConfigService;
    private final PipelineRunService pipelineRunService;

    public PipelineController(PipelineConfigService pipelineConfigService, PipelineRunService pipelineRunService) {
        this.pipelineConfigService = pipelineConfigService;
        this.pipelineRunService = pipelineRunService;
    }

    @PostMapping("/config")
    public ResponseEntity<PipelineConfig> createPipelineConfig(@Valid @RequestBody PipelineConfigRequest request) {
        PipelineConfig pipelineConfig = pipelineConfigService.createPipelineConfig(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pipelineConfig);
    }

    @GetMapping("/config/{pipelineId}")
    public ResponseEntity<PipelineConfig> getPipelineConfig(@PathVariable UUID pipelineId) {
        PipelineConfig pipelineConfig = pipelineConfigService.getPipelineConfig(pipelineId);
        return ResponseEntity.ok(pipelineConfig);
    }

    @PostMapping("/{pipelineId}/upload")
    public ResponseEntity<UploadResponse> uploadFile(@Valid @PathVariable UUID pipelineId) {
        // Use the MinioService to handle the file upload
        UploadResponse response = pipelineRunService.initUpload(pipelineId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{pipelineId}/run/{runId}/status")
    public ResponseEntity<RunStatusResponse> getRunStatus(@PathVariable UUID pipelineId,
                    @PathVariable UUID runId) {
        RunStatusResponse response = pipelineRunService.getRunStatus(pipelineId, runId);
        return ResponseEntity.ok(response);
    }
}
