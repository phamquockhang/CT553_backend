package com.pqkhang.ct553_backend.domain.user.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.request.SearchCriteria;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.auth.dto.request.ChangePasswordRequest;
import com.pqkhang.ct553_backend.domain.auth.entity.Role;
import com.pqkhang.ct553_backend.domain.auth.repository.RoleRepository;
import com.pqkhang.ct553_backend.domain.booking.cart.service.CartService;
import com.pqkhang.ct553_backend.domain.user.dto.CustomerDTO;
import com.pqkhang.ct553_backend.domain.user.dto.ScoreDTO;
import com.pqkhang.ct553_backend.domain.user.entity.Customer;
import com.pqkhang.ct553_backend.domain.user.entity.Score;
import com.pqkhang.ct553_backend.domain.user.mapper.CustomerMapper;
import com.pqkhang.ct553_backend.domain.user.mapper.ScoreMapper;
import com.pqkhang.ct553_backend.domain.user.repository.CustomerRepository;
import com.pqkhang.ct553_backend.domain.user.repository.StaffRepository;
import com.pqkhang.ct553_backend.domain.user.service.CustomerService;
import com.pqkhang.ct553_backend.domain.user.service.ScoreService;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import com.pqkhang.ct553_backend.infrastructure.utils.StringUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    StaffRepository staffRepository;
    CustomerMapper customerMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;
    ScoreMapper scoreMapper;
    private final ScoreService scoreService;
    private final CartService cartService;

    public CustomerDTO customCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = customerMapper.toCustomerDTO(customer);
        customerDTO.setScore(customer.getScores().stream()
                .filter(Score::getIsCurrent)
                .map(scoreMapper::toScoreDTO)
                .findFirst()
                .orElse(null));

        return customerDTO;
    }

    private Specification<Customer> getCustomerSpec(Map<String, String> params) {
        Specification<Customer> spec = Specification.where(null);
        if (params.containsKey("query")) {
            String searchValue = params.get("query").trim().toLowerCase();
            String[] searchValues = searchValue.split(",");
            spec = spec.or((root, query, criteriaBuilder) -> {
//                Join<Customer, Role> roleJoin = root.join("role", JoinType.LEFT);
                return criteriaBuilder.or(
                        Arrays.stream(searchValues)
                                .map(stringUtils::normalizeString)
                                .map(value -> "%" + value.trim().toLowerCase() + "%")
                                .map(likePattern -> criteriaBuilder.or(
                                        criteriaBuilder.like(
                                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("email"))),
                                                likePattern),
                                        criteriaBuilder.like(
                                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("firstName"))),
                                                likePattern),
                                        criteriaBuilder.like(
                                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("lastName"))),
                                                likePattern)
//                                        criteriaBuilder.like(
//                                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(roleJoin.get("description"))),
//                                                likePattern)
                                ))
                                .toArray(Predicate[]::new)
                );
            });
        }
        List<SearchCriteria> activeCriteria = requestParamUtils.getSearchCriteria(params, "isActivated");
        Specification<Customer> customerSpec = Specification.where(null);
        for (SearchCriteria criteria : activeCriteria) {
            customerSpec = customerSpec.or(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isActivated"), Boolean.parseBoolean(criteria.getValue().toString()))));
        }
        spec = spec.and(customerSpec);

        return spec;
    }

    @Override
    public Page<CustomerDTO> getCustomers(Map<String, String> params) throws ResourceNotFoundException {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Customer> spec = getCustomerSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Customer.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Customer> customerPage = customerRepository.findAll(spec, pageable);

        if (customerPage.isEmpty()) {
            throw new ResourceNotFoundException("No customer found");
        }

        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(customerPage.getTotalPages())
                .total(customerPage.getTotalElements())
                .build();
        return Page.<CustomerDTO>builder()
                .meta(meta)
                .data(customerPage.getContent().stream()
                        .map(this::customCustomerDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::customCustomerDTO)
                .toList();
    }

    @Override
    public CustomerDTO getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer ID " + id + " is invalid."));
        return customCustomerDTO(customer);
    }

    @Override
    public CustomerDTO getLoggedInCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Customer customer = customerRepository.findCustomerByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + authentication.getName()));
            return customCustomerDTO(customer);
        }
        return null;
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) throws ResourceNotFoundException {
        if (customerRepository.existsByEmail(customerDTO.getEmail()) || staffRepository.existsByEmail(customerDTO.getEmail())
        ) {
            throw new ResourceNotFoundException("Email này đã được sử dụng");
        } else {
            Customer customer = customerMapper.toCustomer(customerDTO);
            customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));

            // default role is customer
            Role role = roleRepository.findById(3L).orElseThrow(() -> new ResourceNotFoundException("Role ID " + customerDTO.getRole().getRoleId() + " is invalid."));
            customer.setRole(role);

            customerRepository.save(customer);

            ScoreDTO scoreDTO = new ScoreDTO();
            scoreDTO.setChangeAmount(0);
            scoreService.createScore(customer.getCustomerId(), scoreDTO);

            cartService.createCartByCustomerId(customer.getCustomerId());

            customerRepository.save(customer);

            return customerMapper.toCustomerDTO(customer);
        }
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(UUID id, CustomerDTO customerDTO) throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer ID " + id + " is invalid."));

        if (!customerDTO.getEmail().equals(customer.getEmail())) {
            throw new ResourceNotFoundException("Email cannot be changed");
        }

        // default role is customer
        Role role = roleRepository.findByName("CUSTOMER").orElseThrow(() -> new ResourceNotFoundException("Role name " + customerDTO.getRole().getName() + " is invalid."));
        customer.setRole(role);

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
        } else if (changePasswordRequest.getCurrentPassword().equals(changePasswordRequest.getNewPassword())) {
            throw new ResourceNotFoundException("New password must be different from the current password");
        }

        customer.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        customerRepository.save(customer);
    }

    @Override
    public UUID getCustomerIdByEmail(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + email));
        return customer.getCustomerId();
    }
}

