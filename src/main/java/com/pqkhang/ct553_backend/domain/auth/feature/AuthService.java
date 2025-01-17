package com.pqkhang.ct553_backend.domain.auth.feature;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.auth.object.customer.CustomerDTO;
import com.pqkhang.ct553_backend.domain.auth.request.AuthRequest;
import com.pqkhang.ct553_backend.domain.auth.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

public interface AuthService {
    CustomerDTO registerCustomer(CustomerDTO customerDTO) throws ResourceNotFoundException;
    AuthResponse login(@Valid AuthRequest authRequest, HttpServletResponse response);
    void logout(HttpServletResponse httpServletResponse) throws ResourceNotFoundException;

//    AuthResponse loginCustomer(@Valid AuthRequest authRequest, HttpServletResponse response);
//    AuthResponse loginStaff(@Valid AuthRequest authRequest, HttpServletResponse response);

//    AuthResponse login(AuthRequest authRequest, HttpServletResponse response);
//
//    AuthResponse refreshAccessToken(String refreshToken);
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
//
//    void verifyResetToken(String token) throws ResourceNotFoundException;
}
