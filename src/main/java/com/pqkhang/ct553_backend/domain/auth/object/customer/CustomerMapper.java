package com.pqkhang.ct553_backend.domain.auth.object.customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "password", ignore = true)
    CustomerDTO toCustomerDTO(Customer Customer);

    Customer toCustomer(CustomerDTO CustomerDTO);

    @Mapping(target = "password", ignore = true)
//    @Mapping(target = "role.permissions", ignore = true)
    void updateCustomerFromDTO(CustomerDTO CustomerDTO, @MappingTarget Customer Customer);
}
