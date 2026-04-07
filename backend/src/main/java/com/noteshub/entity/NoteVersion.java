package com.noteshub.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "note_versions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    @Column(name = "version_number", nullable = false)
    private int versionNumber;

    @Column(name = "content_text", columnDefinition = "TEXT")
    private String contentText;

    @Column(name = "file_url", columnDefinition = "TEXT")
    private String fileUrl;

    @Column(name = "file_hash")
    private String fileHash;

    @Column(name = "change_message", nullable = false)
    private String changeMessage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
