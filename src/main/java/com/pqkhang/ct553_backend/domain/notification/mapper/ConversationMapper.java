package com.pqkhang.ct553_backend.domain.notification.mapper;

import com.pqkhang.ct553_backend.domain.notification.dto.ConversationDTO;
import com.pqkhang.ct553_backend.domain.notification.entity.Conversation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    ConversationDTO toConversationDTO(Conversation conversation);

    Conversation toConversation(ConversationDTO conversationDTO);
}
