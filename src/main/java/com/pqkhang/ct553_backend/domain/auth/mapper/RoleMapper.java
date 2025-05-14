package com.pqkhang.ct553_backend.domain.auth.mapper;

import com.pqkhang.ct553_backend.domain.auth.dto.RoleDTO;
import com.pqkhang.ct553_backend.domain.auth.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    //    @Mapping(target = "isActive", source = "active")
    RoleDTO toRoleDTO(Role role);

    Role toRole(RoleDTO roleDTO);

    void updateRoleFromDTO(@MappingTarget Role role, RoleDTO roleDTO);
}
