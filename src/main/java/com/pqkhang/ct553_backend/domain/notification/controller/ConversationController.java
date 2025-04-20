package com.pqkhang.ct553_backend.domain.notification.controller;

import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.domain.notification.dto.ConversationDTO;
import com.pqkhang.ct553_backend.domain.notification.service.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/conversations")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ConversationController {

    ConversationService conversationService;

    @GetMapping
    public ApiResponse<List<ConversationDTO>> getConversations(@RequestParam Map<String, String> params) {
        return ApiResponse.<List<ConversationDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(conversationService.getConversations(params))
                .message("Get all conversations successfully")
                .build();
    }

    @PostMapping
    public ApiResponse<Void> createConversation(@Valid @RequestBody ConversationDTO conversationDTO) {
        conversationService.createConversation(conversationDTO);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Create conversation successfully")
                .build();
    }
}
