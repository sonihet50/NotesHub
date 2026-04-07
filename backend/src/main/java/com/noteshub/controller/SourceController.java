package com.noteshub.controller;

import com.noteshub.dto.SourceDto;
import com.noteshub.service.SourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sources")
@RequiredArgsConstructor
public class SourceController {

    private final SourceService sourceService;

    @PostMapping
    public ResponseEntity<SourceDto.Response> add(@Valid @RequestBody SourceDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sourceService.addSource(request));
    }

    @GetMapping("/note/{noteId}")
    public ResponseEntity<List<SourceDto.Response>> getByNote(@PathVariable UUID noteId) {
        return ResponseEntity.ok(sourceService.getSourcesForNote(noteId));
    }

    @DeleteMapping("/{sourceId}")
    public ResponseEntity<Void> delete(@PathVariable UUID sourceId) {
        sourceService.deleteSource(sourceId);
        return ResponseEntity.noContent().build();
    }
}
