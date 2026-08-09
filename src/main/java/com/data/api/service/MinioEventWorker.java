package com.data.api.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.data.api.model.PipelineRun;
import com.data.api.model.PipelineStatus;
import com.data.api.repository.PipelineRunRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MinioEventWorker {

    private static final Logger log = LoggerFactory.getLogger(MinioEventWorker.class);

    private final PipelineRunRepository runRepository;
    private final ObjectMapper objectMapper;
    
    public MinioEventWorker(PipelineRunRepository runRepository, ObjectMapper objectMapper) {
        this.runRepository = runRepository;
        this.objectMapper = objectMapper;
    }
    
    @KafkaListener(topics = "minio-events", groupId = "data-core-group-v2")
    @Transactional
    public void consumeMinioEvent(String message) {

        log.info("Received MinIO event message: {}", message);
        System.out.println("Received MinIO event message: " + message);
        try {
            JsonNode root = objectMapper.readTree(message);
            JsonNode records = root.path("Records");

            if (records.isMissingNode() || !records.isArray() || records.isEmpty()) {
                log.warn("No Records found in the MinIO event message: {}", message);
                return;
            }

            String objectKey = records.get(0).path("s3").path("object").path("key").asText();
            log.info("New file detected: {}", objectKey);

            UUID runId = extractRunIdFromObjectKey(objectKey);

            if(runId == null ) {
                log.warn("Could not extract runId from object key: {}", objectKey);
                return;
            }

            PipelineRun run = runRepository.findByIdForUpdate(runId)
                    .orElseThrow(() -> new RuntimeException("Run not found for the given run ID: " + runId));

            if(run.getStatus() == PipelineStatus.COMPLETED) {
                log.info("Run {} is already completed. No action needed.", runId);
                return;
            }

            log.info("Start processing runId: {}", runId);

            run.setStatus(PipelineStatus.COMPLETED);
            runRepository.save(run);

            log.info("Run {} marked as COMPLETED", runId);
            
        } catch (Exception e) {
            log.error("Error processing MinIO event: {}", message, e);
            throw new RuntimeException("Error processing MinIO event", e);
        }
    }

    private UUID extractRunIdFromObjectKey(String objectKey) {
        try {
            String filename = objectKey.contains("/") ? objectKey.substring(objectKey.lastIndexOf("/") + 1) : objectKey;
            
            String uuidPart = filename.split("_")[0];
            return UUID.fromString(uuidPart);

        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format in object key: {}", objectKey, e);
            return null;
        }
    }

}