package com.pqkhang.ct553_backend.infrastructure.websocket.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketController {
    @MessageMapping("/send-notification")
    @SendTo("/topic/notifications")
    public String sendNotification(String message) {
        return message;
    }
}
