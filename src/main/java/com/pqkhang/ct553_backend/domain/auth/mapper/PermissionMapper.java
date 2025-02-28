package com.pqkhang.ct553_backend.domain.auth.mapper;


import com.pqkhang.ct553_backend.domain.auth.dto.PermissionDTO;
import com.pqkhang.ct553_backend.domain.auth.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface PermissionMapper {
    PermissionDTO toPermissionDTO(Permission permission);

    Permission toPermission(PermissionDTO permissionDTO);

    void updatePermissionFromDTO(@MappingTarget Permission permission, PermissionDTO permissionDTO);
}
