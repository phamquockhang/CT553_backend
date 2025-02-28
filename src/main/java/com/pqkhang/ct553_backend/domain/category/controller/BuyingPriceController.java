package com.pqkhang.ct553_backend.domain.category.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.domain.category.dto.BuyingPriceDTO;
import com.pqkhang.ct553_backend.domain.category.service.BuyingPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buying_prices")
@RequiredArgsConstructor
public class BuyingPriceController {

    private final BuyingPriceService buyingPriceService;

    @GetMapping("/{productId}")
    public ApiResponse<List<BuyingPriceDTO>> getAllBuyingPricesByProductId(@PathVariable("productId") Integer productId) throws ResourceNotFoundException {
        return ApiResponse.<List<BuyingPriceDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(buyingPriceService.getAllBuyingPricesByProductId(productId))
                .message("Get all buying prices for product with ID " + productId + " successfully")
                .build();
    }

    @GetMapping("/current/{productId}")
    public ApiResponse<BuyingPriceDTO> getCurrentBuyingPriceByProductId(@PathVariable("productId") Integer productId) throws ResourceNotFoundException {
        return ApiResponse.<BuyingPriceDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(buyingPriceService.getCurrentBuyingPriceByProductId(productId))
                .message("Get current buying price for product with ID " + productId + " successfully")
                .build();
    }

    @PostMapping("/{productId}")
    public ApiResponse<BuyingPriceDTO> createBuyingPrice(@PathVariable("productId") Integer productId, @Valid @RequestBody BuyingPriceDTO buyingPriceDTO) throws ResourceNotFoundException {
        return ApiResponse.<BuyingPriceDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(buyingPriceService.createBuyingPrice(productId, buyingPriceDTO))
                .message("Create buying price for product with ID " + productId + " successfully")
                .build();
    }

}
