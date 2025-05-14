package com.pqkhang.ct553_backend.domain.notification.repository;

import com.pqkhang.ct553_backend.domain.notification.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID>, JpaSpecificationExecutor<Message> {
    Page<Message> findByConversation_ConversationId(UUID convId, Pageable pageable);
}
