package com.pqkhang.ct553_backend.domain.auth.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.auth.dto.request.AuthRequest;
import com.pqkhang.ct553_backend.domain.auth.dto.response.AuthResponse;
import com.pqkhang.ct553_backend.domain.user.dto.CustomerDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

public interface AuthService {
    CustomerDTO registerCustomer(CustomerDTO customerDTO) throws ResourceNotFoundException;

    AuthResponse login(@Valid AuthRequest authRequest, HttpServletResponse response);

    void logout(HttpServletResponse httpServletResponse) throws ResourceNotFoundException;

    AuthResponse refreshAccessToken(String refreshToken);
//    AuthResponse loginCustomer(@Valid AuthRequest authRequest, HttpServletResponse response);
//
//    AuthResponse loginStaff(@Valid AuthRequest authRequest, HttpServletResponse response);
//
//    AuthResponse login(AuthRequest authRequest, HttpServletResponse response);
//
//    void logout(HttpServletResponse httpServletResponse) throws ResourceNotFoundException;
//
//    UserDTO processOAuthPostLogin(OidcUser oidcUser);
//
//    UserDTO verifyUser(String token) throws ResourceNotFoundException;
//
//    void forgotPassword(String email, String siteUrl) throws ResourceNotFoundException, MessagingException, UnsupportedEncodingException;
//
//    void resetPassword(String token, String newPassword) throws ResourceNotFoundException;
}
