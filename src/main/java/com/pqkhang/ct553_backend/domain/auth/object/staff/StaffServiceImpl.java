package com.pqkhang.ct553_backend.domain.auth.object.staff;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.auth.object.role.Role;
import com.pqkhang.ct553_backend.domain.auth.object.role.RoleRepository;
import com.pqkhang.ct553_backend.domain.auth.request.ChangePasswordRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class StaffServiceImpl implements StaffService {

    StaffRepository staffRepository;
    StaffMapper staffMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Override
    @Transactional
    public StaffDTO createStaff(StaffDTO staffDTO) throws ResourceNotFoundException {
        if (staffRepository.existsByEmail(staffDTO.getEmail())) {
            throw new ResourceNotFoundException("Email already exists");
        } else {
            Staff staff = staffMapper.toStaff(staffDTO);
            staff.setPassword(passwordEncoder.encode(staffDTO.getPassword()));
            Role role = roleRepository.findById(staffDTO.getRole().getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Role ID " + staffDTO.getRole().getRoleId() + " is invalid."));
            staff.setRole(role);
//            staff.setRole();
            staffRepository.save(staff);
            return staffMapper.toStaffDTO(staff);
        }
    }

    @Override
    public Page<StaffDTO> getStaffs(Map<String, String> params) throws ResourceNotFoundException {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        org.springframework.data.domain.Page<Staff> staffPage = staffRepository.findAll(pageable);
        if (staffPage.isEmpty()) {
            throw new ResourceNotFoundException("No customer found");
        }
        return Page.<StaffDTO>builder()
                .meta(Meta.builder()
                        .page(pageable.getPageNumber() + 1)
                        .pageSize(pageable.getPageSize())
                        .pages(staffPage.getTotalPages())
                        .total(staffPage.getTotalElements())
                        .build())
                .data(staffPage.getContent().stream()
                        .map(staffMapper::toStaffDTO)
                        .collect(Collectors.toList()))
                .build();
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
    public StaffDTO updateStaff(UUID id, StaffDTO staffDTO) throws ResourceNotFoundException {
        Staff staff = staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff ID " + id + " is invalid."));

        if (!staff.getEmail().equals(staffDTO.getEmail())) {
            throw new ResourceNotFoundException("Email cannot be changed");
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
        }

        staff.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        staffRepository.save(staff);
    }
}

