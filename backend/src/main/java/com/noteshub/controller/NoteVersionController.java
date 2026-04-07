package com.noteshub.controller;

import com.noteshub.dto.NoteVersionDto;
import com.noteshub.service.NoteVersionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/versions")
@RequiredArgsConstructor
public class NoteVersionController {

    private final NoteVersionService noteVersionService;

    @PostMapping
    public ResponseEntity<NoteVersionDto.Response> save(@Valid @RequestBody NoteVersionDto.SaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteVersionService.saveVersion(request));
    }

    @GetMapping("/note/{noteId}")
    public ResponseEntity<List<NoteVersionDto.Response>> getHistory(@PathVariable UUID noteId) {
        return ResponseEntity.ok(noteVersionService.getHistory(noteId));
    }

    @PostMapping("/{versionId}/restore")
    public ResponseEntity<NoteVersionDto.Response> restore(
            @PathVariable UUID versionId,
            @Valid @RequestBody NoteVersionDto.RestoreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(noteVersionService.restoreVersion(versionId, request));
    }
}
