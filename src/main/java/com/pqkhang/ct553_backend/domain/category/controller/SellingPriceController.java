package com.pqkhang.ct553_backend.domain.category.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.domain.category.dto.SellingPriceDTO;
import com.pqkhang.ct553_backend.domain.category.service.SellingPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/selling_prices")
@RequiredArgsConstructor
public class SellingPriceController {

    private final SellingPriceService sellingPriceService;

    @GetMapping("/{productId}")
    public ApiResponse<List<SellingPriceDTO>> getAllSellingPricesByProductId(@PathVariable("productId") Integer productId) throws ResourceNotFoundException {
        return ApiResponse.<List<SellingPriceDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(sellingPriceService.getAllSellingPricesByProductId(productId))
                .message("Get all selling prices for product with ID " + productId + " successfully")
                .build();
    }

    @GetMapping("/current/{productId}")
    public ApiResponse<SellingPriceDTO> getCurrentSellingPriceByProductId(@PathVariable("productId") Integer productId) throws ResourceNotFoundException {
        return ApiResponse.<SellingPriceDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(sellingPriceService.getCurrentSellingPriceByProductId(productId))
                .message("Get current selling price for product with ID " + productId + " successfully")
                .build();
    }

    @PostMapping("/{productId}")
    public ApiResponse<SellingPriceDTO> createSellingPrice(@PathVariable("productId") Integer productId, @Valid @RequestBody SellingPriceDTO sellingPriceDTO) throws ResourceNotFoundException {
        return ApiResponse.<SellingPriceDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(sellingPriceService.createSellingPrice(productId, sellingPriceDTO))
                .message("Create selling price for product with ID " + productId + " successfully")
                .build();
    }

}
