package com.pqkhang.ct553_backend.domain.auth.object.permission;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;

import java.util.List;
import java.util.Map;

public interface PermissionService {
    Page<PermissionDTO> getPermissions(Map<String, String> params) throws ResourceNotFoundException;
    List<PermissionDTO> getAllPermissions();
    PermissionDTO createPermission(PermissionDTO permissionDTO) throws ResourceNotFoundException;
    void deletePermission(Long id) throws ResourceNotFoundException;
    PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) throws ResourceNotFoundException;
}
