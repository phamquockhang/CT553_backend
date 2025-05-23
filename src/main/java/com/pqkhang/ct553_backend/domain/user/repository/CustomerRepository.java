package com.pqkhang.ct553_backend.domain.user.repository;

import com.pqkhang.ct553_backend.domain.user.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {
    boolean existsByEmail(String email);

    Optional<Customer> findCustomerByEmail(String email);
}
