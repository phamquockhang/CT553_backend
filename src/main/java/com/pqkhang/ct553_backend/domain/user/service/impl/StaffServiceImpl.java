package com.pqkhang.ct553_backend.domain.user.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.request.SearchCriteria;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.auth.dto.request.ChangePasswordRequest;
import com.pqkhang.ct553_backend.domain.auth.entity.Role;
import com.pqkhang.ct553_backend.domain.auth.repository.RoleRepository;
import com.pqkhang.ct553_backend.domain.user.dto.StaffDTO;
import com.pqkhang.ct553_backend.domain.user.entity.Staff;
import com.pqkhang.ct553_backend.domain.user.mapper.StaffMapper;
import com.pqkhang.ct553_backend.domain.user.repository.CustomerRepository;
import com.pqkhang.ct553_backend.domain.user.repository.StaffRepository;
import com.pqkhang.ct553_backend.domain.user.service.StaffService;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import com.pqkhang.ct553_backend.infrastructure.utils.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class StaffServiceImpl implements StaffService {

    StaffRepository staffRepository;
    CustomerRepository customerRepository;
    StaffMapper staffMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;

    // getStaffSpec by email, firstName, lastName and role
    private Specification<Staff> getStaffSpec(Map<String, String> params) {
        Specification<Staff> spec = Specification.where(null);
        if (params.containsKey("query")) {
            String searchValue = params.get("query").trim().toLowerCase();
            String[] searchValues = searchValue.split(",");
            spec = spec.or((root, query, criteriaBuilder) -> {
                Join<Staff, Role> roleJoin = root.join("role", JoinType.LEFT);
                return criteriaBuilder.or(
                        Arrays.stream(searchValues)
                                .map(stringUtils::normalizeString)
                                .map(value -> "%" + value.trim().toLowerCase() + "%")
                                .map(likePattern -> criteriaBuilder.or(
                                        criteriaBuilder.like(
                                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("email"))),
                                                likePattern),
                                        criteriaBuilder.like(
                                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("firstName"))),
                                                likePattern),
                                        criteriaBuilder.like(
                                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("lastName"))),
                                                likePattern),
                                        criteriaBuilder.like(
                                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(roleJoin.get("description"))),
                                                likePattern)
                                ))
                                .toArray(Predicate[]::new)
                );
            });
        }
        List<SearchCriteria> activeCriteria = requestParamUtils.getSearchCriteria(params, "isActivated");
        Specification<Staff> staffSpec = Specification.where(null);
        for (SearchCriteria criteria : activeCriteria) {
            staffSpec = staffSpec.or(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isActivated"), Boolean.parseBoolean(criteria.getValue().toString()))));
        }
        spec = spec.and(staffSpec);

        return spec;
    }

    @Override
    public Page<StaffDTO> getStaffs(Map<String, String> params) throws ResourceNotFoundException {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Staff> spec = getStaffSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Staff.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Staff> staffPage = staffRepository.findAll(spec, pageable);

        if (staffPage.isEmpty()) {
            throw new ResourceNotFoundException("No staff found");
        }

        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(staffPage.getTotalPages())
                .total(staffPage.getTotalElements())
                .build();
        return Page.<StaffDTO>builder()
                .meta(meta)
                .data(staffPage.getContent().stream()
                        .map(staffMapper::toStaffDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<StaffDTO> getAllStaffs() {
        return staffRepository.findAll().stream()
                .map(staffMapper::toStaffDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StaffDTO getStaffById(UUID id) {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Staff ID " + id + " is invalid."));
        return staffMapper.toStaffDTO(staff);
    }

    @Override
    public StaffDTO getLoggedInStaff() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Staff staff = staffRepository.findStaffByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found: " + authentication.getName()));
            return staffMapper.toStaffDTO(staff);
        }
        return null;
    }

    @Override
    @Transactional
    public StaffDTO createStaff(StaffDTO staffDTO) throws ResourceNotFoundException {
        if (staffRepository.existsByEmail(staffDTO.getEmail()) || customerRepository.existsByEmail(staffDTO.getEmail())
        ) {
            throw new ResourceNotFoundException("Email already exists");
        } else {
            Staff staff = staffMapper.toStaff(staffDTO);
            staff.setPassword(passwordEncoder.encode(staffDTO.getPassword()));

            // default role is staff
            Role role = roleRepository.findById(2L).orElseThrow(() -> new ResourceNotFoundException("Role ID " + staffDTO.getRole().getRoleId() + " is invalid."));
            staff.setRole(role);

            staffRepository.save(staff);
            return staffMapper.toStaffDTO(staff);
        }
    }

    @Override
    @Transactional
    public StaffDTO updateStaff(UUID id, StaffDTO staffDTO) throws ResourceNotFoundException {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff ID " + id + " is invalid."));

        if (!staff.getEmail().equals(staffDTO.getEmail())) {
            throw new ResourceNotFoundException("Email cannot be changed");
        }

        if (staffDTO.getRole() != null && staffDTO.getRole().getRoleId().equals(1L)) {
            Role role = roleRepository.findByName("MANAGER").orElseThrow(() -> new ResourceNotFoundException("Role name " + staffDTO.getRole().getName() + " is invalid."));
            staff.setRole(role);
        } else {
            // default role is staff
            Role role = roleRepository.findByName("STAFF").orElseThrow(() -> new ResourceNotFoundException("Role name " + staffDTO.getRole().getName() + " is invalid."));
            staff.setRole(role);
        }


        staffMapper.updateStaffFromDTO(staffDTO, staff);
        staffRepository.save(staff);
        return staffMapper.toStaffDTO(staff);
    }

    @Override
    @Transactional
    public void deleteStaff(UUID id) throws ResourceNotFoundException {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff ID " + id + " is invalid."));
        staffRepository.delete(staff);
    }

    @Override
    @Transactional
    public void changePassword(UUID id, ChangePasswordRequest changePasswordRequest) throws ResourceNotFoundException {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff ID " + id + " not found"));

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), staff.getPassword())) {
            throw new ResourceNotFoundException("Current password is incorrect");
        } else if (changePasswordRequest.getCurrentPassword().equals(changePasswordRequest.getNewPassword())) {
            throw new ResourceNotFoundException("New password must be different from the current password");
        }

        staff.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        staffRepository.save(staff);
    }
}

