package com.noteshub.controller;

import com.noteshub.dto.TagDto;
import com.noteshub.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping("/api/tags")
    public ResponseEntity<TagDto.Response> create(@Valid @RequestBody TagDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.createTag(request));
    }

    @GetMapping("/api/tags")
    public ResponseEntity<List<TagDto.Response>> getAll() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @PostMapping("/api/notes/{noteId}/tags/{tagId}")
    public ResponseEntity<Void> addTag(@PathVariable UUID noteId, @PathVariable UUID tagId) {
        tagService.addTagToNote(noteId, tagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/api/notes/{noteId}/tags/{tagId}")
    public ResponseEntity<Void> removeTag(@PathVariable UUID noteId, @PathVariable UUID tagId) {
        tagService.removeTagFromNote(noteId, tagId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/notes/{noteId}/tags")
    public ResponseEntity<List<TagDto.Response>> getTagsForNote(@PathVariable UUID noteId) {
        return ResponseEntity.ok(tagService.getTagsForNote(noteId));
    }
}
