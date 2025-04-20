package com.pqkhang.ct553_backend.domain.notification.mapper;

import com.pqkhang.ct553_backend.domain.notification.dto.MessageDTO;
import com.pqkhang.ct553_backend.domain.notification.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "conversationId", source = "conversation.conversationId")
    MessageDTO toMessageDTO(Message message);

    Message toMessage(MessageDTO messageDTO);
}
