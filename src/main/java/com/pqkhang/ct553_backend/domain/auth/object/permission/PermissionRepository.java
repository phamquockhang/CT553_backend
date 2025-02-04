package com.pqkhang.ct553_backend.domain.auth.object.permission;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    boolean existsByModuleAndApiPathAndMethod(String module, String apiPath, String method);

//    List<Permission> findByPermissionIdIn(List<Long> ids);

    boolean existsByNameAndModuleAndApiPathAndMethod(String name, String module, String apiPath, String method);
}
