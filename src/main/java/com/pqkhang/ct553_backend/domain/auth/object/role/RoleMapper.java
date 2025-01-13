package com.pqkhang.ct553_backend.domain.auth.object.role;

import com.pqkhang.ct553_backend.domain.auth.object.permission.PermissionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
//    @Mapping(target = "isActive", source = "active")
    RoleDTO toRoleDTO(Role role);
    Role toRole(RoleDTO roleDTO);
    void updateRoleFromDTO(@MappingTarget Role role, RoleDTO roleDTO);
}
