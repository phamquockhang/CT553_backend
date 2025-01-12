package com.pqkhang.ct553_backend.domain.auth.object.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> , JpaSpecificationExecutor<Role> {
    boolean existsRoleByRoleId(Long id);
}
