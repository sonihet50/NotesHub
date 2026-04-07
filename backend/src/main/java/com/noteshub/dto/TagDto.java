package com.noteshub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public class TagDto {

    @Data
    public static class CreateRequest {
        @NotBlank(message = "Tag name is required")
        private String name;
    }

    @Data
    public static class Response {
        private UUID id;
        private String name;
        private LocalDateTime createdAt;
    }
}
