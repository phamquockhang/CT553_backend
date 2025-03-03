package com.pqkhang.ct553_backend.domain.category.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.category.dto.ProductDTO;
import com.pqkhang.ct553_backend.domain.category.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ApiResponse<ProductDTO> getProduct(@PathVariable("productId") Integer productId) throws ResourceNotFoundException {
        return ApiResponse.<ProductDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(productService.getProduct(productId))
                .message("Lấy sản phẩm thành công")
                .build();
    }

    @GetMapping
    public ApiResponse<Page<ProductDTO>> getProducts(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<ProductDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(productService.getProducts(params))
                .message("Lấy tất cả sản phẩm thành công")
                .build();
    }

    @GetMapping("/public")
    public ApiResponse<Page<ProductDTO>> getProductsPublicApi(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<ProductDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(productService.getProductsPublicApi(params))
                .message("Lấy tất cả sản phẩm thành công")
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<ProductDTO>> getAllProducts() {
        return ApiResponse.<List<ProductDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(productService.getAllProducts())
                .message("Lấy tất cả sản phẩm thành công")
                .build();
    }

    @PostMapping
    public ApiResponse<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) throws ResourceNotFoundException {
        return ApiResponse.<ProductDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(productService.createProduct(productDTO))
                .message("Tạo sản phẩm thành công")
                .build();
    }

    @PutMapping("/{productId}")
    public ApiResponse<ProductDTO> updateProduct(@PathVariable("productId") Integer productId, @RequestBody ProductDTO productDTO) throws ResourceNotFoundException {
        return ApiResponse.<ProductDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(productService.updateProduct(productId, productDTO))
                .message("Cập nhật sản phẩm thành công")
                .build();
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable("productId") Integer productId) throws ResourceNotFoundException {
        productService.deleteProduct(productId);
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Xóa sản phẩm thành công")
                .build();
    }

}
