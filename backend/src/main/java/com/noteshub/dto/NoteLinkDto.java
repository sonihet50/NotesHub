package com.noteshub.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public class NoteLinkDto {

    @Data
    public static class CreateRequest {
        @NotNull(message = "fromNoteId is required")
        private UUID fromNoteId;

        @NotNull(message = "toNoteId is required")
        private UUID toNoteId;
    }

    @Data
    public static class Response {
        private UUID id;
        private UUID fromNoteId;
        private String fromNoteTitle;
        private UUID toNoteId;
        private String toNoteTitle;
        private LocalDateTime createdAt;
    }

    @Data
    public static class LinkedNoteResponse {
        private UUID noteId;
        private String noteTitle;
        private String direction;
        private LocalDateTime linkedAt;
    }
}
