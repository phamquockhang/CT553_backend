package com.pqkhang.ct553_backend.infrastructure.kafka.email;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailObject {
    String receiverEmail;

    String subject;

    String templateFileName;

    Map<String, Object> context;
}
