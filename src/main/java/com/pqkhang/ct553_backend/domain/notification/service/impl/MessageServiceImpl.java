package com.pqkhang.ct553_backend.domain.notification.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.notification.dto.MessageDTO;
import com.pqkhang.ct553_backend.domain.notification.entity.Conversation;
import com.pqkhang.ct553_backend.domain.notification.entity.Message;
import com.pqkhang.ct553_backend.domain.notification.enums.MessageStatusEnum;
import com.pqkhang.ct553_backend.domain.notification.mapper.MessageMapper;
import com.pqkhang.ct553_backend.domain.notification.repository.ConversationRepository;
import com.pqkhang.ct553_backend.domain.notification.repository.MessageRepository;
import com.pqkhang.ct553_backend.domain.notification.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MessageServiceImpl implements MessageService {

    MessageMapper messageMapper;
    MessageRepository messageRepository;


    static String DEFAULT_PAGE = "1";
    static String DEFAULT_PAGE_SIZE = "30";
    private final ConversationRepository conversationRepository;

    private Pageable createPageable(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", DEFAULT_PAGE));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", DEFAULT_PAGE_SIZE));
        return PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    private Page<MessageDTO> buildMessagePage(org.springframework.data.domain.Page<Message> messagePage, Pageable pageable) throws ResourceNotFoundException {
        if (messagePage.isEmpty()) {
            throw new ResourceNotFoundException("No messages found");
        }

        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(messagePage.getTotalPages())
                .total(messagePage.getTotalElements())
                .build();
        return Page.<MessageDTO>builder()
                .meta(meta)
                .data(messagePage.getContent()
                        .stream()
                        .map(messageMapper::toMessageDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    @SneakyThrows
    public Page<MessageDTO> getMessages(Map<String, String> params) {
        UUID convId = UUID.fromString(params.get("conversationId"));
        Pageable pageable = createPageable(params);
        org.springframework.data.domain.Page<Message> messagePage = messageRepository.findByConversation_ConversationId(convId, pageable);

        return buildMessagePage(messagePage, pageable);
    }

    @Override
    @SneakyThrows
    public void createMessage(MessageDTO MessageDTO) {
        Message message = messageMapper.toMessage(MessageDTO);

        UUID conversationId = UUID.fromString(MessageDTO.getConversationId());
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
        message.setConversation(conversation);

        messageRepository.save(message);
    }

    @Override
    public void readMessage(UUID messageId) throws ResourceNotFoundException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        message.setStatus(MessageStatusEnum.READ);
        messageRepository.save(message);
    }
}
