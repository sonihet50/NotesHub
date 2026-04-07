package com.noteshub.service;

import com.noteshub.dto.NoteVersionDto;
import com.noteshub.entity.Note;
import com.noteshub.entity.NoteVersion;
import com.noteshub.repository.NoteVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteVersionService {

    private final NoteVersionRepository noteVersionRepository;
    private final NoteService noteService;

    @Transactional
    public NoteVersionDto.Response saveVersion(NoteVersionDto.SaveRequest request) {
        Note note = noteService.findNote(request.getNoteId());

        int nextNumber = noteVersionRepository
                .findTopByNoteIdOrderByVersionNumberDesc(note.getId())
                .map(v -> v.getVersionNumber() + 1)
                .orElse(1);

        NoteVersion version = NoteVersion.builder()
                .note(note)
                .versionNumber(nextNumber)
                .contentText(request.getContentText())
                .fileUrl(request.getFileUrl())
                .fileHash(request.getFileHash())
                .changeMessage(request.getChangeMessage())
                .build();

        NoteVersion saved = noteVersionRepository.save(version);
        note.setCurrentVersion(saved);
        noteService.save(note);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<NoteVersionDto.Response> getHistory(UUID noteId) {
        noteService.findNote(noteId);
        return noteVersionRepository.findAllByNoteIdOrderByVersionNumberDesc(noteId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public NoteVersionDto.Response restoreVersion(UUID versionId, NoteVersionDto.RestoreRequest request) {
        NoteVersion target = noteVersionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        Note note = target.getNote();

        int nextNumber = noteVersionRepository
                .findTopByNoteIdOrderByVersionNumberDesc(note.getId())
                .map(v -> v.getVersionNumber() + 1)
                .orElse(1);

        NoteVersion restored = NoteVersion.builder()
                .note(note)
                .versionNumber(nextNumber)
                .contentText(target.getContentText())
                .fileUrl(target.getFileUrl())
                .fileHash(target.getFileHash())
                .changeMessage(request.getChangeMessage())
                .build();

        NoteVersion saved = noteVersionRepository.save(restored);
        note.setCurrentVersion(saved);
        noteService.save(note);

        return toResponse(saved);
    }

    private NoteVersionDto.Response toResponse(NoteVersion v) {
        NoteVersionDto.Response res = new NoteVersionDto.Response();
        res.setId(v.getId());
        res.setNoteId(v.getNote().getId());
        res.setVersionNumber(v.getVersionNumber());
        res.setContentText(v.getContentText());
        res.setFileUrl(v.getFileUrl());
        res.setFileHash(v.getFileHash());
        res.setChangeMessage(v.getChangeMessage());
        res.setCreatedAt(v.getCreatedAt());
        return res;
    }
}
