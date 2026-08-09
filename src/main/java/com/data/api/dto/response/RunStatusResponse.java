package com.data.api.dto.response;

import java.util.UUID;
import com.data.api.model.PipelineStatus;

public record RunStatusResponse(UUID runId, PipelineStatus status) {
    
}
