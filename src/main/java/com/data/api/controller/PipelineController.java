package com.data.api.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.data.api.dto.request.PipelineConfigRequest;
import com.data.api.dto.response.RunStatusResponse;
import com.data.api.dto.response.UploadResponse;
import com.data.api.model.PipelineConfig;
import com.data.api.service.MinioService;
import com.data.api.service.PipelineConfigService;
import com.data.api.service.PipelineRunService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pipeline")
public class PipelineController {
    
    private final PipelineConfigService pipelineService;
    private final PipelineRunService pipelineRunService;

    public PipelineController(PipelineConfigService pipelineService, PipelineRunService pipelineRunService) {
        this.pipelineService = pipelineService;
        this.pipelineRunService = pipelineRunService;
    }

    @PostMapping("/config")
    public ResponseEntity<PipelineConfig> createPipelineConfig(@Valid @RequestBody PipelineConfigRequest request) {
        PipelineConfig pipelineConfig = pipelineService.createPipelineConfig(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pipelineConfig);
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadFile(@PathVariable UUID pipelineId) {
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
