package com.noteshub.dto;

import com.noteshub.enums.SourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class SourceDto {

    @Data
    public static class CreateRequest {
        @NotNull(message = "Note ID is required")
        private UUID noteId;

        @NotNull(message = "Source type is required")
        private SourceType sourceType;

        @NotBlank(message = "Source title is required")
        private String sourceTitle;

        private String sourceUrl;
        private Map<String, Object> metadata;
    }

    @Data
    public static class Response {
        private UUID id;
        private UUID noteId;
        private SourceType sourceType;
        private String sourceTitle;
        private String sourceUrl;
        private Map<String, Object> metadata;
        private LocalDateTime createdAt;
    }
}
