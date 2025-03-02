package com.pqkhang.ct553_backend.domain.auth.service.impl;


import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.auth.dto.request.AuthRequest;
import com.pqkhang.ct553_backend.domain.auth.dto.response.AuthResponse;
import com.pqkhang.ct553_backend.domain.auth.entity.Role;
import com.pqkhang.ct553_backend.domain.auth.repository.PermissionRepository;
import com.pqkhang.ct553_backend.domain.auth.repository.RoleRepository;
import com.pqkhang.ct553_backend.domain.auth.service.AuthService;
import com.pqkhang.ct553_backend.domain.booking.cart.service.CartService;
import com.pqkhang.ct553_backend.domain.user.dto.CustomerDTO;
import com.pqkhang.ct553_backend.domain.user.dto.ScoreDTO;
import com.pqkhang.ct553_backend.domain.user.entity.Customer;
import com.pqkhang.ct553_backend.domain.user.entity.Staff;
import com.pqkhang.ct553_backend.domain.user.mapper.CustomerMapper;
import com.pqkhang.ct553_backend.domain.user.mapper.StaffMapper;
import com.pqkhang.ct553_backend.domain.user.repository.CustomerRepository;
import com.pqkhang.ct553_backend.domain.user.repository.StaffRepository;
import com.pqkhang.ct553_backend.domain.user.service.ScoreService;
import com.pqkhang.ct553_backend.infrastructure.audit.AuditAwareImpl;
import com.pqkhang.ct553_backend.infrastructure.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;

    PermissionRepository permissionRepository;
    StaffRepository staffRepository;
    StaffMapper staffMapper;
    JwtDecoder jwtDecoder;
    AuditAwareImpl auditAware;
    ScoreService scoreService;
    CartService cartService;

    @Override
    public CustomerDTO registerCustomer(CustomerDTO customerDTO) throws ResourceNotFoundException {
        boolean isEmailCustomerExist = this.customerRepository.existsByEmail(customerDTO.getEmail());
        boolean isEmailStaffExist = this.staffRepository.existsByEmail(customerDTO.getEmail());
        if (isEmailCustomerExist || isEmailStaffExist) {
            throw new DataIntegrityViolationException("Email này đã được sử dụng! Vui lòng chọn email khác.");
        }

        Customer customer = customerMapper.toCustomer(customerDTO);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Role role = roleRepository.findByName("CUSTOMER").orElseThrow(() -> new ResourceNotFoundException("Role name CUSTOMER is invalid."));

//        if (role.isEmpty()) {
//            role = Optional.of(new Role());
//            role.get().setName("CUSTOMER");
//            role.get().setIsActivated(true);
//            role.get().setDescription("Khách hàng");
//        }

        customer.setRole(role);

        customerRepository.save(customer);
        ScoreDTO scoreDTO = new ScoreDTO();
        scoreDTO.setChangeAmount(0);
        scoreService.createScore(customer.getCustomerId(), scoreDTO);

        cartService.createCartByCustomerId(customer.getCustomerId());


        return customerMapper.toCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public AuthResponse login(AuthRequest authRequest, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        String accessToken = "";
        String refreshToken = "";
        Staff staff = staffRepository.findStaffByEmail(authRequest.getEmail()).orElse(null);
        Customer customer = customerRepository.findCustomerByEmail(authRequest.getEmail()).orElse(null);

//        System.out.println("&&&&&&&&&&&&&&&&&&&Staff: " + staff);
//        System.out.println("&&&&&&&&&&&&&&&&&&&Customer: " + customer);

        if (staff != null) {
            if (!staff.getIsActivated()) {
                throw new RuntimeException("Tài khoản nhân viên chưa kích hoạt!");
            }
            accessToken = jwtUtils.generateAccessToken(staff);
            refreshToken = jwtUtils.generateRefreshToken(staff);

        } else if (customer != null) {
            if (!customer.getIsActivated()) {
                throw new RuntimeException("Tài khoản khách hàng chưa kích hoạt!");
            }
            accessToken = jwtUtils.generateAccessToken(customer);
            refreshToken = jwtUtils.generateRefreshToken(customer);
        } else {
            throw new RuntimeException("Không tìm thấy tài khoản!");
        }

        // Store refresh token in http only cookie
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

        response.addCookie(refreshTokenCookie);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public void logout(HttpServletResponse httpServletResponse) throws ResourceNotFoundException {
        String email = auditAware.getCurrentAuditor().orElse("");

        if (email.isEmpty()) {
            throw new ResourceNotFoundException("Access Token not valid");
        }
        SecurityContextHolder.clearContext();
        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        httpServletResponse.addCookie(refreshTokenCookie);
    }

    @Override
    public AuthResponse refreshAccessToken(String refreshToken) {
        Jwt jwtRefreshToken = jwtDecoder.decode(refreshToken);
        String username = jwtUtils.getUsername(jwtRefreshToken);
        Customer customer = customerRepository.findCustomerByEmail(username).orElse(null);
        Staff staff = staffRepository.findStaffByEmail(username).orElse(null);

        if (customer == null && staff == null) {
            throw new RuntimeException("User not found");
        }
        if (customer != null && !customer.getIsActivated()) {
            throw new RuntimeException("Tài khoản khách hàng chưa kích hoạt!");
        }
        if (staff != null && !staff.getIsActivated()) {
            throw new RuntimeException("Tài khoản nhân viên chưa kích hoạt!");
        }

        return AuthResponse.builder()
                .accessToken(jwtUtils.generateAccessToken(customer != null ? customer : staff))
                .build();
    }

//    @Override
//    public void logout(HttpServletResponse response) throws ResourceNotFoundException {
//        String email = auditAware.getCurrentAuditor().orElse("");
//
//        if (email.isEmpty()) {
//            throw new ResourceNotFoundException("Access Token not valid");
//        }
//        SecurityContextHolder.clearContext();
//        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(0);
//
//        response.addCookie(refreshTokenCookie);
//    }
//
//    @Override
//    public UserDTO processOAuthPostLogin(OidcUser oidcUser) {
//        String email = oidcUser.getEmail();
//        String name = oidcUser.getFullName();
//
//        Optional<User> existingUser = userRepository.findByEmail(email);
//        User user;
//        if (existingUser.isPresent()) {
//            user = existingUser.get();
//        } else {
//            user = new User();
//            user.setEmail(email);
//            user.setFirstName(name.split(" ")[0]);
//            user.setLastName(name.split(" ").length > 1 ? name.split(" ")[1] : "");
//            user.setPassword("");
//
//            Optional<Role> role = roleRepository.findByRoleName("USER");
//            if (role.isEmpty()) {
//                role = Optional.of(new Role());
//                role.get().setRoleName("USER");
//                role.get().setActive(true);
//                role.get().setDescription("User role");
//                role = Optional.of(roleRepository.save(role.get()));
//            }
//
//            user.setRole(role.get());
//            user = userRepository.save(user);
//        }
//
//        return userMapper.toUserDTO(user);
//    }
//
//    @Override
//    public void forgotPassword(String email, String siteUrl) throws ResourceNotFoundException, MessagingException, UnsupportedEncodingException {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));
//        if (!user.isActive()) {
//            throw new ResourceNotFoundException("User account is not active");
//        }
//
//        String tokenMail = UUID.randomUUID().toString();
//        redisService.set(tokenMail, user.getEmail(), 60 * 60 * 2 * 1000); // 2 hours expiration in milliseconds
//
//        String resetPasswordLink = siteUrl + "/reset-password?token=" + tokenMail;
//        emailService.sendPasswordResetEmail(user, resetPasswordLink);
//    }
//
//    @Override
//    public void resetPassword(String verifyToken, String newPassword) throws ResourceNotFoundException {
//        String email = (String) redisService.get(verifyToken);
//        if (email == null) {
//            throw new ResourceNotFoundException("Invalid token");
//        }
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        if (!user.isActive()) {
//            throw new ResourceNotFoundException("User account is not active");
//        }
//        user.setPassword(passwordEncoder.encode(newPassword));
//        userRepository.save(user);
//    }
//
//    @Override
//    public void verifyResetToken(String verifyToken) throws ResourceNotFoundException {
//        String email = (String) redisService.get(verifyToken);
//        if (email == null) {
//            throw new ResourceNotFoundException("Invalid token");
//        }
//    }
}