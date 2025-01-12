package com.pqkhang.ct553_backend.domain.auth.object.permission;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionDTO {
    Long permissionId;
    String name;
    String apiPath;
    String method;
    String module;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
