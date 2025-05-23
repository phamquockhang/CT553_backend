package com.pqkhang.ct553_backend.domain.user.entity;

import com.pqkhang.ct553_backend.domain.auth.entity.Role;
import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
import com.pqkhang.ct553_backend.domain.user.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "staffs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Staff extends BaseEntity implements UserDetails {

    @Id
    @Column(name = "staff_id", columnDefinition = "UUID DEFAULT gen_random_uuid()")
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID staffId;

    Integer processedOrders;
    Integer delayedOrders;

    String lastName;
    String firstName;
    LocalDate dob;

    @Column(updatable = false)
    String email;

    String password;

    @Enumerated(EnumType.STRING)
    GenderEnum gender;

    Boolean isActivated;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    @PrePersist
    public void prePersist() {
        if (isActivated == null) {
            isActivated = true;
        }
        if (processedOrders == null) {
            processedOrders = 0;
        }
        if (delayedOrders == null) {
            delayedOrders = 0;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return isActivated;
    }
}
