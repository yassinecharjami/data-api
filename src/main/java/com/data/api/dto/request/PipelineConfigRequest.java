package com.data.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PipelineConfigRequest(
    @NotBlank(message = "Format must not be blank")
    @Pattern(regexp = "^(json|csv|parquet)$", message = "Format must be one of: json, csv, parquet")
    String format,
    
    @NotBlank(message = "Source must not be blank")
    @Pattern(regexp = "^(local)$", message = "Source must be one of: local")
    String source,
    
    @NotBlank(message = "Destination must not be blank")
    @Pattern(regexp = "^(minio)$", message = "Destination must be one of: minio")
    String destination,
    
    @NotBlank(message = "Team must not be blank")
    String team) {
}