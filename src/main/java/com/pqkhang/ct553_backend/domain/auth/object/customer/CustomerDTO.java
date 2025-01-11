package com.pqkhang.ct553_backend.domain.auth.object.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDTO {
    UUID id;
    @NotBlank(message = "First name is required")
    String firstName;
    @NotBlank(message = "Last name is required")
    String lastName;
//    @NotNull(message = "Date of birth is required")
    LocalDate dob;
    @NotBlank(message = "Email is required")
    String email;
    @NotBlank(message = "Password is required")
    String password;
    @NotBlank(message = "Gender is required")
    String gender;
    Boolean isActivated;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
