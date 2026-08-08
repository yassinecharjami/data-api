package com.data.api.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.data.api.dto.request.PresignedUrlRequest;
import com.data.api.service.MinioService;

@RestController
@RequestMapping("/ingestion")
public class IngestionController {
    
    private final MinioService minioService;

    public IngestionController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestBody PresignedUrlRequest request) {
        // Use the MinioService to handle the file upload
        String presignedUrl = minioService.generatePresignedUrl(request.getBucketName(), request.getObjectName());
        return ResponseEntity.ok(Map.of("presignedUrl", presignedUrl));
    }
}
