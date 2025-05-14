package com.pqkhang.ct553_backend.domain.user.repository;

import com.pqkhang.ct553_backend.domain.user.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID>, JpaSpecificationExecutor<Staff> {
    boolean existsByEmail(String email);

    Optional<Staff> findStaffByEmail(String email);

    @Query("SELECT s.email FROM Staff s WHERE s.isActivated = true AND s.role.roleId = 2")
    List<String> findAllStaffEmailsByIsActivatedTrue();
}
