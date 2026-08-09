package com.data.api.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.data.api.dto.request.PipelineConfigRequest;
import com.data.api.dto.request.PresignedUrlRequest;
import com.data.api.model.PipelineConfig;
import com.data.api.service.MinioService;
import com.data.api.service.PipelineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pipeline")
public class PipelineController {
    
    private final MinioService minioService;
    private final PipelineService pipelineService;

    public PipelineController(MinioService minioService, PipelineService pipelineService) {
        this.minioService = minioService;
        this.pipelineService = pipelineService;
    }

    @PostMapping("/config")
    public ResponseEntity<PipelineConfig> createPipelineConfig(@Valid @RequestBody PipelineConfigRequest request) {
        PipelineConfig pipelineConfig = pipelineService.createPipelineConfig(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pipelineConfig);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestBody PresignedUrlRequest request) {
        // Use the MinioService to handle the file upload
        String presignedUrl = minioService.generatePresignedUrl(request.getBucketName(), request.getObjectName());
        return ResponseEntity.ok(Map.of("presignedUrl", presignedUrl));
    }
}
