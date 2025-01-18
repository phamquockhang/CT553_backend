package com.pqkhang.ct553_backend.domain.auth.object.customer.address;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/{customerId}")
    public ApiResponse<List<AddressDTO>> getAllAddressesByCustomerId(@PathVariable("customerId") UUID customerId) throws ResourceNotFoundException {
        return ApiResponse.<List<AddressDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(addressService.getAllAddressesByCustomerId(customerId))
                .message("Get all addresses for customer with ID " + customerId + " successfully")
                .build();
    }

    @GetMapping("/default/{customerId}")
    public ApiResponse<AddressDTO> getDefaultAddressByCustomerId(@PathVariable("customerId") UUID customerId) throws ResourceNotFoundException {
        return ApiResponse.<AddressDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(addressService.getDefaultAddressByCustomerId(customerId))
                .message("Get default address for customer with ID " + customerId + " successfully")
                .build();
    }

    @PostMapping("/{customerId}")
    public ApiResponse<AddressDTO> createAddress(@PathVariable("customerId") UUID customerId, @Valid @RequestBody AddressDTO addressDTO) throws ResourceNotFoundException {
        return ApiResponse.<AddressDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(addressService.createAddress(customerId, addressDTO))
                .message("Create address successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<AddressDTO> updateAddress(@PathVariable("id") UUID id, @Valid @RequestBody AddressDTO addressDTO) throws ResourceNotFoundException {
        return ApiResponse.<AddressDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(addressService.updateAddress(id, addressDTO))
                .message("Update address successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Map<String, Boolean>> deleteAddress(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        addressService.deleteAddress(id);
        return ApiResponse.<Map<String, Boolean>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(Map.of("deleted", Boolean.TRUE))
                .message("Delete address successfully")
                .build();
    }

}
