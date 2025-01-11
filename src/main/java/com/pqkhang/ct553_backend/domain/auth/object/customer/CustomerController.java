package com.pqkhang.ct553_backend.domain.auth.object.customer;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.auth.request.ChangePasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ApiResponse<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws ResourceNotFoundException {
        return ApiResponse.<CustomerDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(customerService.createCustomer(customerDTO))
                .message("Customer created successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<Page<CustomerDTO>> getCustomers(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<CustomerDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(customerService.getCustomers(params))
                .message("Get all customers successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerDTO> getCustomerById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        return ApiResponse.<CustomerDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(customerService.getCustomerById(id))
                .message("Get customer has id " + id + " successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CustomerDTO> updateCustomer(@PathVariable("id") UUID id, @RequestBody CustomerDTO customerDTO) throws ResourceNotFoundException {
        return ApiResponse.<CustomerDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(customerService.updateCustomer(id, customerDTO))
                .message("Update customer has id " + id + " successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCustomer(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        customerService.deleteCustomer(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Delete customer has id " + id + " successfully")
                .build();
    }

    @PutMapping("/{id}/change-password")
    public ApiResponse<Void> changePassword(@PathVariable UUID id, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) throws ResourceNotFoundException {
        customerService.changePassword(id, changePasswordRequest);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Change password successfully")
                .build();
    }
}
