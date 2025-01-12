package com.pqkhang.ct553_backend.domain.auth.object.staff;

import com.pqkhang.ct553_backend.domain.auth.object.enums.GenderEnum;
import com.pqkhang.ct553_backend.domain.auth.object.role.Role;
import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
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
    @Column(name = "id", columnDefinition = "UUID DEFAULT gen_random_uuid()")
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String lastName;
    String firstName;
    LocalDate dob;

    @Column(updatable = false)
    String email;

    String password;

    @Enumerated(EnumType.STRING)
    GenderEnum gender;

    Boolean isActivated;

    @PrePersist
    public void prePersist() {
        if (isActivated == null) {
            isActivated = true;
        }
    }

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

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
