package com.pqkhang.ct553_backend.domain.booking.product.product_image;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product_images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @GetMapping("/{productId}")
    public ApiResponse<List<ProductImageDTO>> getAllProductImagesByProductId(@PathVariable Integer productId) {
        return ApiResponse.<List<ProductImageDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách ảnh sản phẩm thành công")
                .payload(productImageService.getAllProductImagesByProductId(productId))
                .build();
    }

    @PostMapping
    public ApiResponse<ProductImageDTO> createProductImage(@ModelAttribute @Valid ProductImageDTO productImageDTO, @RequestParam("productImageFiles") List<MultipartFile> productImageFiles) throws ResourceNotFoundException {
//        productImageService.createProductImage(productImageDTO, productImageFiles);
        return ApiResponse.<ProductImageDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Tạo ảnh sản phẩm thành công")
                .payload(productImageService.createProductImage(productImageDTO, productImageFiles))
                .build();
    }

    @PutMapping("/{productId}")
    public ApiResponse<ProductImageDTO> updateAllProductImageByProductId(@ModelAttribute @Valid ProductImageDTO productImageDTO, @RequestParam("productImageFiles") List<MultipartFile> productImageFiles) throws ResourceNotFoundException {
        return ApiResponse.<ProductImageDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Cập nhật ảnh sản phẩm thành công")
                .payload(productImageService.updateAllProductImageByProductId(productImageDTO, productImageFiles))
                .build();
    }

    @DeleteMapping("/{productImageId}")
    public ApiResponse<Void> deleteProductImage(@PathVariable Integer productImageId) throws ResourceNotFoundException {
        productImageService.deleteProductImage(productImageId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Xóa ảnh sản phẩm thành công")
                .build();
    }
}