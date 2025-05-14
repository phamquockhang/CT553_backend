package com.pqkhang.ct553_backend.infrastructure.kafka.email;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE)
public class EmailProducer {

    @Value("${kafka.email.topic}")
    String emailTopic;

    final KafkaTemplate<String, EmailObject> kafkaTemplate;

    public void sendEmailMessage(EmailObject emailObject) {
        try {
            kafkaTemplate.send(emailTopic, emailObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
