package com.noteshub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public class NoteVersionDto {

    @Data
    public static class SaveRequest {
        @NotNull(message = "Note ID is required")
        private UUID noteId;

        private String contentText;
        private String fileUrl;
        private String fileHash;

        @NotBlank(message = "Change message is required")
        private String changeMessage;
    }

    @Data
    public static class RestoreRequest {
        @NotBlank(message = "Change message is required")
        private String changeMessage;
    }

    @Data
    public static class Response {
        private UUID id;
        private UUID noteId;
        private int versionNumber;
        private String contentText;
        private String fileUrl;
        private String fileHash;
        private String changeMessage;
        private LocalDateTime createdAt;
    }
}
