package com.noteshub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "note_tags")
@IdClass(NoteTag.NoteTagId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteTag {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteTagId implements Serializable {
        private UUID note;
        private UUID tag;
    }
}
