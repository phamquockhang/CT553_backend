package com.pqkhang.ct553_backend.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthRequest {
    @NotNull
//    @NotBlank(message = "Email is required")
    String email;

    @NotNull
//    @NotBlank(message = "Password is required")
    String password;
}
