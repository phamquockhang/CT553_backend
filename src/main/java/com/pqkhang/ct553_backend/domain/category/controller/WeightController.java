package com.pqkhang.ct553_backend.domain.category.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.domain.category.dto.WeightDTO;
import com.pqkhang.ct553_backend.domain.category.service.WeightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weights")
@RequiredArgsConstructor
public class WeightController {

    private final WeightService weightService;

    @GetMapping("/{productId}")
    public ApiResponse<List<WeightDTO>> getAllWeightsByProductId(@PathVariable("productId") Integer productId) throws ResourceNotFoundException {
        return ApiResponse.<List<WeightDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(weightService.getAllWeightsByProductId(productId))
                .message("Get all weights for product with ID " + productId + " successfully")
                .build();
    }

    @GetMapping("/current/{productId}")
    public ApiResponse<WeightDTO> getCurrentWeightByProductId(@PathVariable("productId") Integer productId) throws ResourceNotFoundException {
        return ApiResponse.<WeightDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(weightService.getCurrentWeightByProductId(productId))
                .message("Get current weight for product with ID " + productId + " successfully")
                .build();
    }

    @PostMapping("/{productId}")
    public ApiResponse<WeightDTO> createWeight(@PathVariable("productId") Integer productId, @Valid @RequestBody WeightDTO WeightDTO) throws ResourceNotFoundException {
        return ApiResponse.<WeightDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(weightService.createWeight(productId, WeightDTO))
                .message("Create weight for product with ID " + productId + " successfully")
                .build();
    }

}
