package com.pqkhang.ct553_backend.domain.auth.object.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pqkhang.ct553_backend.domain.auth.object.permission.PermissionDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTO {
    Long roleId;

    @NotBlank(message = "Role name is required")
    String roleName;

    String description;

    @JsonProperty("isActive")
    boolean isActive;

    List<PermissionDTO> permissions;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
