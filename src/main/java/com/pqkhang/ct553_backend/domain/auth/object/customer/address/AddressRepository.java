package com.pqkhang.ct553_backend.domain.auth.object.customer.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID>, JpaSpecificationExecutor<Address> {
    List<Address> findAllByCustomer_CustomerId(UUID customerId);

    Address findAddressesByCustomer_CustomerIdAndProvinceIdAndDistrictIdAndWardCodeAndDescription(UUID customerCustomerId, Long provinceId, Long districtId, String wardCode, String description);

    Address findByCustomer_CustomerIdAndIsDefault(UUID customerCustomerId, Boolean isDefault);
}
