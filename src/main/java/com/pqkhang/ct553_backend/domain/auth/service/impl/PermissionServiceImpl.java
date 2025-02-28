package com.pqkhang.ct553_backend.domain.auth.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.request.SearchCriteria;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.auth.dto.PermissionDTO;
import com.pqkhang.ct553_backend.domain.auth.entity.Permission;
import com.pqkhang.ct553_backend.domain.auth.mapper.PermissionMapper;
import com.pqkhang.ct553_backend.domain.auth.repository.PermissionRepository;
import com.pqkhang.ct553_backend.domain.auth.service.PermissionService;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import com.pqkhang.ct553_backend.infrastructure.utils.StringUtils;
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
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    RequestParamUtils requestParamUtils;
    private final StringUtils stringUtils;

    private Permission findPermissionById(Long id) throws ResourceNotFoundException {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found permission with id: " + id));
    }

    @Override
    public Page<PermissionDTO> getPermissions(Map<String, String> params) throws ResourceNotFoundException {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));

        Specification<Permission> spec = getPermissionSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Permission.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Permission> permissionPage = permissionRepository.findAll(spec, pageable);

        if (permissionPage.isEmpty()) {
            throw new ResourceNotFoundException("No permission found");
        }

        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(permissionPage.getTotalPages())
                .total(permissionPage.getTotalElements())
                .build();
        return Page.<PermissionDTO>builder()
                .meta(meta)
                .data(permissionPage.getContent().stream()
                        .map(permissionMapper::toPermissionDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private Specification<Permission> getPermissionSpec(Map<String, String> params) {
        Specification<Permission> spec = Specification.where(null);

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
                                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("apiPath"))),
                                                likePattern)
                                ))
                                .toArray(Predicate[]::new)
                );
            });
        }

        List<SearchCriteria> methodCriteria = requestParamUtils.getSearchCriteria(params, "method");
        List<SearchCriteria> moduleCriteria = requestParamUtils.getSearchCriteria(params, "module");
        Specification<Permission> methodSpec = Specification.where(null);
        for (SearchCriteria criteria : methodCriteria) {
            methodSpec = methodSpec.or(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("method"), criteria.getValue())));
        }
        Specification<Permission> moduleSpec = Specification.where(null);
        for (SearchCriteria criteria : moduleCriteria) {
            moduleSpec = moduleSpec.or(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("module"), criteria.getValue())));
        }
        spec = spec.and(methodSpec).and(moduleSpec);
        return spec;
    }

    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionDTO)
                .toList();
    }

    @Override
    @Transactional
    public PermissionDTO createPermission(PermissionDTO permissionDTO) throws ResourceNotFoundException {
        if (permissionRepository.existsByModuleAndApiPathAndMethod(permissionDTO.getModule(), permissionDTO.getApiPath(), permissionDTO.getMethod())) {
            throw new ResourceNotFoundException("Quyền này đã tồn tại");
        }
        Permission newPermission = permissionRepository.save(permissionMapper.toPermission(permissionDTO));
        return permissionMapper.toPermissionDTO(newPermission);
    }

    @Override
    public void deletePermission(Long id) throws ResourceNotFoundException {
        Permission permission = findPermissionById(id);
        permissionRepository.delete(permission);
    }

    @Override
    @Transactional
    public PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) throws ResourceNotFoundException {
        Permission permissionToUpdate = findPermissionById(id);
        if (permissionRepository.existsByNameAndModuleAndApiPathAndMethod(permissionDTO.getName(), permissionDTO.getModule(), permissionDTO.getApiPath(), permissionDTO.getMethod())) {
            throw new ResourceNotFoundException("Quyền này đã tồn tại");
        }

        permissionMapper.updatePermissionFromDTO(permissionToUpdate, permissionDTO);
        return permissionMapper.toPermissionDTO(permissionRepository.save(permissionToUpdate));
    }

}