package com.pqkhang.ct553_backend.domain.auth.feature;


import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.auth.object.customer.Customer;
import com.pqkhang.ct553_backend.domain.auth.object.customer.CustomerDTO;
import com.pqkhang.ct553_backend.domain.auth.object.customer.CustomerMapper;
import com.pqkhang.ct553_backend.domain.auth.object.customer.CustomerRepository;
import com.pqkhang.ct553_backend.domain.auth.object.permission.PermissionRepository;
import com.pqkhang.ct553_backend.domain.auth.object.role.Role;
import com.pqkhang.ct553_backend.domain.auth.object.role.RoleRepository;
import com.pqkhang.ct553_backend.domain.auth.object.staff.Staff;
import com.pqkhang.ct553_backend.domain.auth.object.staff.StaffDTO;
import com.pqkhang.ct553_backend.domain.auth.object.staff.StaffMapper;
import com.pqkhang.ct553_backend.domain.auth.object.staff.StaffRepository;
import com.pqkhang.ct553_backend.domain.auth.request.AuthRequest;
import com.pqkhang.ct553_backend.domain.auth.response.AuthResponse;
import com.pqkhang.ct553_backend.infrastructure.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Override
    public CustomerDTO registerCustomer(CustomerDTO customerDTO) throws ResourceNotFoundException {
        boolean isEmailExist = this.customerRepository.existsByEmail(customerDTO.getEmail());
        if (isEmailExist) {
            throw new DataIntegrityViolationException("Email " + customerDTO.getEmail() + " already exists, please use another email.");
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
        return customerMapper.toCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public AuthResponse loginCustomer(AuthRequest authRequest, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        Customer customer = customerRepository.findCustomerByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtUtils.generateAccessToken(customer);
        String refreshToken = jwtUtils.generateRefreshToken(customer);

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

//    @Override
//    public AuthResponse login(AuthRequest authRequest, HttpServletResponse response) {
//
//    }
//
//    @Override
//    public AuthResponse refreshAccessToken(String refreshToken) {
//        Jwt jwtRefreshToken = jwtDecoder.decode(refreshToken);
//        String username = jwtUtils.getUsername(jwtRefreshToken);
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return AuthResponse.builder()
//                .accessToken(jwtUtils.generateAccessToken(user))
//                .build();
//    }
//
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