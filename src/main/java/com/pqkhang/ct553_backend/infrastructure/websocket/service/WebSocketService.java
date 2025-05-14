package com.pqkhang.ct553_backend.infrastructure.websocket.service;

import com.pqkhang.ct553_backend.domain.notification.dto.ConversationDTO;
import com.pqkhang.ct553_backend.domain.notification.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketService {

    SimpMessagingTemplate messagingTemplate;

    public void sendSystemNotification(UUID userId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
    }

    public void refetchConversation(UUID userId, ConversationDTO conversationDTO) {
        messagingTemplate.convertAndSend("/topic/conversations/" + userId, conversationDTO);
    }

    public void sendMessage(UUID conversationId, MessageDTO messageDTO) {
        messagingTemplate.convertAndSend("/topic/messages/" + conversationId, messageDTO);
    }

    public void markMessageAsRead(MessageDTO messageDTO) {
        messagingTemplate.convertAndSend("/topic/messages/" + messageDTO.getConversationId(), messageDTO);
    }
}
