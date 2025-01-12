package com.pqkhang.ct553_backend.domain.auth.object.permission;


import com.pqkhang.ct553_backend.domain.auth.object.role.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface PermissionMapper {
    PermissionDTO toPermissionDTO(Permission permission);
    Permission toPermission(PermissionDTO permissionDTO);
    void updatePermissionFromDTO(@MappingTarget Permission permission, PermissionDTO permissionDTO);
}
