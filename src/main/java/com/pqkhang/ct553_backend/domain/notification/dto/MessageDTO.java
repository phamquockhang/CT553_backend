package com.pqkhang.ct553_backend.domain.notification.dto;

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
public class MessageDTO {
    UUID messageId;

    @NotBlank(message = "Conversation id required")
    String conversationId;

    @NotBlank(message = "Sender id required")
    String senderId;

    @NotBlank(message = "Receiver id required")
    String receiverId;

    String status;

    @NotBlank(message = "Content required")
    String content;

    LocalDateTime createdAt;
}
