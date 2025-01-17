package com.pqkhang.ct553_backend.domain.auth.object.customer.score;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.auth.request.ChangePasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/scores")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;



//    @GetMapping("/all")
//    public ApiResponse<List<ScoreDTO>> getAllScoresByCustomerId
//        return ApiResponse.<List<ScoreDTO>>builder()
//                .status(HttpStatus.OK.value())
//                .success(true)
//                .payload(scoreService.getAllCustomers())
//                .message("Get all customers successfully")
//                .build();
//    }

    @GetMapping("/{customerId}")
    public ApiResponse<List<ScoreDTO>> getAllScoresByCustomerId(@PathVariable("customerId") UUID customerId) throws ResourceNotFoundException {
        return ApiResponse.<List<ScoreDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(scoreService.getAllScoresByCustomerId(customerId))
                .message("Get all scores for customer with ID " + customerId + " successfully")
                .build();
    }

//    @PostMapping
//    public ApiResponse<ScoreDTO> createCustomer(@Valid @RequestBody ScoreDTO scoreDTO) throws ResourceNotFoundException {
//        return ApiResponse.<ScoreDTO>builder()
//                .status(HttpStatus.CREATED.value())
//                .success(true)
//                .payload(scoreService.createCustomer(scoreDTO))
//                .message("Customer created successfully")
//                .build();
//    }
//
//    @GetMapping("/logged-in")
//    public ApiResponse<ScoreDTO> getLoggedInCustomer() {
//        return ApiResponse.<ScoreDTO>builder()
//                .status(HttpStatus.OK.value())
//                .success(true)
//                .payload(scoreService.getLoggedInCustomer())
//                .message("Get logged in customer successfully")
//                .build();
//    }
//
//    @PutMapping("/{id}")
//    public ApiResponse<ScoreDTO> updateCustomer(@PathVariable("id") UUID id, @RequestBody ScoreDTO scoreDTO) throws ResourceNotFoundException {
//        return ApiResponse.<ScoreDTO>builder()
//                .status(HttpStatus.OK.value())
//                .success(true)
//                .payload(scoreService.updateCustomer(id, scoreDTO))
//                .message("Cập nhật khách hàng thành công")
//                .build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<Void> deleteCustomer(@PathVariable("id") UUID id) throws ResourceNotFoundException {
//        scoreService.deleteCustomer(id);
//        return ApiResponse.<Void>builder()
//                .status(HttpStatus.OK.value())
//                .success(true)
//                .message("Xóa khách hàng thành công")
//                .build();
//    }
//
//    @PutMapping("/{id}/change-password")
//    public ApiResponse<Void> changePassword(@PathVariable UUID id, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) throws ResourceNotFoundException {
//        scoreService.changePassword(id, changePasswordRequest);
//        return ApiResponse.<Void>builder()
//                .status(HttpStatus.OK.value())
//                .success(true)
//                .message("Đổi mật khẩu thành công")
//                .build();
//    }
}
