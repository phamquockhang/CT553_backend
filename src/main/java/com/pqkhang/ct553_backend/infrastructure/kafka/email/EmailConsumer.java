package com.pqkhang.ct553_backend.infrastructure.kafka.email;

import com.pqkhang.ct553_backend.domain.common.service.impl.EmailServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@Slf4j
public class EmailConsumer {

    EmailServiceImpl emailServiceImpl;

    @KafkaListener(topics = "${kafka.email.topic}",
            concurrency = "${kafka.email.concurrency}",
            properties = {"spring.json.value.default.type=com.pqkhang.ct553_backend.infrastructure.kafka.email.EmailObject"}
    )
    @SneakyThrows
    public void listenEmailNotifications(@Payload EmailObject emailObject) {
        log.info("emailObject received: {}", emailObject);
        if (emailObject != null) {
            emailServiceImpl.sendEmailAsync(emailObject);
        } else {
            log.error("emailObject is null or invalid Kafka message payload");
        }
    }
}
