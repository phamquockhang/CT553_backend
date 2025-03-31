package com.pqkhang.ct553_backend.domain.notification.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.notification.dto.NotificationDTO;
import com.pqkhang.ct553_backend.domain.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    NotificationService notificationService;

    @GetMapping
    public ApiResponse<Page<NotificationDTO>> getNotifications(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<NotificationDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(notificationService.getNotifications(params))
                .message("Get all notifications successfully")
                .build();
    }

    @PostMapping
    public ApiResponse<Void> createNotification(@Valid @RequestBody NotificationDTO notificationDTO) {
        notificationService.createNotification(notificationDTO);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Create notification successfully")
                .build();
    }

    @PutMapping("/{notificationId}")
    public ApiResponse<Void> readNotification(@PathVariable("notificationId") UUID notificationId) throws ResourceNotFoundException {
        notificationService.readNotification(notificationId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Read notification successfully")
                .build();
    }
}
