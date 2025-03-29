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
public class NotificationDTO {
    UUID notificationId;

    Boolean isRead;

    @NotBlank(message = "Notification type is required")
    String notificationType;

    String customerId;

    String staffId;

    String sellingOrderId;

    LocalDateTime createdAt;
}
