package com.pqkhang.ct553_backend.domain.auth.object.customer.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressDTO {
    UUID addressId;

    @NotNull(message = "Province id is required")
    Long provinceId;

    @NotNull(message = "District id is required")
    Long districtId;

    @NotBlank(message = "Ward code is required")
    String wardCode;

    String description;
    Boolean isDefault;

//    CustomerDTO customer;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
