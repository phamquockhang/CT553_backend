package com.pqkhang.ct553_backend.domain.booking.product;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ProductService {
    Page<ProductDTO> getProducts(Map<String, String> params) throws ResourceNotFoundException;

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Integer ProductId) throws ResourceNotFoundException;

    ProductDTO createProduct(@Valid ProductDTO productDTO) throws ResourceNotFoundException;

    ProductDTO updateProduct(Integer productId, @Valid ProductDTO productDTO) throws ResourceNotFoundException;

    void deleteProduct(Integer productId) throws ResourceNotFoundException;

    boolean existsByName(String name);
}
