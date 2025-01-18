package com.pqkhang.ct553_backend.domain.auth.object.customer;

import com.pqkhang.ct553_backend.domain.auth.object.customer.address.AddressDTO;
import com.pqkhang.ct553_backend.domain.auth.object.role.RoleDTO;
import jakarta.validation.constraints.NotBlank;
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
    UUID customerId;
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

    RoleDTO role;

    AddressDTO address;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
