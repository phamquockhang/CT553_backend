package com.pqkhang.ct553_backend.domain.auth.object.role;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    @PersistenceContext
    EntityManager entityManager;
    RequestParamUtils requestParamUtils;

    @Override
    public Page<RoleDTO> getRoles(Map<String, String> params) throws ResourceNotFoundException {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Role.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Role> rolePage = roleRepository.findAll( pageable);

        if (rolePage.isEmpty()) {
            throw new ResourceNotFoundException("No roles found");
        }

        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(rolePage.getTotalPages())
                .total(rolePage.getTotalElements())
                .build();
        return Page.<RoleDTO>builder()
                .meta(meta)
                .data(rolePage.getContent().stream()
                        .map(roleMapper::toRoleDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRoleById(Long id) throws ResourceNotFoundException {
        return roleRepository.findById(id)
                .map(roleMapper::toRoleDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Not found role with ID " + id));
    }

    @Override
    public void deleteRole(Long id) throws ResourceNotFoundException {
        if (!roleRepository.existsRoleByRoleId(id)) {
            throw new ResourceNotFoundException("Role ID " + id + " is invalid.");
        }
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) throws ResourceNotFoundException {
        Role roleToUpdate = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found role with ID " + id));
        roleMapper.updateRoleFromDTO(roleToUpdate, roleDTO);
        return roleMapper.toRoleDTO(roleRepository.save(roleToUpdate));
    }

    @Override
    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) throws ResourceNotFoundException {
        Role newRole = roleMapper.toRole(roleDTO);
//        newRole.getPermissions().forEach(permission -> {
//            if (permission.getPermissionId() != null) {
//                entityManager.merge(permission);
//            }
//        });
        return roleMapper.toRoleDTO(roleRepository.save(newRole));
    }

}