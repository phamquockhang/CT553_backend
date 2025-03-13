package com.pqkhang.ct553_backend.domain.transaction.mapper;

import com.pqkhang.ct553_backend.domain.transaction.dto.PaymentMethodDTO;
import com.pqkhang.ct553_backend.domain.transaction.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PaymentMethodMapper.class})
public interface PaymentMethodMapper {
    PaymentMethodDTO toPaymentMethodDTO(PaymentMethod paymentMethod);

    PaymentMethod toPaymentMethod(PaymentMethodDTO paymentMethodDTO);

    void updatePaymentMethodFromDTO(@MappingTarget PaymentMethod paymentMethod, PaymentMethodDTO paymentMethodDTO);
}
