package com.data.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresignedUrlRequest {
    private String bucketName;
    private String objectName;
}