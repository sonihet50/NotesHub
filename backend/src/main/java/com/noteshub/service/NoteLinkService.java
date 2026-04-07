package com.noteshub.service;

import com.noteshub.dto.NoteLinkDto;
import com.noteshub.entity.Note;
import com.noteshub.entity.NoteLink;
import com.noteshub.repository.NoteLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteLinkService {

    private final NoteLinkRepository noteLinkRepository;
    private final NoteService noteService;

    @Transactional
    public NoteLinkDto.Response createLink(NoteLinkDto.CreateRequest request) {
        if (request.getFromNoteId().equals(request.getToNoteId())) {
            throw new IllegalArgumentException("A note cannot link to itself");
        }
        if (noteLinkRepository.existsByFromNoteIdAndToNoteId(request.getFromNoteId(), request.getToNoteId())) {
            throw new IllegalArgumentException("Link already exists between these notes");
        }

        Note fromNote = noteService.findNote(request.getFromNoteId());
        Note toNote = noteService.findNote(request.getToNoteId());

        NoteLink link = NoteLink.builder()
                .fromNote(fromNote)
                .toNote(toNote)
                .build();

        return toResponse(noteLinkRepository.save(link));
    }

    @Transactional(readOnly = true)
    public List<NoteLinkDto.LinkedNoteResponse> getLinkedNotes(UUID noteId) {
        noteService.findNote(noteId);
        List<NoteLinkDto.LinkedNoteResponse> result = new ArrayList<>();

        noteLinkRepository.findAllByFromNoteId(noteId).forEach(link -> {
            NoteLinkDto.LinkedNoteResponse item = new NoteLinkDto.LinkedNoteResponse();
            item.setNoteId(link.getToNote().getId());
            item.setNoteTitle(link.getToNote().getTitle());
            item.setDirection("OUTGOING");
            item.setLinkedAt(link.getCreatedAt());
            result.add(item);
        });

        noteLinkRepository.findAllByToNoteId(noteId).forEach(link -> {
            NoteLinkDto.LinkedNoteResponse item = new NoteLinkDto.LinkedNoteResponse();
            item.setNoteId(link.getFromNote().getId());
            item.setNoteTitle(link.getFromNote().getTitle());
            item.setDirection("INCOMING");
            item.setLinkedAt(link.getCreatedAt());
            result.add(item);
        });

        return result;
    }

    private NoteLinkDto.Response toResponse(NoteLink link) {
        NoteLinkDto.Response res = new NoteLinkDto.Response();
        res.setId(link.getId());
        res.setFromNoteId(link.getFromNote().getId());
        res.setFromNoteTitle(link.getFromNote().getTitle());
        res.setToNoteId(link.getToNote().getId());
        res.setToNoteTitle(link.getToNote().getTitle());
        res.setCreatedAt(link.getCreatedAt());
        return res;
    }
}
