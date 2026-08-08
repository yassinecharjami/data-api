package com.data.api.service;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.Http.Method;

@Service
public class MinioService {

    private final MinioClient minioClient;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }
    
    public String generatePresignedUrl(String bucketName, String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(bucketName)
                .object(objectName)
                .expiry(1,TimeUnit.HOURS)
                .build()
            );
        } catch (MinioException e) {
            throw new RuntimeException("Error generating presigned URL", e);
        }
    }
}
