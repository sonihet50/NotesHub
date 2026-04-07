package com.noteshub.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "note_links",
    uniqueConstraints = @UniqueConstraint(columnNames = {"from_note_id", "to_note_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteLink {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_note_id", nullable = false)
    private Note fromNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_note_id", nullable = false)
    private Note toNote;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
