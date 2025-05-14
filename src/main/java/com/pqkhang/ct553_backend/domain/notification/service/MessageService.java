package com.pqkhang.ct553_backend.domain.notification.service;

import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.notification.dto.MessageDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public interface MessageService {
    Page<MessageDTO> getMessages(Map<String, String> params);

    void createMessage(@Valid MessageDTO MessageDTO);

    void readMessage(UUID messageId);
}
