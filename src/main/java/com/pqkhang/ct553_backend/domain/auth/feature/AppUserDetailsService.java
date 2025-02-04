package com.pqkhang.ct553_backend.domain.auth.feature;

import com.pqkhang.ct553_backend.domain.auth.object.customer.Customer;
import com.pqkhang.ct553_backend.domain.auth.object.customer.CustomerRepository;
import com.pqkhang.ct553_backend.domain.auth.object.staff.Staff;
import com.pqkhang.ct553_backend.domain.auth.object.staff.StaffRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AppUserDetailsService implements UserDetailsService {
    CustomerRepository customerRepository;
    StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findCustomerByEmail(email).orElse(null);
        if (customer != null) {
            List<GrantedAuthority> authoritiesCustomer = List.of(new SimpleGrantedAuthority(customer.getRole().getName()));
            return new User(customer.getEmail(), customer.getPassword(), authoritiesCustomer);
        }

        Staff staff = staffRepository.findStaffByEmail(email).orElse(null);
        if (staff != null) {
            List<GrantedAuthority> authoritiesStaff = List.of(new SimpleGrantedAuthority(staff.getRole().getName()));
            return new User(staff.getEmail(), staff.getPassword(), authoritiesStaff);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
