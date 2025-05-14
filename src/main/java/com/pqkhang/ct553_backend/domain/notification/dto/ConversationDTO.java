package com.pqkhang.ct553_backend.domain.notification.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationDTO {
    UUID conversationId;

    @NotBlank(message = "ParticipantId1 required")
    @Column(nullable = false)
    String participantId1;

    @NotBlank(message = "participantId2 required")
    @Column(nullable = false)
    String participantId2;

    String lastMessageContent;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
