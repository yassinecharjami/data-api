package com.data.api.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.data.api.dto.request.PipelineConfigRequest;
import com.data.api.dto.response.RunStatusResponse;
import com.data.api.dto.response.UploadResponse;
import com.data.api.model.PipelineConfig;
import com.data.api.service.PipelineConfigService;
import com.data.api.service.PipelineRunService;

import jakarta.validation.Valid;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


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

    @GetMapping("/{pipelineId}/config")
    public ResponseEntity<PipelineConfig> getPipelineConfig(@PathVariable Long pipelineId) {
        PipelineConfig pipelineConfig = pipelineConfigService.getPipelineConfig(pipelineId);
        return ResponseEntity.ok(pipelineConfig);
    }

    @PostMapping("/{pipelineId}/upload")
    public ResponseEntity<UploadResponse> uploadFile(@Valid @PathVariable Long pipelineId) {
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

    /**
     * Endpoint temporarily added for testing purposes. It will be removed in the future.
     * Send data using presignedUrl
     */

    @PostMapping(value = "/test-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)    
    public ResponseEntity<String> uploadUsingPresignedUrl(@RequestParam("file") MultipartFile file,
        @RequestParam("presignedUrl") String presignedUrl) {
        
    RestTemplate restTemplate = new RestTemplate();

    try {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentLength(file.getSize());

    HttpEntity<byte[]> requestEntity;
    
        requestEntity = new HttpEntity<>(file.getBytes(), headers);

    ResponseEntity<String> response = restTemplate.exchange(presignedUrl, HttpMethod.PUT, requestEntity, String.class);

    if (response.getStatusCode().is2xxSuccessful()) {
        return ResponseEntity.ok("File uploaded successfully using presigned URL.");
    } else {
        return ResponseEntity.status(response.getStatusCode()).body("Failed to upload file using presigned URL.");

    }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while uploading file: " + e.getMessage());
    }
}
}
