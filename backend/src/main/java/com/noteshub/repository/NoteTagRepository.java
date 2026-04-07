package com.noteshub.repository;

import com.noteshub.entity.NoteTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoteTagRepository extends JpaRepository<NoteTag, NoteTag.NoteTagId> {
    List<NoteTag> findAllByNoteId(UUID noteId);
    boolean existsByNoteIdAndTagId(UUID noteId, UUID tagId);
    void deleteByNoteIdAndTagId(UUID noteId, UUID tagId);
}
