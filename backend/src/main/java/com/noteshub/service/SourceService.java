package com.noteshub.service;

import com.noteshub.dto.SourceDto;
import com.noteshub.entity.Note;
import com.noteshub.entity.Source;
import com.noteshub.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SourceService {

    private final SourceRepository sourceRepository;
    private final NoteService noteService;

    @Transactional
    public SourceDto.Response addSource(SourceDto.CreateRequest request) {
        Note note = noteService.findNote(request.getNoteId());

        Source source = Source.builder()
                .note(note)
                .sourceType(request.getSourceType())
                .sourceTitle(request.getSourceTitle())
                .sourceUrl(request.getSourceUrl())
                .metadata(request.getMetadata())
                .build();

        return toResponse(sourceRepository.save(source));
    }

    @Transactional(readOnly = true)
    public List<SourceDto.Response> getSourcesForNote(UUID noteId) {
        noteService.findNote(noteId);
        return sourceRepository.findAllByNoteId(noteId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deleteSource(UUID sourceId) {
        if (!sourceRepository.existsById(sourceId)) {
            throw new IllegalArgumentException("Source not found: " + sourceId);
        }
        sourceRepository.deleteById(sourceId);
    }

    private SourceDto.Response toResponse(Source s) {
        SourceDto.Response res = new SourceDto.Response();
        res.setId(s.getId());
        res.setNoteId(s.getNote().getId());
        res.setSourceType(s.getSourceType());
        res.setSourceTitle(s.getSourceTitle());
        res.setSourceUrl(s.getSourceUrl());
        res.setMetadata(s.getMetadata());
        res.setCreatedAt(s.getCreatedAt());
        return res;
    }
}
