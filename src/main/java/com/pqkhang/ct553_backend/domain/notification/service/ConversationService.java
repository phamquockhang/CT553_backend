package com.pqkhang.ct553_backend.domain.notification.service;

import com.pqkhang.ct553_backend.domain.notification.dto.ConversationDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ConversationService {
    List<ConversationDTO> getConversations(Map<String, String> params);

    void createConversation(@Valid ConversationDTO conversationDTO);
}
