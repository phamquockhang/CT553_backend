package com.pqkhang.ct553_backend.domain.notification.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.notification.dto.MessageDTO;
import com.pqkhang.ct553_backend.domain.notification.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {

    MessageService messageService;

    @GetMapping
    public ApiResponse<Page<MessageDTO>> getNotifications(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<MessageDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(messageService.getMessages(params))
                .message("Get all messages successfully")
                .build();
    }

    @PostMapping
    public ApiResponse<Void> createNotification(@Valid @RequestBody MessageDTO messageDTO) {
        messageService.createMessage(messageDTO);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Create message successfully")
                .build();
    }

    @PutMapping("/{messageId}")
    public ApiResponse<Void> readNotification(@PathVariable("messageId") UUID messageId) throws ResourceNotFoundException {
        messageService.readMessage(messageId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Read message successfully")
                .build();
    }
}
