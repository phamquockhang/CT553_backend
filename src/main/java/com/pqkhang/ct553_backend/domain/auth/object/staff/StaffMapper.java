package com.pqkhang.ct553_backend.domain.auth.object.staff;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.auth.object.customer.Customer;
import com.pqkhang.ct553_backend.domain.auth.object.customer.CustomerDTO;
import com.pqkhang.ct553_backend.domain.auth.object.enums.GenderEnum;
import com.pqkhang.ct553_backend.domain.auth.object.role.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface StaffMapper {
    @Mapping(target = "password", ignore = true)
    StaffDTO toStaffDTO(Staff staff);

    Staff toStaff(StaffDTO staffDTO);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role.permissions", ignore = true)
    default void updateStaffFromDTO(StaffDTO staffDTO, @MappingTarget Staff staff) throws ResourceNotFoundException {

        if (staffDTO.getLastName().equals(staff.getLastName()) && staffDTO.getFirstName().equals(staff.getFirstName()) && staffDTO.getDob().equals(staff.getDob()) && staffDTO.getGender().equals(staff.getGender().toString()) && staffDTO.getIsActivated().equals(staff.getIsActivated())) {
            throw new ResourceNotFoundException("Staff information no have change");
        }

        staff.setUpdatedAt( staffDTO.getUpdatedAt() );
        staff.setLastName( staffDTO.getLastName() );
        staff.setFirstName( staffDTO.getFirstName() );
        staff.setDob( staffDTO.getDob() );
        if ( staffDTO.getGender() != null ) {
            staff.setGender( Enum.valueOf( GenderEnum.class, staffDTO.getGender() ) );
        }
        else {
            staff.setGender( null );
        }
        staff.setIsActivated( staffDTO.getIsActivated() );
    }
}
