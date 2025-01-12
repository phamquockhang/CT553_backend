package com.pqkhang.ct553_backend.domain.auth.object.staff;

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
    void updateStaffFromDTO(StaffDTO staffDTO, @MappingTarget Staff staff);
}
