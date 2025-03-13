package com.pqkhang.ct553_backend.domain.transaction.dto;

import com.pqkhang.ct553_backend.domain.transaction.dto.response.VNPayResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentMethodDTO {
    Integer paymentMethodId;

    @NotBlank(message = "Payment method name is required")
    String paymentMethodName;

    VNPayResponse vnPayResponse;

    String paymentUrl;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}