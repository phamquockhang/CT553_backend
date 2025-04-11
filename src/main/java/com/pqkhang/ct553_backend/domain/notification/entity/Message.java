package com.pqkhang.ct553_backend.domain.notification.entity;

import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
import com.pqkhang.ct553_backend.domain.notification.enums.MessageStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message extends BaseEntity {
    @Id
    @Column(name = "message_id", columnDefinition = "UUID DEFAULT gen_random_uuid()")
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID messageId;

    @Column(nullable = false)
    String senderId;

    @Column(nullable = false)
    String receiverId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MessageStatusEnum status;

    @Column(nullable = false)
    String content;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    Conversation conversation;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = MessageStatusEnum.SENT;
        }
    }
}