package com.data.api.dto.response;

import java.util.UUID;

public record UploadResponse(UUID runId, String presignedUrl) {
    
}
