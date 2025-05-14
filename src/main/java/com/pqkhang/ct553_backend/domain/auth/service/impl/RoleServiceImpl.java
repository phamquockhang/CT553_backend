package com.pqkhang.ct553_backend.domain.auth.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.request.SearchCriteria;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.auth.dto.RoleDTO;
import com.pqkhang.ct553_backend.domain.auth.entity.Role;
import com.pqkhang.ct553_backend.domain.auth.mapper.RoleMapper;
import com.pqkhang.ct553_backend.domain.auth.repository.RoleRepository;
import com.pqkhang.ct553_backend.domain.auth.service.RoleService;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import com.pqkhang.ct553_backend.infrastructure.utils.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
    private final StringUtils stringUtils;

    private Specification<Role> getRoleSpec(Map<String, String> params) {
        Specification<Role> spec = Specification.where(null);
        if (params.containsKey("query")) {
            String searchValue = params.get("query").trim().toLowerCase();
            String[] searchValues = searchValue.split(",");
            spec = spec.or((root, query, criteriaBuilder) -> {
//                Join<Staff, Role> roleJoin = root.join("role", JoinType.LEFT);
                return criteriaBuilder.or(
                        Arrays.stream(searchValues)
                                .map(stringUtils::normalizeString)
                                .map(value -> "%" + value.trim().toLowerCase() + "%")
                                .map(likePattern -> criteriaBuilder.or(
                                        criteriaBuilder.like(
                                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("name"))),
                                                likePattern),
                                        criteriaBuilder.like(
                                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("description"))),
                                                likePattern)
                                ))
                                .toArray(Predicate[]::new)
                );
            });
        }
        List<SearchCriteria> activeCriteria = requestParamUtils.getSearchCriteria(params, "isActivated");
        Specification<Role> roleSpec = Specification.where(null);
        for (SearchCriteria criteria : activeCriteria) {
            roleSpec = roleSpec.or(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isActivated"), Boolean.parseBoolean(criteria.getValue().toString()))));
        }
        spec = spec.and(roleSpec);

        return spec;
    }

    @Override
    public Page<RoleDTO> getRoles(Map<String, String> params) throws ResourceNotFoundException {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Role> spec = getRoleSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Role.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Role> rolePage = roleRepository.findAll(spec, pageable);

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
        return roleMapper.toRoleDTO(roleRepository.save(newRole));
    }

}