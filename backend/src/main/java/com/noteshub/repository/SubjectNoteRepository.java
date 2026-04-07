package com.noteshub.repository;

import com.noteshub.entity.SubjectNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubjectNoteRepository extends JpaRepository<SubjectNote, UUID> {
    List<SubjectNote> findAllByOwnerId(UUID ownerId);
}
