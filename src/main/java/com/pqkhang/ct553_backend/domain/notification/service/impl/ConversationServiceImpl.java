package com.pqkhang.ct553_backend.domain.notification.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.notification.dto.ConversationDTO;
import com.pqkhang.ct553_backend.domain.notification.entity.Conversation;
import com.pqkhang.ct553_backend.domain.notification.mapper.ConversationMapper;
import com.pqkhang.ct553_backend.domain.notification.repository.ConversationRepository;
import com.pqkhang.ct553_backend.domain.notification.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ConversationServiceImpl implements ConversationService {

    ConversationMapper conversationMapper;
    ConversationRepository conversationRepository;

    static String DEFAULT_PAGE = "1";
    static String DEFAULT_PAGE_SIZE = "30";

    private Pageable createPageable(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", DEFAULT_PAGE));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", DEFAULT_PAGE_SIZE));
        return PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "updatedAt"));
    }

    @SneakyThrows
    private Page<ConversationDTO> buildConversationPage(org.springframework.data.domain.Page<Conversation> conversationPage, Pageable pageable) {
        if (conversationPage.isEmpty()) {
            throw new ResourceNotFoundException("No conversations found");
        }

        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(conversationPage.getTotalPages())
                .total(conversationPage.getTotalElements())
                .build();
        return Page.<ConversationDTO>builder()
                .meta(meta)
                .data(conversationPage.getContent()
                        .stream()
                        .map(conversationMapper::toConversationDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    @SneakyThrows
    public List<ConversationDTO> getConversations(Map<String, String> params) {
        String participantId = params.get("participantId");
        if (participantId == null || participantId.trim().isEmpty()) {
            throw new IllegalArgumentException("Participant ID cannot be null or empty");
        }

        List<Conversation> conversations = conversationRepository
                .findByParticipantId1OrParticipantId2(participantId, participantId);

        if (conversations.isEmpty()) {
            throw new ResourceNotFoundException("No conversations found");
        }

        return conversations.stream()
                .map(conversationMapper::toConversationDTO)
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public void createConversation(ConversationDTO conversationDTO) {
        Conversation conversation = conversationRepository.findByParticipantId1AndParticipantId2(
                conversationDTO.getParticipantId1(),
                conversationDTO.getParticipantId2()
        );
        if (conversation != null) {
            throw new IllegalStateException("Conversation already exists");
        }

        Conversation newConversation = conversationMapper.toConversation(conversationDTO);
        conversationRepository.save(newConversation);
    }
}
