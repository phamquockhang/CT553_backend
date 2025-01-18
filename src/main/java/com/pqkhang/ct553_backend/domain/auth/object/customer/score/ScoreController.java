package com.pqkhang.ct553_backend.domain.auth.object.customer.score;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @GetMapping("/{customerId}")
    public ApiResponse<List<ScoreDTO>> getAllScoresByCustomerId(@PathVariable("customerId") UUID customerId) throws ResourceNotFoundException {
        return ApiResponse.<List<ScoreDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(scoreService.getAllScoresByCustomerId(customerId))
                .message("Get all scores for customer with ID " + customerId + " successfully")
                .build();
    }

    @GetMapping("/current/{customerId}")
    public ApiResponse<ScoreDTO> getCurrentScoreByCustomerId(@PathVariable("customerId") UUID customerId) throws ResourceNotFoundException {
        return ApiResponse.<ScoreDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(scoreService.getCurrentScoreByCustomerId(customerId))
                .message("Get current score for customer with ID " + customerId + " successfully")
                .build();
    }

    @PostMapping("/{customerId}")
    public ApiResponse<ScoreDTO> createScore(@PathVariable("customerId") UUID customerId, @Valid @RequestBody ScoreDTO scoreDTO) throws ResourceNotFoundException {
        return ApiResponse.<ScoreDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(scoreService.createScore(customerId, scoreDTO))
                .message("Create score successfully")
                .build();
    }
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
