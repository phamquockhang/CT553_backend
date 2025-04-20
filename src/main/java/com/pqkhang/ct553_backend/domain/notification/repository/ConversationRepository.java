package com.pqkhang.ct553_backend.domain.notification.repository;

import com.pqkhang.ct553_backend.domain.notification.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID>, JpaSpecificationExecutor<Conversation> {
    Conversation findByParticipantId1AndParticipantId2(String participantId1, String participantId2);

    List<Conversation> findByParticipantId1OrParticipantId2(String participantId1, String participantId2);
}