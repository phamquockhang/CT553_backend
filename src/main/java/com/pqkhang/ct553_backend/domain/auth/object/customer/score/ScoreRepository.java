package com.pqkhang.ct553_backend.domain.auth.object.customer.score;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID>, JpaSpecificationExecutor<Score> {
    List<Score> findAllByCustomer_CustomerId(UUID customerCustomerId);

    Score findByCustomer_CustomerIdAndIsCurrent(UUID customerId, boolean b);
}
