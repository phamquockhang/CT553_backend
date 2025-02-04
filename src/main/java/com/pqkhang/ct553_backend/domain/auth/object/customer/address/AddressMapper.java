package com.pqkhang.ct553_backend.domain.auth.object.customer.address;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO toAddressDTO(Address address);

    Address toAddress(AddressDTO addressDTO);
}
