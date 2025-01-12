package com.pqkhang.ct553_backend.domain.auth.object.customer;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.auth.object.role.Role;
import com.pqkhang.ct553_backend.domain.auth.object.role.RoleRepository;
import com.pqkhang.ct553_backend.domain.auth.request.ChangePasswordRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) throws ResourceNotFoundException {
        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new ResourceNotFoundException("Email already exists");
        } else {
            Customer customer = customerMapper.toCustomer(customerDTO);
            customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
            Role role = roleRepository.findById(customerDTO.getRole().getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Role ID " + customerDTO.getRole().getRoleId() + " is invalid."));
            customer.setRole(role);
//            customer.setRole();
            customerRepository.save(customer);
            return customerMapper.toCustomerDTO(customer);
        }
    }

    @Override
    public Page<CustomerDTO> getCustomers(Map<String, String> params) throws ResourceNotFoundException {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        org.springframework.data.domain.Page<Customer> customerPage = customerRepository.findAll(pageable);
        if (customerPage.isEmpty()) {
            throw new ResourceNotFoundException("No customer found");
        }
        return Page.<CustomerDTO>builder()
                .meta(Meta.builder()
                        .page(pageable.getPageNumber() + 1)
                        .pageSize(pageable.getPageSize())
                        .pages(customerPage.getTotalPages())
                        .total(customerPage.getTotalElements())
                        .build())
                .data(customerPage.getContent().stream()
                        .map(customerMapper::toCustomerDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public CustomerDTO getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer ID " + id + " is invalid."));
        return customerMapper.toCustomerDTO(customer);
    }

    @Override
    public CustomerDTO getLoggedInCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Customer customer = customerRepository.findCustomerByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + authentication.getName()));
            return customerMapper.toCustomerDTO(customer);
        }
        return null;
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(UUID id, CustomerDTO customerDTO) throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer ID " + id + " is invalid."));

        if (!customer.getEmail().equals(customerDTO.getEmail())) {
            throw new ResourceNotFoundException("Email cannot be changed");
        }

        if (customerDTO.getRole() != null) {
            Role role = roleRepository.findById(customerDTO.getRole().getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role ID " + customerDTO.getRole().getRoleId() + " is invalid."));
            customer.setRole(role);
        }

        customerMapper.updateCustomerFromDTO(customerDTO, customer);
        customerRepository.save(customer);
        return customerMapper.toCustomerDTO(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID id) throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer ID " + id + " is invalid."));
        customerRepository.delete(customer);
    }

    @Override
    @Transactional
    public void changePassword(UUID id, ChangePasswordRequest changePasswordRequest) throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer ID " + id + " not found"));

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), customer.getPassword())) {
            throw new ResourceNotFoundException("Current password is incorrect");
        }

        customer.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        customerRepository.save(customer);
    }
}

