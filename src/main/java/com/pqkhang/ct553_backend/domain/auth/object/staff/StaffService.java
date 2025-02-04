package com.pqkhang.ct553_backend.domain.auth.object.staff;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.auth.request.ChangePasswordRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface StaffService {
    Page<StaffDTO> getStaffs(Map<String, String> params) throws ResourceNotFoundException;

    List<StaffDTO> getAllStaffs();

    StaffDTO createStaff(StaffDTO staffDTO) throws ResourceNotFoundException;

    StaffDTO getStaffById(UUID id) throws ResourceNotFoundException;

    StaffDTO getLoggedInStaff();

    StaffDTO updateStaff(UUID id, StaffDTO staffDTO) throws ResourceNotFoundException;

    void deleteStaff(UUID id) throws ResourceNotFoundException;

    void changePassword(UUID id, ChangePasswordRequest changePasswordRequest) throws ResourceNotFoundException;

}
