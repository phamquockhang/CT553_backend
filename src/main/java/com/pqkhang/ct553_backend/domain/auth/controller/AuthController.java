package com.pqkhang.ct553_backend.domain.auth.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.domain.auth.dto.request.AuthRequest;
import com.pqkhang.ct553_backend.domain.auth.dto.response.AuthResponse;
import com.pqkhang.ct553_backend.domain.auth.service.AuthService;
import com.pqkhang.ct553_backend.domain.user.dto.CustomerDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/register/customer")
    public ApiResponse<CustomerDTO> registerCustomer(@RequestBody CustomerDTO customerDTO) throws ResourceNotFoundException {
        return ApiResponse.<CustomerDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Đăng ký thành công")
                .payload(authService.registerCustomer(customerDTO))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest,
                                           HttpServletResponse response) {
        return ApiResponse.<AuthResponse>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Đăng nhập thành công")
                .payload(authService.login(authRequest, response))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) throws ResourceNotFoundException {
        authService.logout(response);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthResponse> refreshAccessToken(@CookieValue("refresh_token") String refreshToken) {
        return ApiResponse.<AuthResponse>builder()
                .status(HttpStatus.OK.value())
                .payload(authService.refreshAccessToken(refreshToken))
                .build();
    }

//    @GetMapping("/verify")
//    public ApiResponse<UserDTO> verifyUser(@RequestParam("token") String token) throws ResourceNotFoundException {
//        UserDTO userDTO = authService.verifyUser(token);
//
//        return ApiResponse.<UserDTO>builder()
//                .status(HttpStatus.OK.value())
//                .payload(userDTO)
//                .build();
//    }
//
//

//
//
//
//    @GetMapping("/success")
//    public ApiResponse<UserDTO> loginSuccess(@AuthenticationPrincipal OidcUser oidcUser) {
//        UserDTO userDTO = authService.processOAuthPostLogin(oidcUser);
//        return ApiResponse.<UserDTO>builder()
//                .status(HttpStatus.OK.value())
//                .payload(userDTO)
//                .build();
//    }
//
//
//
//    @GetMapping("/failure")
//    public ApiResponse<String> loginFailure() {
//        return ApiResponse.<String>builder()
//                .status(HttpStatus.UNAUTHORIZED.value())
//                .payload("Login failed")
//                .build();
//    }
//
//    @PostMapping("/forgot-password")
//    public ApiResponse<String> forgotPassword(@RequestParam String email, @RequestParam String siteUrl) {
//        try {
//            authService.forgotPassword(email, siteUrl);
//            return ApiResponse.<String>builder()
//                    .status(HttpStatus.OK.value())
//                    .payload("Password reset link has been sent to your email.")
//                    .build();
//        } catch (Exception e) {
//            return ApiResponse.<String>builder()
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                    .payload(e.getMessage())
//                    .build();
//        }
//    }
//
//    @PostMapping("/reset-password")
//    public ApiResponse<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
//        try {
//            authService.resetPassword(token, newPassword);
//            return ApiResponse.<String>builder()
//                    .status(HttpStatus.OK.value())
//                    .payload("Password has been reset successfully.")
//                    .build();
//        } catch (Exception e) {
//            return ApiResponse.<String>builder()
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                    .payload(e.getMessage())
//                    .build();
//        }
//    }
//
//
//    @GetMapping("/verify-reset-token")
//    public ApiResponse<Void> verifyResetToken(@RequestParam String token) throws ResourceNotFoundException {
//        authService.verifyResetToken(token);
//        return ApiResponse.<Void>builder()
//                .status(HttpStatus.OK.value())
//                .build();
//    }

}
