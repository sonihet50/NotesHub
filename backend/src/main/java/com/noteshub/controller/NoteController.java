package com.noteshub.controller;

import com.noteshub.dto.NoteDto;
import com.noteshub.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteDto.Response> create(@Valid @RequestBody NoteDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.createNote(request));
    }

    @GetMapping("/subject/{subjectNoteId}")
    public ResponseEntity<List<NoteDto.Response>> getBySubjectNote(@PathVariable UUID subjectNoteId) {
        return ResponseEntity.ok(noteService.getNotesBySubjectNote(subjectNoteId));
    }
}
