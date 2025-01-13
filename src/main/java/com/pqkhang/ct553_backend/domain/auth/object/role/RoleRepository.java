package com.pqkhang.ct553_backend.domain.auth.object.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> , JpaSpecificationExecutor<Role> {
    boolean existsRoleByRoleId(Long id);
    Optional<Role> findByName(String name);
}
