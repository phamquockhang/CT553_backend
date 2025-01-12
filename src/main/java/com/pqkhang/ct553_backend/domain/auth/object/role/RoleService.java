package com.pqkhang.ct553_backend.domain.auth.object.role;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;

import java.util.List;
import java.util.Map;

public interface RoleService {
    RoleDTO createRole (RoleDTO roleDTO) throws ResourceNotFoundException;
    Page<RoleDTO> getRoles(Map<String, String> params);
    RoleDTO getRoleById(Long id) throws ResourceNotFoundException;
    void deleteRole(Long id) throws ResourceNotFoundException;
    RoleDTO updateRole(Long id, RoleDTO roleDTO) throws ResourceNotFoundException;
    List<RoleDTO> getAllRoles();
}
