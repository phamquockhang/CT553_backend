package com.pqkhang.ct553_backend.domain.auth.object.customer.address;

import com.pqkhang.ct553_backend.domain.auth.object.customer.Customer;
import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address extends BaseEntity  {

    @Id
    @Column(name = "address_id", columnDefinition = "UUID DEFAULT gen_random_uuid()")
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID addressId;

    Long provinceId;
    Long districtId;
    String wardCode;
    String description;

    Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @PrePersist
    public void prePersist() {
        if (isDefault == null) {
            isDefault = true;
        }
    }

}
