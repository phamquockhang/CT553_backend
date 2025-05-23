package com.pqkhang.ct553_backend.infrastructure.websocket.controller;

import com.pqkhang.ct553_backend.domain.notification.dto.MessageDTO;
import com.pqkhang.ct553_backend.domain.notification.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketController {

    MessageService messageService;

    @MessageMapping("/send-notification")
    @SendTo("/topic/notifications")
    public String sendNotification(String message) {
        return message;
    }

    // Nhận từ FE
    @MessageMapping("/chat.sendMessage")
    public void receiveMessageCreateMessage(MessageDTO messageDTO) {
        messageService.createMessage(messageDTO);
    }

    @MessageMapping("/chat.readMessage")
    public void receiveMessageReadMessage(MessageDTO messageDTO) {
        messageService.readMessage(messageDTO.getMessageId());
    }
}
