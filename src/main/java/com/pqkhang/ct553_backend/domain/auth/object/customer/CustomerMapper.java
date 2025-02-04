package com.pqkhang.ct553_backend.domain.auth.object.customer;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.auth.object.enums.GenderEnum;
import com.pqkhang.ct553_backend.domain.auth.object.role.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface CustomerMapper {
    @Mapping(target = "password", ignore = true)
    CustomerDTO toCustomerDTO(Customer Customer);

    Customer toCustomer(CustomerDTO CustomerDTO);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role.permissions", ignore = true)
    default void updateCustomerFromDTO(CustomerDTO CustomerDTO, @MappingTarget Customer Customer) throws ResourceNotFoundException {

        if (CustomerDTO.getLastName().equals(Customer.getLastName()) && CustomerDTO.getFirstName().equals(Customer.getFirstName()) && CustomerDTO.getDob().equals(Customer.getDob()) && CustomerDTO.getGender().equals(Customer.getGender().toString()) && CustomerDTO.getIsActivated().equals(Customer.getIsActivated())) {
            throw new ResourceNotFoundException("Không có thông tin nào thay đổi!");
        }

        Customer.setUpdatedAt(CustomerDTO.getUpdatedAt());
        Customer.setLastName(CustomerDTO.getLastName());
        Customer.setFirstName(CustomerDTO.getFirstName());
        Customer.setDob(CustomerDTO.getDob());
        if (CustomerDTO.getGender() != null) {
            Customer.setGender(Enum.valueOf(GenderEnum.class, CustomerDTO.getGender()));
        } else {
            Customer.setGender(null);
        }
        Customer.setIsActivated(CustomerDTO.getIsActivated());
    }
}
