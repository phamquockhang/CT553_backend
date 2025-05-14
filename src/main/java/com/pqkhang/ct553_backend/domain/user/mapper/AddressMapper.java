package com.pqkhang.ct553_backend.domain.user.mapper;

import com.pqkhang.ct553_backend.domain.user.dto.AddressDTO;
import com.pqkhang.ct553_backend.domain.user.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO toAddressDTO(Address address);

    Address toAddress(AddressDTO addressDTO);
}
