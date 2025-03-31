package com.pqkhang.ct553_backend.domain.notification.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.notification.dto.NotificationDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public interface NotificationService {
    Page<NotificationDTO> getNotifications(Map<String, String> params) throws ResourceNotFoundException;

    void createNotification(@Valid NotificationDTO notificationDTO);

    void readNotification(UUID notificationId) throws ResourceNotFoundException;
}
