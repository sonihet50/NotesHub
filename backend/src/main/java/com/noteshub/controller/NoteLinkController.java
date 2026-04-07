package com.noteshub.controller;

import com.noteshub.dto.NoteLinkDto;
import com.noteshub.service.NoteLinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class NoteLinkController {

    private final NoteLinkService noteLinkService;

    @PostMapping
    public ResponseEntity<NoteLinkDto.Response> create(@Valid @RequestBody NoteLinkDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteLinkService.createLink(request));
    }

    @GetMapping("/note/{noteId}")
    public ResponseEntity<List<NoteLinkDto.LinkedNoteResponse>> getLinked(@PathVariable UUID noteId) {
        return ResponseEntity.ok(noteLinkService.getLinkedNotes(noteId));
    }
}
