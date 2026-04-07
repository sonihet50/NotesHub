package com.noteshub.controller;

import com.noteshub.dto.SubjectNoteDto;
import com.noteshub.service.SubjectNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subject-notes")
@RequiredArgsConstructor
public class SubjectNoteController {

    private final SubjectNoteService subjectNoteService;

    @PostMapping
    public ResponseEntity<SubjectNoteDto.Response> create(@Valid @RequestBody SubjectNoteDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectNoteService.create(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubjectNoteDto.Response>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(subjectNoteService.getByUser(userId));
    }

    @PostMapping("/{subjectNoteId}/fork")
    public ResponseEntity<SubjectNoteDto.Response> fork(
            @PathVariable UUID subjectNoteId,
            @RequestParam UUID newOwnerId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subjectNoteService.fork(subjectNoteId, newOwnerId));
    }
}
